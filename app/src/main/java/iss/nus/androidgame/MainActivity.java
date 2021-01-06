package iss.nus.androidgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
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

        // Render the images
        Integer[] imagesId = {
                R.drawable.afraid,
                R.drawable.full,
                R.drawable.hug,
                R.drawable.laugh,
                R.drawable.no_way,
                R.drawable.peep,
                R.drawable.snore,
                R.drawable.stop,
                R.drawable.tired,
                R.drawable.what,
                R.drawable.afraid,
                R.drawable.full,
                R.drawable.hug,
                R.drawable.laugh,
                R.drawable.no_way,
                R.drawable.peep,
                R.drawable.snore,
                R.drawable.stop,
                R.drawable.tired,
                R.drawable.what,
        };

        //  Include this whole part after we have get the images
        GridView gridview = (GridView) findViewById(R.id.gridView);

        gridview.setAdapter(new ImageAdapter(this, imagesId));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id){
                // Get the R.drawable id of the selected picture
                Integer selectedImageId = (Integer) parent.getItemAtPosition(position);

                ImageAdapter adapter = (ImageAdapter) parent.getAdapter();

                // Get the button that we have clicked
                MemoryImageView iv = adapter.getSelectedImageView(position);
                iv.toggle();

                Integer pos = (Integer) position;
                Toast.makeText(getApplicationContext(), pos.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}