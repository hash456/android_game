package iss.nus.androidgame;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    // https://www.google.com/search?tbm=isch&q=findSomeImage

    Set<String> selectedImageId = new HashSet<>();

    AlertDialog.Builder dlg;

    ProgressBar progressBar;
    TextView progressText;
    Integer progress;

    // Don't allow game to start if user don't download image from URL
    private boolean allowStartGame = false;

    protected BroadcastReceiver progressBarReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action == null)
                return;

            if(action.equals("DOWNLOAD_START")) {
                // Show progress bar
                progressBar.setVisibility(View.VISIBLE);
                progressText.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);
            } else if(action.equals("DOWNLOAD_ONGOING")) {
                progress = intent.getIntExtra("Download Progress", 0);
                Integer pros = progress * 5;

                // Advance progress bar
                progressBar.setProgress(pros);
                progressText.setText(pros.toString() + "% of 100% done");

                // Replace placeholder image with downloaded image
                MemoryImageView iv = findViewById(progress + 1);
                File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                File file = new File(dir, "pic" + progress.toString());
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                Drawable d = new BitmapDrawable(getResources(), bitmap);
                iv.setBackgroundDrawable(d);
            } else if(action.equals("DOWNLOAD_COMPLETE")) {
                // Fillup progress bar
                progressBar.setProgress(100);
                progressText.setText("100% of 100% done");
                allowStartGame = true;
                stopService(new Intent(MainActivity.this, JsoupCrawler.class));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_AppCompat_Light_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // For progress bar
        progressBar = findViewById(R.id.pgProgressBar);
        progressText = findViewById(R.id.tvProgressPercentage);

        IntentFilter filter = new IntentFilter();
        filter.addAction("DOWNLOAD_START");
        filter.addAction("DOWNLOAD_ONGOING");
        filter.addAction("DOWNLOAD_COMPLETE");
        registerReceiver(progressBarReceiver, filter);

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

                        stopService(new Intent(MainActivity.this, JsoupCrawler.class));
                        startService(intent);

                        Toast.makeText(getApplicationContext(), urlToFetch , Toast.LENGTH_SHORT).show();

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
                if(allowStartGame) {
                    MemoryImageView iv = v.findViewById(position + 1);

                    iv.toggle();

                    // Image Id based
                    if (iv.getSelected() && selectedImageId.size() < 6) {
                        selectedImageId.add((String) iv.getTag());
                    } else if (iv.getSelected() && selectedImageId.size() >= 6) {
                        iv.toggle();
                        Toast.makeText(getApplicationContext(), "6 image chosen", Toast.LENGTH_SHORT).show();
                    } else {
                        selectedImageId.remove((String) iv.getTag());
                    }

                    if(selectedImageId.size() == 6) {
                        dlg.show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter URL", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}