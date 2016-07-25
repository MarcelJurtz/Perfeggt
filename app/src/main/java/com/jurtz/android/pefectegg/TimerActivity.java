package com.jurtz.android.pefectegg;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class TimerActivity extends AppCompatActivity {

    TextView lblTime;
    Button cmdStartTimer;
    int cookingTime;
    private Handler countdownHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        countdownHandler = new Handler();

        // Sekundenangabe für Timer aus Aufruf beziehen
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                cookingTime = 0;
            } else {
                cookingTime = extras.getInt("cookingtime_seconds");
            }
        } else {
            cookingTime = (int) savedInstanceState.getSerializable("cookingtime_seconds");
        }

        cookingTime = 15;
        Toast.makeText(getApplicationContext(), cookingTime + "", Toast.LENGTH_LONG).show();

        lblTime = (TextView) findViewById(R.id.lblTimer);
        cmdStartTimer = (Button) findViewById(R.id.cmdStartTimer);
        cmdStartTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countdownHandler.postDelayed(countdownRunnable, 0);
            }
        });

    }
    // Timer beenden wenn Zurück gedrückt wird
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            countdownHandler.removeMessages(0);
            Toast.makeText(getApplicationContext(),"REMOVE CALLBACK",Toast.LENGTH_SHORT).show();
        }
        return super.onKeyDown(keyCode,event);
    }

    private Runnable countdownRunnable = new Runnable() {
        @Override
        public void run() {
            if (cookingTime < -8) {
                // Timer beenden, wenn Zeit abgelaufen ist
                countdownHandler.removeMessages(0);
                Toast.makeText(getApplicationContext(), "REMOVE CALLBACK", Toast.LENGTH_SHORT).show();
            } else if(cookingTime < 0) {
                toggleTextColor(lblTime,cookingTime);
                cookingTime--;
                countdownHandler.postDelayed(this,500);
            } else {
                // Textfeld ändern und Zeit reduzieren
                updateText(cookingTime, lblTime);
                cookingTime--;
                countdownHandler.postDelayed(this, 1000);
            }
        }
    };

    // Textfeld mit neuem Wert updaten
    // Zeit in Sekunden
    public void updateText(int time, TextView lbl) {
        int seconds = time%60;
        int minutes = (time-seconds)/60;
        lbl.setText(returnTwoDigitFormat(minutes)+":"+returnTwoDigitFormat(seconds));
    }
    // Wandelt Zahlen, die < 10 sind zum Format '01' um
    private String returnTwoDigitFormat(int time) {
        String sec;
        if(time >= 10) {
            sec = time+"";
        } else {
            sec = "0"+time;
        }
        return sec;
    }

    private void toggleTextColor(TextView txt, int t) {
        if(t%2 == 0) {
            txt.setTextColor(Color.BLACK);
        } else {
            txt.setTextColor(Color.RED);
        }
    }
}
