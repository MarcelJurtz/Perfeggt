package com.jurtz.android.pefectegg;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class TimerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        int cookingTime;
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

        Toast.makeText(getApplicationContext(),cookingTime+"",Toast.LENGTH_LONG).show();
    }
}
