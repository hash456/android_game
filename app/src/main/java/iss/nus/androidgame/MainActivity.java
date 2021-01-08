package iss.nus.androidgame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    // https://www.google.com/search?tbm=isch&q=findSomeImage

    Set<String> selectedImageId = new HashSet<>();

    ArrayList<Drawable> images = new ArrayList<>();

    AlertDialog.Builder dlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_AppCompat_Light_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // For progress bar
        ProgressBar progressBar = findViewById(R.id.pgProgressBar);
        TextView progressText = findViewById(R.id.tvProgressPercentage);
        Handler handler = new Handler();
        final int[] percentDone = {0};

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

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                stopService(new Intent(MainActivity.this, JsoupCrawler.class));
                                images = getDownloadImages();
                                imagesToGridView();
                            }
                        }, 10000);

                        new Thread(new Runnable() {
                            public void run() {
                                while (percentDone[0] < 100) {
                                    percentDone[0] += 5;

                                    handler.post(new Runnable() {
                                        public void run() {
                                            progressBar.setProgress(percentDone[0]);

                                            progressText.setText(percentDone[0] + " of 100% done");
                                        }
                                    });

                                    // This part is for testing only
                                    try {
                                        Thread.sleep(200);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                percentDone[0] = 0;
                            }
                        }).start();

                    } else {
                        Toast.makeText(getApplicationContext(), "URL cannot be empty" , Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        GridView gridview = (GridView) findViewById(R.id.gridView);

        gridview.setAdapter(new ImageAdapter(this));

        dlg = new AlertDialog.Builder(this)
                .setTitle("Start Game?")
                .setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dlg, int which) {
                        Intent intent = new Intent(MainActivity.this, GameActivity.class);
                        ArrayList<String> imageList = new ArrayList<>(selectedImageId);
                        intent.putStringArrayListExtra("images", imageList);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id){
                MemoryImageView iv = v.findViewById(position + 1);

                iv.toggle();

                // Image Id based
                if(iv.getSelected() && selectedImageId.size() < 6) {
                    selectedImageId.add((String) iv.getTag());
                } else if(iv.getSelected() && selectedImageId.size() >= 6) {
                    iv.toggle();
                    Toast.makeText(getApplicationContext(), "6 image chosen", Toast.LENGTH_SHORT).show();
                } else {
                    selectedImageId.remove((String) iv.getTag());
                }

                if(selectedImageId.size() == 6) {
                    dlg.show();
                }
            }
        });

    }

    protected ArrayList<Drawable> getDownloadImages() {
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        ArrayList<Drawable> myList = new ArrayList<>();
        for(Integer i = 0; i < 20; i++) {
            File file = new File(dir, "pic" + i.toString());
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            Drawable d = new BitmapDrawable(getResources(), bitmap);
            myList.add(d);
        }
        return myList;
    }

    protected void imagesToGridView() {
        for(Integer i = 0; i < 20; i++) {
            MemoryImageView iv = findViewById(i + 1);
            iv.setBackgroundDrawable(images.get(i));
            iv.setTag("pic" + i.toString());
        }
    }
}