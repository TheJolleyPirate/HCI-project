package hci.hciprojectvoiceinterface;

import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.TextView;

import androidx.core.util.Pair;

import java.util.ArrayList;

public class CustomRecogniser implements RecognitionListener {

    public String response = "";
    public TextView textView = null;
    public View rootView = null;
    public MainActivity mainActivity = null;
    public TextToSpeech textToSpeech = null;
    public boolean clarificationInput = false;

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
        Pair<String, Boolean> ret = parseFeedback(bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0));
        response = ret.first;
        outputText += "\n"+response;
        if(textView != null){
            textView.setText(outputText);
        }
        if(textToSpeech != null){
            textToSpeech.speak(response, TextToSpeech.QUEUE_ADD, null);
        }
        clarificationInput = ret.second;
    }

    @Override
    public void onPartialResults(Bundle bundle) {

    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }

    //Returns the response string along with whether it needs clarification. If it's not clear, wait for clarification.
    private Pair<String, Boolean> parseFeedback(String transcribedSpeech){
        if(!clarificationInput) {
            if (transcribedSpeech.contains("control")) {
                return new Pair<>("Logging feedback for controls.", false);
            }
            if (transcribedSpeech.contains("sound") || transcribedSpeech.contains("audio")) {
                return new Pair<>("Logging feedback for audio.", false);
            }
            if (transcribedSpeech.contains("graphic") || transcribedSpeech.contains("texture") || transcribedSpeech.contains("model")) {
                return new Pair<>("Logging feedback for graphics.", false);
            }
            if (transcribedSpeech.contains("game suck")) {
                return new Pair<>("Skill issue.", false);
            }
            if (transcribedSpeech.contains("close the pod bay door")) {
                return new Pair<>("I'm sorry Dave, I'm afraid I can't do that.", false);
            }
            return new Pair<>("I'm not sure how to categorise that. Please give me a name for that category.", true);
        }
        else{
            return new Pair<>("Ok. I'm adding "+transcribedSpeech+" as a new category", false);
        }
    }
}
