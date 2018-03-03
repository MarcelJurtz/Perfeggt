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

        cmdCalculateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(getApplicationContext(), TimerActivity.class);
                    intent.putExtra(SettingsHelper.EXTRA_COOKINGTIME, CalculateCookingTime());
                    getApplicationContext().startActivity(intent);
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), getString(R.string.err_invalid_input), Toast.LENGTH_SHORT).show();
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

        // T Start
        if (rbTemperatureFridge.isChecked()) {
            temperature = SettingsHelper.TEMP_DEG_FRIDGE;
        } else if (rbTemperatureRoom.isChecked()) {
            temperature = SettingsHelper.TEMP_DEG_ROOM;
        } else {
            try {
                temperature = Double.parseDouble(txtTemperature.getText().toString());
            } catch (Exception ex) {
                throw ex;
            }
        }

        // Höhe
        try {
            boilingPoint = CookingTimeCalculator.calcBoilingPoint(Double.parseDouble(txtHeightAboveSea.getText().toString()));
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
            tInside = SettingsHelper.TEMP_DEG_HARD;
        } else if (rbConsistencyMedium.isChecked()) {
            tInside = SettingsHelper.TEMP_DEG_MEDIUM;
        } else {
            tInside = SettingsHelper.TEMP_DEG_SOFT;
        }

        double cookingTimeMinutes = CookingTimeCalculator.calcCookingTimeMinutes(weight, boilingPoint, temperature, tInside);
        int cookingTimeSeconds = (int) Math.round(cookingTimeMinutes * 60);

        return cookingTimeSeconds;
    }
}
