package com.nitinr.livelink;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class SttService {
    //Singleton instance
    public static SttService SttService_instance = null;
    private Context context;

    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;
    public String text;

    private SttService (Context context){
        this.context = context;
        text = "";
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        setListener();
    };

    private void setListener() {
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> queries = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                if (queries != null) {
                    text = queries.get(0);

                    Toast.makeText(context, text, Toast.LENGTH_LONG)
                            .show();

                    text = "";
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });
    }

    public static SttService getInstance(Context context) {
        if (SttService_instance == null)
            SttService_instance = new SttService(context);

        return SttService_instance;
    }

    public void startSpeechToText() {
        speechRecognizer.startListening(speechRecognizerIntent);
    }

    public void stopSpeechToText() {
        speechRecognizer.stopListening();
    }
}
