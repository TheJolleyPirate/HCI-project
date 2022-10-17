package hci.hciprojectvoiceinterface;

import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class CustomUtteranceListener extends UtteranceProgressListener {
    public boolean startRecordingWhenDone = false;
    public MainActivity mainActivity = null;
    public View rootView = null;
    public TextView textView = null;

    @Override
    public void onStart(String s) {

    }

    @Override
    public void onDone(String s) {
        //If we need clarification, enable the recording automatically when the utterance is complete
        Log.i("TextToSpeechCallback", "Text to speech is done!");
        if(startRecordingWhenDone && mainActivity != null && rootView != null){
            //SpeechRecogniser needs to be run on the same thread so we call toggleRecordingState from the main thread
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mainActivity.toggleRecordingState(rootView);
                }
            });
        }
    }

    @Override
    public void onError(String s) {

    }
}
