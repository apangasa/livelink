package com.nitinr.livelink;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

public class SttService {
    //Singleton instance
    public static SttService SttService_instance = null;
    private Context context;

    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;
    public String query;

    private SttService (Context context){
        this.context = context;
        query = "";
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
                ArrayList<String> capturedText = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                if (capturedText != null) {
                    query = Arrays.stream(capturedText.get(0).split(" "))
                            .filter(e -> !Phrases.phraseSet.contains(e))
                            .map(String::toLowerCase)
                            .findFirst()
                            .orElse("");
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

    private void checkVoicePermission() {
        if (!(ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + context.getApplicationContext().getPackageName()));
            context.startActivity(intent);
        }
    }

    public void startSpeechToText() {
        checkVoicePermission();

        speechRecognizer.startListening(speechRecognizerIntent);
    }

    public void stopSpeechToText() {
        speechRecognizer.stopListening();
    }
}
