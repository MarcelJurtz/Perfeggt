package com.jurtz.android.pefectegg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.logging.Level;

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

    // gewünschte Innentemperatur des Eis
    private final int TEMPERATURE_SOFT = 62;
    private final int TEMPERATURE_MEDIUM = 72;
    private final int TEMPERATURE_HARD = 82;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        rbTemperatureRoom = (RadioButton)findViewById(R.id.rbTemperatureRoom);
        rbTemperatureFridge = (RadioButton)findViewById(R.id.rbTemperatureFridge);
        rbTemperatureCustom = (RadioButton)findViewById(R.id.rbTemperatureOther);

        rbConsistencySoft = (RadioButton)findViewById(R.id.rbConsistencySoft);
        rbConsistencyMedium = (RadioButton)findViewById(R.id.rbConsistencyMedium);
        rbConsistencyHard = (RadioButton)findViewById(R.id.rbConsistencyHard);

        txtWeight = (TextView)findViewById(R.id.txtWeight);
        txtHeightAboveSea = (TextView)findViewById(R.id.txtHeightAboveSea);
        txtTemperature = (TextView)findViewById(R.id.txtTemperature);

        Button cmdCalculateTime = (Button)findViewById(R.id.cmdCalculateTime);
        final Intent timerIntent = new Intent(this, TimerActivity.class);

        cmdCalculateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kochzeit in Minuten
                double temperature = 0;
                double boilingPoint = 0;
                double weight = 0;
                int tInside = 0;
                boolean entriesOk = true;

                // T Start
                if(rbTemperatureFridge.isChecked()) {
                    temperature = TEMPERATURE_FRIDGE;
                } else if(rbTemperatureRoom.isChecked()) {
                    temperature = TEMPERATURE_ROOM;
                } else {
                    try {
                        temperature = Double.parseDouble(txtTemperature.getText().toString());
                    } catch(Exception ex) {
                        entriesOk = false;
                    }
                }

                // Höhe
                try {
                    boilingPoint = getBoilingPoint(Double.parseDouble(txtHeightAboveSea.getText().toString()));
                } catch(Exception ex) {
                    entriesOk = false;
                }

                // Gewicht
                try {
                    weight = Double.parseDouble(txtWeight.getText().toString());
                } catch(Exception ex) {
                    entriesOk = false;
                }

                // T Ziel
                if(rbConsistencyHard.isChecked()) {
                    tInside = TEMPERATURE_HARD;
                } else if(rbConsistencyMedium.isChecked()) {
                    tInside = TEMPERATURE_MEDIUM;
                } else {
                    tInside = TEMPERATURE_SOFT;
                }

                // ------------------ KOCHZEIT ------------------
                if(entriesOk) {
                    double cookingTime = getCookingTime(weight, temperature, boilingPoint, tInside);
                    int cookingTimeSeconds = (int)Math.round(cookingTime * 60);
                    timerIntent.putExtra("cookingtime_seconds",cookingTimeSeconds);
                    startActivity(timerIntent);
                } else {
                    Toast.makeText(getApplicationContext(),"Ungültige Eingaben!",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



    // Ermittlung perfekter Kochlänge
    private double getCookingTime(double weight, double t_start, double t_water, double t_inside) {
        double cookingTime = 0.465 * Math.pow(weight,2.0/3.0) * Math.log(0.76*((t_water-t_start)/(t_water-t_inside)));
        return cookingTime;
    }

    // Ermittlung des Siedepunkts kochenden Wassers nach Höhe über NN
    private double getBoilingPoint(double heightAboveSea) {
        double temp = 100;
        double desc = heightAboveSea/285;
        temp -= desc;
        return temp;
    }
}
