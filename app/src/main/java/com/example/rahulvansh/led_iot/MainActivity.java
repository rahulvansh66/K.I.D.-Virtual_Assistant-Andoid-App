package com.example.rahulvansh.led_iot;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.ActivityNotFoundException;
import android.speech.tts.TextToSpeech;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import com.example.rahulvansh.led_iot.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static java.lang.Boolean.parseBoolean;

public class MainActivity extends AppCompatActivity {

    Switch switchButton, switchButton2;

    DatabaseReference ref_switch1 = FirebaseDatabase.getInstance().getReferenceFromUrl("YOUR_FIREBASE_PROJECT_LINK/home/led1");
    DatabaseReference ref_switch2 = FirebaseDatabase.getInstance().getReferenceFromUrl("YOUR_FIREBASE_PROJECT_LINK/home/led2");
    DatabaseReference input_win_flag = FirebaseDatabase.getInstance().getReferenceFromUrl("YOUR_FIREBASE_PROJECT_LINK/desktop/flag");
    DatabaseReference input_win = FirebaseDatabase.getInstance().getReferenceFromUrl("YOUR_FIREBASE_PROJECT_LINK/desktop/input");
    DatabaseReference input_pi = FirebaseDatabase.getInstance().getReferenceFromUrl("YOUR_FIREBASE_PROJECT_LINK/pi/input");
    DatabaseReference input_pi_flag = FirebaseDatabase.getInstance().getReferenceFromUrl("YOUR_FIREBASE_PROJECT_LINK/pi/flag");
    DatabaseReference input_vision = FirebaseDatabase.getInstance().getReferenceFromUrl("YOUR_FIREBASE_PROJECT_LINK/smart_vision/input");
    DatabaseReference input_vision_flag = FirebaseDatabase.getInstance().getReferenceFromUrl("YOUR_FIREBASE_PROJECT_LINK/smart_vision/flag");
    public TextView textView, textView2, mVoiceInputTv,speech_text;
    public TextToSpeech t1;
    public String value,value2;
    private ImageButton mSpeakBtn;
    public RadioGroup radioGroup_1;
    public RadioButton rb2;
    public int flag=0;
    private static final int REQ_CODE_SPEECH_INPUT = 100;
    String switchOn = "Switch is ON";
    String switchOff = "Switch is OFF";
    public static boolean status1,status2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.US);
                }
            }
        });

        // For first switch button
        switchButton = (Switch) findViewById(R.id.switchButton);
        textView = (TextView) findViewById(R.id.textView);
        rb2 = (RadioButton) findViewById(R.id.radiobutton2);
        rb2.setChecked(true);
        mVoiceInputTv = (TextView) findViewById(R.id.voiceInput);
        mSpeakBtn = (ImageButton) findViewById(R.id.btnSpeak);
        mSpeakBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startVoiceInput();
            }
        });


        // Read from the database
        ref_switch1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                value = dataSnapshot.getValue(String.class);
                status1 = Boolean.parseBoolean(value);
                if(value.equals("1"))
                {
                    switchButton.setChecked(true);
                }
                else
                {
                    switchButton.setChecked(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        //switchButton.setChecked(false);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    textView.setText(switchOn);
                    ref_switch1.setValue("1");
                    t1.speak("Switch 1 is turing on", TextToSpeech.QUEUE_FLUSH, null);
                }
                else {
                    textView.setText(switchOff);
                    ref_switch1.setValue("0");
                    t1.speak("Switch 1 is turing off", TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });

        if (switchButton.isChecked()) {
            textView.setText(switchOn);
        } else {
            textView.setText(switchOff);
        }

        // for second switch button
        switchButton2 = (Switch) findViewById(R.id.switchButton2);
        textView2 = (TextView) findViewById(R.id.textView2);

        ref_switch2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                value2 = dataSnapshot.getValue(String.class);
                status1 = Boolean.parseBoolean(value2);

                // if (input.equals("turn on the led"))<-------->
                if(value2.equals("1"))
                {
                    switchButton2.setChecked(true);
                }
                else
                {
                    switchButton2.setChecked(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        switchButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    textView2.setText(switchOn);
                    ref_switch2.setValue("1");
                    t1.speak("Switch 2 is turing on", TextToSpeech.QUEUE_FLUSH, null);
                } else {
                    textView2.setText(switchOff);
                    ref_switch2.setValue("0");
                    t1.speak("Switch 2 is turing off", TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });

        if (switchButton2.isChecked()) {
            textView2.setText(switchOn);
        } else {
            textView2.setText(switchOff);
        }


    }
    public void onRadioButtonClicked(View view) {

         String selection = "";

        switch (view.getId()) {
            case R.id.radiobutton2:
                flag = 1;
                selection = "Ask to KID";
                break;

            case R.id.radiobutton:
                flag = 0;
                selection = "IOT board";
                break;

            case R.id.radiobutton1:
                flag = 2;
                selection = "Smart Vision";
                break;

        }
        Toast.makeText(this,selection , Toast.LENGTH_SHORT).show();
    }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Start speaking...");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String input = result.get(0).toString();
                    speech_text = (TextView) findViewById(R.id.textView3);
                    speech_text.setText("You just said: "+input);
                    if (flag == 1) {
                        input_win_flag.setValue("1");
                        input_win.setValue(input);
                        t1.speak("Let me think...", TextToSpeech.QUEUE_FLUSH, null);
                    }
                    else if(flag == 2)
                    {
                        input_vision_flag.setValue("1");
                        input_vision.setValue(input);
                        t1.speak("Let me think...", TextToSpeech.QUEUE_FLUSH, null);
                    }
                    else{
                        input_pi_flag.setValue("1");
                        input_pi.setValue(input);
                        //t1.speak("I am thinking", TextToSpeech.QUEUE_FLUSH, null);
                        if (input.equalsIgnoreCase("turn on switch 1") || input.equalsIgnoreCase("turn on switch one") || input.equalsIgnoreCase("on switch 1") || input.equalsIgnoreCase("on switch one")) {
                            if (value.equals("1")) {
                                t1.speak("Switch 1 is already on", TextToSpeech.QUEUE_FLUSH, null);
                                Toast.makeText(getApplicationContext(), "switch 1 is already on", Toast.LENGTH_SHORT).show();
                            } else {
                                switchButton.setChecked(true);
                                t1.speak("Switch 1 is turing on", TextToSpeech.QUEUE_FLUSH, null);
                            }
                        } else if (input.equalsIgnoreCase("turn on switch 2") || input.equalsIgnoreCase("turn on switch to") || input.equalsIgnoreCase("turn on switch two") || input.equalsIgnoreCase("on switch 2") || input.equalsIgnoreCase("on switch to") || input.equalsIgnoreCase("on switch two")) {
                            if (value2.equals("1")) {
                                t1.speak("Switch 2 is already on", TextToSpeech.QUEUE_FLUSH, null);
                                Toast.makeText(getApplicationContext(), "switch 2 is already on", Toast.LENGTH_SHORT).show();
                            } else {
                                switchButton2.setChecked(true);
                                t1.speak("Switch 2 is turing on", TextToSpeech.QUEUE_FLUSH, null);
                            }
                        } else if (input.equalsIgnoreCase("turn off switch 1") || input.equalsIgnoreCase("turn off switch one") || input.equalsIgnoreCase("off switch 1") || input.equalsIgnoreCase("off switch one")) {
                            if (value.equals("0")) {
                                t1.speak("Switch 1 is already off", TextToSpeech.QUEUE_FLUSH, null);
                                Toast.makeText(getApplicationContext(), "switch 1 is already off", Toast.LENGTH_SHORT).show();
                            } else {
                                switchButton.setChecked(false);
                                t1.speak("Switch 1 is turing off", TextToSpeech.QUEUE_FLUSH, null);
                            }
                        } else if (input.equalsIgnoreCase("turn off switch 2") || input.equalsIgnoreCase("turn off switch two") || input.equalsIgnoreCase("turn off switch to") || input.equalsIgnoreCase("off switch 2") || input.equalsIgnoreCase("off switch two") || input.equalsIgnoreCase("off switch to")) {
                            if (value2.equals("0")) {
                                t1.speak("Switch 2 is already off", TextToSpeech.QUEUE_FLUSH, null);
                                Toast.makeText(getApplicationContext(), "switch 2 is already off", Toast.LENGTH_SHORT).show();
                            } else {
                                switchButton2.setChecked(false);
                                t1.speak("Switch 2 is turing off", TextToSpeech.QUEUE_FLUSH, null);
                            }
                        } else {
                            //mVoiceInputTv.setText("Please try again");
                            t1.speak("It's wrong command, please try again", TextToSpeech.QUEUE_FLUSH, null);
                            Toast.makeText(getApplicationContext(), "Wrong Command", Toast.LENGTH_SHORT).show();
                        }

                        break;
                    }
                }

            }
        }
    }


}