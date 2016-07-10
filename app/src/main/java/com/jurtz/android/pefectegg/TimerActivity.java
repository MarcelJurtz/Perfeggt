package com.jurtz.android.pefectegg;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class TimerActivity extends AppCompatActivity {

    int timeInSec;
    TextView lblTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        final int cookingTime;

        lblTime = (TextView)findViewById(R.id.lblTimer);

        if(savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                cookingTime = 0;
            } else {
                cookingTime = extras.getInt("cookingtime_seconds");
            }
        } else {
            cookingTime = (int)savedInstanceState.getSerializable("cookingtime_seconds");

        }
        timeInSec = cookingTime;
        // Timer: Parameter 1: Dauer in MS, Parameter 2: Intervall in MS
        new CountDownTimer(1000*timeInSec,1000) {
            public void onTick(long millisUntilFinish) {
                timeInSec--;
                updateText(timeInSec,lblTime);
            }
            public void onFinish() {
                lblTime.setText("00:00");
            }
        }.start();
        Toast.makeText(getApplicationContext(),cookingTime+"",Toast.LENGTH_LONG).show();
    }
    public void updateText(int time, TextView lbl) {
        int seconds = time%60;
        int minutes = (time-seconds)/60;
        lbl.setText(minutes+":"+seconds);
    }
}
