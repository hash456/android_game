package iss.nus.androidgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_AppCompat_Light_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LandingActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button highScoreButton = findViewById(R.id.highScoreButton);
        highScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LandingActivity.this, HighScoreActivity.class);
                startActivity(intent);
            }
        });
    }
}