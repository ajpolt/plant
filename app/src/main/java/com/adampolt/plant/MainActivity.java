package com.adampolt.plant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    SharedPreferences plantPrefs;

    ImageView plantView;
    TextView statusView;
    Button waterButton;
    View settingsButton;
    Button shareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        plantPrefs = getSharedPreferences("plant", MODE_PRIVATE);

        plantView = findViewById(R.id.imageView);
        statusView = findViewById(R.id.status);
        waterButton = findViewById(R.id.water);
        settingsButton = findViewById(R.id.settings);
        shareButton = findViewById(R.id.share);

        waterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waterPlant();
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "My plant is awesome!");
                shareIntent.setType("text/plain");

                startActivity(shareIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        int color = plantPrefs.getInt("color", -1);

        if(color != -1) {
            plantView.setColorFilter(color);
        }

        statusCheck();
    }

    private static final long ONE_HOUR = 1000 * 60 * 60;
    private static final long ONE_DAY = 1000 * 60 * 60 * 24;

    private void statusCheck() {
        long firstWateredTime = plantPrefs.getLong("firstWateredTime", 0);

        if(firstWateredTime == 0) {
            // we have a new plant
            statusView.setText(R.string.new_plant);
            plantView.setImageResource(R.drawable.ic_plant_bad);

            plantPrefs.edit()
                    .putLong("firstWateredTime", new Date().getTime())
                    .commit();
        } else {
            // we have an existing plant
            long lastWateredTime = plantPrefs.getLong("lastWateredTime", 0);

            long now = new Date().getTime();

            if(now - lastWateredTime > ONE_DAY) {
                // Plant looks bad
                statusView.setText(R.string.plant_needs_water);
                plantView.setImageResource(R.drawable.ic_plant_bad);
            } else if(now - lastWateredTime > ONE_HOUR) {
                // Plant looks ok
                statusView.setText(R.string.plant_kindof_needs_water);
                plantView.setImageResource(R.drawable.ic_plant_ok);
            } else {
                // Plant looks good
                statusView.setText(R.string.plant_blooming);
                plantView.setImageResource(R.drawable.ic_plant_good);
            }
        }
    }

    private void waterPlant() {
        plantPrefs.edit()
                .putLong("lastWateredTime", new Date().getTime())
                .commit();

        statusCheck();
    }
}














