package iss.nus.androidgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // https://www.google.com/search?tbm=isch&q=findSomeImage

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button fetchButton = findViewById(R.id.fetchButton);
        fetchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText imageUrl = findViewById(R.id.imageUrl);
                if(imageUrl != null) {
                    String urlToFetch = imageUrl.getText().toString().trim();
                    if(!urlToFetch.isEmpty()) {
                        Toast.makeText(getApplicationContext(), urlToFetch , Toast.LENGTH_SHORT).show();
                        // TODO: Fetch Images here
                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter a URL" , Toast.LENGTH_SHORT).show();
                        // Temporary start the game here
                        Intent intent = new Intent(MainActivity.this, GameActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}