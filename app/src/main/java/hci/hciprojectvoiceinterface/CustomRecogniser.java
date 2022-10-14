package hci.hciprojectvoiceinterface;

import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomRecogniser implements RecognitionListener {

    public ArrayList<String> lastResults = new ArrayList<String>();
    public TextView textView = null;
    public View rootView = null;
    public MainActivity mainActivity = null;

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
        lastResults = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if(textView != null){
            textView.setText(lastResults.get(0));
        }
    }

    @Override
    public void onPartialResults(Bundle bundle) {

    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }
}
