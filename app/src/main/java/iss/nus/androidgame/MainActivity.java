package iss.nus.androidgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    // https://www.google.com/search?tbm=isch&q=findSomeImage

    Set<Integer> selectedImageId = new HashSet<>();

    ArrayList<String> imageUrlList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_AppCompat_Light_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hardcode imageUrl here, remove for actual demo
        EditText imageUrl = findViewById(R.id.imageUrl);
        imageUrl.setText("https://stocksnap.io");

        Button fetchButton = findViewById(R.id.fetchButton);
        fetchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText imageUrl = findViewById(R.id.imageUrl);
                if(imageUrl != null) {
                    String urlToFetch = imageUrl.getText().toString().trim();
                    if(!urlToFetch.isEmpty()) {

                        System.out.println("from main url not empty");

                        Intent intent = new Intent (MainActivity.this, JsoupCrawler.class);
                        intent.setAction("download");
                        intent.putExtra("URL",urlToFetch);
                        startService(intent);


                        Toast.makeText(getApplicationContext(), urlToFetch , Toast.LENGTH_SHORT).show();
                        // TODO: Use JsoupCrawler to get image url and store it in imageUrlList, then we use the link to download the images
                        // Android does not allow network operation like connecting to a url on main thread

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                imagesToImageViews();
                                stopService(new Intent(MainActivity.this, JsoupCrawler.class));

                            }
                        }, 10000);
                    } else {
                        Toast.makeText(getApplicationContext(), "URL cannot be empty" , Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        Button startGameButton = findViewById(R.id.startGameButton);
        startGameButton.setVisibility(View.GONE);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedImageId.size() == 6) {
                    Intent intent = new Intent(MainActivity.this, GameActivity.class);
                    // Need to convert to ArrayList first as intent cannot send Sets
                    ArrayList<Integer> imageList = new ArrayList<>(selectedImageId);
                    intent.putIntegerArrayListExtra("images", imageList);
                    startActivity(intent);
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
                R.drawable.one,
                R.drawable.two,
                R.drawable.three,
                R.drawable.four,
                R.drawable.five,
                R.drawable.six,
                R.drawable.seven,
                R.drawable.eight,
                R.drawable.nine,
                R.drawable.ten,
        };

        //  Include this whole part after we have get the images
        GridView gridview = (GridView) findViewById(R.id.gridView);

        gridview.setAdapter(new ImageAdapter(this, imagesId));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id){
                // Get the R.drawable id of the selected picture
                Integer imageId = (Integer) parent.getItemAtPosition(position);

                MemoryImageView iv = v.findViewById(position + 1);

                iv.toggle();

                // Image Id based
                if(iv.getSelected() && selectedImageId.size() < 6) {
                    selectedImageId.add(imageId);
                } else if(iv.getSelected() && selectedImageId.size() >= 6) {
                    iv.toggle();
                    Toast.makeText(getApplicationContext(), "6 image chosen", Toast.LENGTH_SHORT).show();
                } else {
                    selectedImageId.remove(imageId);
                }

                if(selectedImageId.size() == 6) {
                    startGameButton.setVisibility(View.VISIBLE);
                } else {
                    startGameButton.setVisibility(View.GONE);
                }
            }
        });

    }

    protected void imagesToImageViews(){
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = new File(dir, "pic5");
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

        if (bitmap != null)
        {
            int id = getResources().getIdentifier("image1","id",getPackageName());
            ImageView im = findViewById(id);
            Drawable d = new BitmapDrawable(getResources(), bitmap);
            im.setImageDrawable(d);
        }
    }
}