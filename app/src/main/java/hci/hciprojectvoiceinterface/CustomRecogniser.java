package hci.hciprojectvoiceinterface;

import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomRecogniser implements RecognitionListener {

    public String response = "";
    public TextView textView = null;
    public View rootView = null;
    public MainActivity mainActivity = null;
    public TextToSpeech textToSpeech = null;

    @Override
    public void onReadyForSpeech(Bundle bundle) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float v) {

    }

    @Override
    public void onBufferReceived(byte[] bytes) {

    }

    @Override
    public void onEndOfSpeech() {
        if(mainActivity != null && rootView != null){
            mainActivity.toggleRecordingState(rootView);
        }
    }

    @Override
    public void onError(int i) {

    }

    @Override
    public void onResults(Bundle bundle) {
        String outputText = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0);
        response = parseFeedback(bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0));
        outputText += "\n"+response;
        if(textView != null){
            textView.setText(outputText);
        }
        if(textToSpeech != null){
            textToSpeech.speak(response, TextToSpeech.QUEUE_ADD, null);
        }
    }

    @Override
    public void onPartialResults(Bundle bundle) {

    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }

    private String parseFeedback(String transcribedSpeech){
        if(transcribedSpeech.contains("control")){
            return "Logging feedback for controls.";
        }
        if(transcribedSpeech.contains("sound") || transcribedSpeech.contains("audio")){
            return "Logging feedback for audio.";
        }
        if(transcribedSpeech.contains("graphic") || transcribedSpeech.contains("texture") || transcribedSpeech.contains("model")){
            return "Logging feedback for graphics.";
        }
        if(transcribedSpeech.contains("close") && transcribedSpeech.contains("pod") && transcribedSpeech.contains("bay") && transcribedSpeech.contains("doors")){
            return "I'm sorry Dave, I'm afraid I can't do that.";
        }
        return "I'm not sure I understood that.";
    }
}
