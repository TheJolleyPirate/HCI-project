package hci.hciprojectvoiceinterface;

import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.TextView;

import androidx.core.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomRecogniser implements RecognitionListener {

    public String response = "";
    public TextView textView = null;
    public View rootView = null;
    public MainActivity mainActivity = null;
    public TextToSpeech textToSpeech = null;
    public ClarificationType clarificationInput = ClarificationType.NONE;
    public CustomUtteranceListener textToSpeechListener = new CustomUtteranceListener();

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
        ArrayList<String> recogResults = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if(recogResults.size() != 0){
            String outputText = recogResults.get(0);
            Pair<String, ClarificationType> ret = parseFeedback(recogResults.get(0));
            response = ret.first;
            clarificationInput = ret.second;
            textToSpeechListener.startRecordingWhenDone = (clarificationInput != ClarificationType.NONE);
            outputText += "\n"+response;
            if(textView != null){
                textView.setText(outputText);
            }
            if(textToSpeech != null){
                HashMap<String, String> params = new HashMap<>();
                params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, response);
                textToSpeech.speak(response, TextToSpeech.QUEUE_ADD, params);
            }
        }
    }

    @Override
    public void onPartialResults(Bundle bundle) {

    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }

    //Returns the response string along with whether it needs clarification. If it's not clear, wait for clarification.
    private Pair<String, ClarificationType> parseFeedback(String transcribedSpeech){
        switch(clarificationInput) {
            case NONE:
                if(transcribedSpeech.contains("introduce yourself")){
                    return new Pair<>("Hi! I'm Dakk, I'm designed to receive and log your feedback just by listening to you speak.", ClarificationType.NONE);
                }
                if(transcribedSpeech.contains("environment")){
                    return new Pair<>("What specifically do you like about the environment?", ClarificationType.ENVIRONMENT);
                }
                if(transcribedSpeech.contains("ready to start")){
                    return new Pair<>("Great! Please let me know if you have any feedback.", ClarificationType.NONE);
                }
                if(transcribedSpeech.contains("finish the session")){
                    return new Pair<>("Ok. Do you mind if I ask you some questions about the session?", ClarificationType.SESSION_FINISH_1);
                }
                if(transcribedSpeech.contains("control") && transcribedSpeech.contains("clunky")){
                    return new Pair<>("The controls feel clunky? Tell me more about that.", ClarificationType.CONTROLS_CLUNKY);
                }
                if (transcribedSpeech.contains("control")) {
                    return new Pair<>("Logging feedback for controls.", ClarificationType.NONE);
                }
                if (transcribedSpeech.contains("sound") || transcribedSpeech.contains("audio")) {
                    return new Pair<>("Logging feedback for audio.", ClarificationType.NONE);
                }
                if (transcribedSpeech.contains("graphic") || transcribedSpeech.contains("texture") || transcribedSpeech.contains("model")) {
                    return new Pair<>("Logging feedback for graphics.", ClarificationType.NONE);
                }
                if(transcribedSpeech.contains("ui") || transcribedSpeech.contains("menu") || transcribedSpeech.contains("user interface")){
                    return new Pair<>("Logging feedback for user interface", ClarificationType.NONE);
                }
                if (transcribedSpeech.contains("game suck")) {
                    return new Pair<>("Skill issue.", ClarificationType.NONE);
                }
                if (transcribedSpeech.contains("close the pod bay door")) {
                    return new Pair<>("I'm sorry Dave, I'm afraid I can't do that.", ClarificationType.NONE);
                }
                return new Pair<>("I'm not sure how to categorise that. Please give me a name for that category.", ClarificationType.NEW_CATEGORY);

            case NEW_CATEGORY:
                return new Pair<>("Ok. I'm adding "+transcribedSpeech+" as a new category.", ClarificationType.NONE);

            case CONTROLS_CLUNKY:
                return new Pair<>("Thanks. I’ve logged your feedback on the controls", ClarificationType.NONE);

            case ENVIRONMENT:
                return new Pair<>("Thank you. I’ve noted your feedback about the environment", ClarificationType.NONE);

            case SESSION_FINISH_1:
                return new Pair<>("What was your favourite moment or aspect of the session?", ClarificationType.SESSION_FINISH_2);

            case SESSION_FINISH_2:
                return new Pair<>("How do you like this game?", ClarificationType.SESSION_FINISH_3);

            case SESSION_FINISH_3:
                return new Pair<>("Great, thank you. Was there anything you would have liked to do that you couldn’t?", ClarificationType.SESSION_FINISH_4);

            case SESSION_FINISH_4:
                return new Pair<>("Thank you. That’s all the questions I have for today. Thank you for testing.", ClarificationType.NONE);

            default:
                return new Pair<>("Something went wrong! My Clarification Input seems to be invalid! This shouldn't be possible!", ClarificationType.NONE);

        }
    }

    public enum ClarificationType{
        NONE,
        NEW_CATEGORY,
        ENVIRONMENT,
        CONTROLS_CLUNKY,
        SESSION_FINISH_1,
        SESSION_FINISH_2,
        SESSION_FINISH_3,
        SESSION_FINISH_4
    }
}
