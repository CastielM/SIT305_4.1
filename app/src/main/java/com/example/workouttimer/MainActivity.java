package com.example.workouttimer;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{

    Button startButton;
    Button stopButton;
    Button configureButton;
    ProgressBar timerBar;
    TextView timerText;
    TextView phase;
    EditText workoutDuration;
    EditText restDuration;
    EditText repetitions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);
        configureButton = findViewById(R.id.configureButton);
        timerText = findViewById(R.id.timerText);
        timerBar = findViewById(R.id.timerBar);
        phase = findViewById(R.id.phase);
        workoutDuration = findViewById(R.id.workoutDuration);
        restDuration = findViewById(R.id.restDuration);
        repetitions = findViewById(R.id.repetitions);
        stopButton.setText("Stop");
        startButton.setText("Start Workout");
        configureButton.setText("Set workout");
        timerBar.setProgress(100);
        timerText.setText(String.format("%02d:%02d", 0, 0));
        phase.setText("Configure your workout below");

        configureButton.setOnClickListener(new View.OnClickListener() {
            long timeRemaining = 0;

            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                if (workoutDuration.getText().toString().length() == 0) {
                    Toast.makeText(MainActivity.this, "Must enter workout duration", Toast.LENGTH_LONG).show();
                } else if (restDuration.getText().toString().length() == 0) {
                    Toast.makeText(MainActivity.this, "Must enter rest duration", Toast.LENGTH_LONG).show();
                } else if (repetitions.getText().toString().length() == 0) {
                    Toast.makeText(MainActivity.this, "Must enter number of sets", Toast.LENGTH_LONG).show();
                }
                else {
                    int setNumber = Integer.parseInt(repetitions.getText().toString());
                    long workoutTime = Long.parseLong(workoutDuration.getText().toString()) * 1000;
                    long restTime = Long.parseLong(restDuration.getText().toString()) * 1000;
                    long totalDuration = (workoutTime + restTime) * setNumber;
                    long phaseTotaled = (restTime + workoutTime) / 1000;

                    timerText.setText(String.format("%02d:%02d", (totalDuration / 1000) / 60, (totalDuration / 1000) % 60));
                    phase.setText("Click start to begin!");

                    CountDownTimer workoutTimer = new CountDownTimer(totalDuration, 1000) {

                        public void onTick(long millisUntilFinished) {

                            int remainingTime = LongToInt(millisUntilFinished / 1000);
                            int totalTime = LongToInt(totalDuration) / 1000;
                            int percentage = 100 * remainingTime / totalTime;
                            int checkPhase1 = remainingTime % LongToInt(phaseTotaled);
                            int checkPhase2 = (remainingTime - LongToInt(restTime)) % LongToInt(phaseTotaled);
                            if (checkPhase1 == 0)
                            {
                                phase.setText("Workout!");
                            }
                            if (checkPhase2 == 0)
                            {
                                phase.setText("Rest!");
                            }

                            timeRemaining = millisUntilFinished;

                            timerBar.setProgress(percentage);
                            long minutes = (millisUntilFinished / 1000) / 60;
                            long seconds = (millisUntilFinished / 1000) % 60;
                            timerText.setText(String.format("%02d:%02d", minutes, seconds));
                        }

                        public void onFinish() {
                            phase.setText("Workout Complete!");

                        }




                    };

                    stopButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            workoutTimer.cancel();
                        }
                    });



                    startButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            workoutTimer.start();
                        }
                    });
                }
            }

            public int LongToInt(long value)
            {
                long l = value;
                int i = (int)l;
                return i;
            }

            public void hideKeyboard(View view) {
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                } catch(Exception ignored) {
                }
            }


        });




    }
}
