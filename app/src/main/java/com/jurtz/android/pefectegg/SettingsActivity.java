package com.jurtz.android.pefectegg;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.opengl.Visibility;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.logging.Level;

import static android.content.Context.ALARM_SERVICE;

public class SettingsActivity extends AppCompatActivity {

    RadioButton rbTemperatureFridge;
    RadioButton rbTemperatureRoom;
    RadioButton rbTemperatureCustom;

    RadioButton rbConsistencySoft;
    RadioButton rbConsistencyMedium;
    RadioButton rbConsistencyHard;

    TextView txtWeight;
    TextView txtTemperature;
    TextView txtHeightAboveSea;

    private final int TEMPERATURE_FRIDGE = 4;
    private final int TEMPERATURE_ROOM = 20;

    private final int TEMPERATURE_SOFT = 62;
    private final int TEMPERATURE_MEDIUM = 72;
    private final int TEMPERATURE_HARD = 82;

    PendingIntent pi;
    BroadcastReceiver br;
    AlarmManager am;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().hide();

        rbConsistencySoft = (RadioButton) findViewById(R.id.rbConsistencySoft);
        rbConsistencyMedium = (RadioButton) findViewById(R.id.rbConsistencyMedium);
        rbConsistencyHard = (RadioButton) findViewById(R.id.rbConsistencyHard);

        txtWeight = (TextView) findViewById(R.id.txtWeight);
        txtHeightAboveSea = (TextView) findViewById(R.id.txtHeight);
        txtTemperature = (TextView) findViewById(R.id.txtTemperature);
        txtTemperature.setVisibility(View.INVISIBLE);

        rbTemperatureRoom = (RadioButton) findViewById(R.id.rbTemperatureRoom);
        rbTemperatureFridge = (RadioButton) findViewById(R.id.rbTemperatureFridge);
        rbTemperatureCustom = (RadioButton) findViewById(R.id.rbTemperatureOther);
        rbTemperatureCustom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txtTemperature.setVisibility(View.VISIBLE);
                } else {
                    txtTemperature.setVisibility(View.INVISIBLE);
                }
            }
        });

        final Button cmdCalculateTime = (Button) findViewById(R.id.cmdCalculateTime);
        //final Intent timerIntent = new Intent(this, TimerActivity.class);

        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent i) {
                Toast.makeText(c, "Rise and Shine!", Toast.LENGTH_LONG).show();
            }
        };
        registerReceiver(br, new IntentFilter("com.jurtz.android.perfectegg"));
        pi = PendingIntent.getBroadcast(this, 0, new Intent("com.jurtz.android.perfectegg"),
                0);
        am = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);

        cmdCalculateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //timerIntent.putExtra("cookingtime_seconds",cookingTimeSeconds);
                //startActivity(timerIntent);
                try {
                    //startAlert(CalculateCookingTime());
                    Intent intent = new Intent(getApplicationContext(), TimerActivity.class);
                    intent.putExtra("cookingtime_seconds", CalculateCookingTime());
                    getApplicationContext().startActivity(intent);
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), "Ungültige Eingaben!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public int CalculateCookingTime() throws Exception {
        // Kochzeit in Minuten
        double temperature = 0;
        double boilingPoint = 0;
        double weight = 0;
        int tInside = 0;
        boolean entriesOk = true;

        // T Start
        if (rbTemperatureFridge.isChecked()) {
            temperature = TEMPERATURE_FRIDGE;
        } else if (rbTemperatureRoom.isChecked()) {
            temperature = TEMPERATURE_ROOM;
        } else {
            try {
                temperature = Double.parseDouble(txtTemperature.getText().toString());
            } catch (Exception ex) {
                throw ex;
            }
        }

        // Höhe
        try {
            boilingPoint = getBoilingPoint(Double.parseDouble(txtHeightAboveSea.getText().toString()));
        } catch (Exception ex) {
            throw ex;
        }

        // Gewicht
        try {
            if (txtWeight.getText().toString().toLowerCase().equals("s")) {
                weight = 50.0;
            } else if (txtWeight.getText().toString().toLowerCase().equals("m")) {
                weight = 58.0;
            } else if (txtWeight.getText().toString().toLowerCase().equals("l")) {
                weight = 68.0;
            } else if (txtWeight.getText().toString().toLowerCase().equals("xl")) {
                weight = 75.0;
            } else {
                weight = Double.parseDouble(txtWeight.getText().toString());
            }
        } catch (Exception ex) {
            throw ex;
        }

        // T Ziel
        if (rbConsistencyHard.isChecked()) {
            tInside = TEMPERATURE_HARD;
        } else if (rbConsistencyMedium.isChecked()) {
            tInside = TEMPERATURE_MEDIUM;
        } else {
            tInside = TEMPERATURE_SOFT;
        }

        double cookingTime = 0.465 * Math.pow(weight, 2.0 / 3.0) * Math.log(0.76 * ((boilingPoint - temperature) / (boilingPoint - tInside)));
        int cookingTimeSeconds = (int) Math.round(cookingTime * 60);

        return cookingTimeSeconds;
    }


    // Ermittlung des Siedepunkts kochenden Wassers nach Höhe über NN
    private double getBoilingPoint(double heightAboveSea) {
        double temp = 100;
        double desc = heightAboveSea / 285;
        temp -= desc;
        return temp;
    }
}
