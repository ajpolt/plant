package com.adampolt.plant;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button saveButton = findViewById(R.id.save);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText valueText = findViewById(R.id.colorField);

                String colorValue = valueText.getText().toString();

                try {
                    int color = Color.parseColor(colorValue);

                    SharedPreferences plantPrefs = getSharedPreferences("plant", MODE_PRIVATE);
                    plantPrefs.edit()
                            .putInt("color", color)
                            .apply();

                    finish();
                } catch (Exception e) {
                    Toast.makeText(SettingsActivity.this, "Please enter a valid color", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
