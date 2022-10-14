package hci.hciprojectvoiceinterface;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import hci.hciprojectvoiceinterface.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

import android.speech.SpeechRecognizer;
import android.speech.RecognitionListener;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private CustomRecogniser customRecogniser = new CustomRecogniser();
    private SpeechRecognizer speechRecognizer;
    private Context context;

    private boolean recordingState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext(); //Get context
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context); //Get speech recogniser from context
        speechRecognizer.setRecognitionListener(customRecogniser); //Setup custom callback to handle recognised text

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        //Init custom recogniser
        customRecogniser.textView = binding.textviewFirst;
        customRecogniser.rootView = getWindow().getDecorView();
        customRecogniser.mainActivity = this;

        binding.buttonFirst.setText((CharSequence) "Record Toggle");
        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleRecordingState(view);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void toggleRecordingState(View view){
        if(!recordingState) {
            if(ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED){
                ActivityCompat.requestPermissions (MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, 100);
            }
            else {
                Snackbar.make(view, "Started Recording... Press Again To Stop", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                speechRecognizer.startListening(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH));
                recordingState = true;
            }
        }
        else{
            recordingState = false;
            speechRecognizer.stopListening();
            Snackbar.make(view, "Recording Finished", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }
}