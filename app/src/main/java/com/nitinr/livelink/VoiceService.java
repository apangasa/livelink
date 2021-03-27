package com.nitinr.livelink;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.Util;


public class VoiceService extends Service
{
    private static VoiceService.QueryListener queryListener = null;

    protected AudioManager mAudioManager;
    protected SpeechRecognizer mSpeechRecognizer;
    protected Intent mSpeechRecognizerIntent;
    protected final Messenger mServerMessenger = new Messenger(new IncomingHandler(this));

    protected boolean mIsListening;
    protected volatile boolean mIsCountDownOn;
    private boolean mIsStreamSolo;

    static final int MSG_RECOGNIZER_START_LISTENING = 1;
    static final int MSG_RECOGNIZER_CANCEL = 2;

    @Override
    public void onCreate()
    {
        super.onCreate();

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizer.setRecognitionListener(new SpeechRecognitionListener());
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                this.getPackageName());
    }

    public VoiceService() { // default constructor

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int cmd = super.onStartCommand(intent, flags, startId);

        Message msg = new Message();
        msg.what = MSG_RECOGNIZER_START_LISTENING;
        try {
            mServerMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return cmd;
    }

    public interface QueryListener {
        void onQueryCaptured(String query);
        void onDataLoaded(JSONObject data);
    }

    public void setQueryListener (QueryListener listener) {
        queryListener = listener;
    }

    public static void requestDataFromEndpoint(String url, String data) {
        JSONObject obj = new JSONObject();

        try {
            obj.put("img", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkHttpClient client = new OkHttpClient();
        final MediaType mediaType
                = MediaType.get("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(obj.toString(), mediaType);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject data = new JSONObject(Objects.requireNonNull(response.body()).string());

                        queryListener.onDataLoaded(data); // fire listener
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Log.d("RESULT", response.toString());
            }
        });
    }

    protected static class IncomingHandler extends Handler
    {
        private WeakReference<VoiceService> mtarget;

        IncomingHandler(VoiceService target)
        {
            mtarget = new WeakReference<VoiceService>(target);
        }


        @Override
        public void handleMessage(Message msg)
        {
            final VoiceService target = mtarget.get();

            switch (msg.what)
            {
                case MSG_RECOGNIZER_START_LISTENING:

                    if (!target.mIsStreamSolo)
                    {
                        target.mAudioManager.setStreamSolo(AudioManager.STREAM_VOICE_CALL, true);
                        target.mIsStreamSolo = true;
                    }

                    if (!target.mIsListening)
                    {
                        target.mSpeechRecognizer.startListening(target.mSpeechRecognizerIntent);
                        target.mIsListening = true;
                    }
                    break;

                case MSG_RECOGNIZER_CANCEL:
                    if (target.mIsStreamSolo)
                    {
                        target.mAudioManager.setStreamSolo(AudioManager.STREAM_VOICE_CALL, false);
                        target.mIsStreamSolo = false;
                    }
                    target.mSpeechRecognizer.cancel();
                    target.mIsListening = false;
                    break;
            }
        }
    }

    protected CountDownTimer mNoSpeechCountDown = new CountDownTimer(2000, 2000)
    {

        @Override
        public void onTick(long millisUntilFinished)
        {
            // TODO Auto-generated method stub

        }

        @Override
        public void onFinish()
        {
            mIsCountDownOn = false;
            Message message = Message.obtain(null, MSG_RECOGNIZER_CANCEL);
            try
            {
                mServerMessenger.send(message);
                message = Message.obtain(null, MSG_RECOGNIZER_START_LISTENING);
                mServerMessenger.send(message);
            }
            catch (RemoteException e)
            {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        if (mIsCountDownOn)
        {
            mNoSpeechCountDown.cancel();
        }
        if (mSpeechRecognizer != null)
        {
            mSpeechRecognizer.destroy();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected class SpeechRecognitionListener implements RecognitionListener
    {

        @Override
        public void onBeginningOfSpeech()
        {
            if (mIsCountDownOn)
            {
                mIsCountDownOn = false;
                mNoSpeechCountDown.cancel();
            }
        }

        @Override
        public void onBufferReceived(byte[] buffer)
        {

        }

        @Override
        public void onEndOfSpeech()
        {

        }

        @Override
        public void onError(int error)
        {
            if (mIsCountDownOn)
            {
                mIsCountDownOn = false;
                mNoSpeechCountDown.cancel();
            }
            mIsListening = false;
            Message message = Message.obtain(null, MSG_RECOGNIZER_START_LISTENING);
            try
            {
                mServerMessenger.send(message);
            }
            catch (RemoteException e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public void onEvent(int eventType, Bundle params)
        {

        }

        @Override
        public void onPartialResults(Bundle partialResults)
        {

        }

        @Override
        public void onReadyForSpeech(Bundle params)
        {
            mIsCountDownOn = true;
            mNoSpeechCountDown.start();

            Log.d("Note", "onReadyForSpeech"); //$NON-NLS-1$
        }

        @Override
        public void onResults(Bundle results)
        {
            ArrayList<String> capturedText = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

            if (capturedText != null) {
                String query = Arrays.stream(capturedText.get(0).split(" "))
                        .map(String::toLowerCase)
                        .filter(e -> Phrases.phraseSet.contains(e))
                        .findFirst()
                        .orElse("");

                String text = capturedText.get(0);


                if (queryListener != null) {
                    queryListener.onQueryCaptured(query);
                }

                Log.d("UserSaid", text);
                Log.d("Query", query);
            }
        }

        @Override
        public void onRmsChanged(float rmsdB)
        {

        }

    }
}

