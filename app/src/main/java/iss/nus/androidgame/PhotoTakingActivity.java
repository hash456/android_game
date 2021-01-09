package iss.nus.androidgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class PhotoTakingActivity extends AppCompatActivity
        implements View.OnClickListener {

    protected String photoFile;
    protected final int REQ_TAKE_PHOTO_1 = 0;
    protected final int REQ_TAKE_PHOTO_2 = 1;
    protected final int REQ_TAKE_PHOTO_3 = 2;
    protected final int REQ_TAKE_PHOTO_4 = 3;
    protected final int REQ_TAKE_PHOTO_5 = 4;
    protected final int REQ_TAKE_PHOTO_6 = 5;

    String[] listHolder = {"", "", "", "", "", ""};
    ArrayList<String> photoNameList = new ArrayList<>();
    int photoCount = 1;

    Button startGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_AppCompat_Light_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_taking);

        Button iv1 = findViewById(R.id.take_photo1);
        Button iv2 = findViewById(R.id.take_photo2);
        Button iv3 = findViewById(R.id.take_photo3);
        Button iv4 = findViewById(R.id.take_photo4);
        Button iv5 = findViewById(R.id.take_photo5);
        Button iv6 = findViewById(R.id.take_photo6);

        startGame = findViewById(R.id.startGame);
        startGame.setVisibility(View.INVISIBLE);

        iv1.setOnClickListener(this);
        iv2.setOnClickListener(this);
        iv3.setOnClickListener(this);
        iv4.setOnClickListener(this);
        iv5.setOnClickListener(this);
        iv6.setOnClickListener(this);

        //

        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.addAll(photoNameList, listHolder);
                Intent intent = new Intent(PhotoTakingActivity.this, GameActivity.class);
                intent.putStringArrayListExtra("images", photoNameList);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.take_photo1:
                takePhoto(REQ_TAKE_PHOTO_1);
                break;
            case R.id.take_photo2:
                takePhoto(REQ_TAKE_PHOTO_2);
                break;
            case R.id.take_photo3:
                takePhoto(REQ_TAKE_PHOTO_3);
                break;
            case R.id.take_photo4:
                takePhoto(REQ_TAKE_PHOTO_4);
                break;
            case R.id.take_photo5:
                takePhoto(REQ_TAKE_PHOTO_5);
                break;
            case R.id.take_photo6:
                takePhoto(REQ_TAKE_PHOTO_6);
                break;
        }
    }

    protected void takePhoto(int REQ_TAKE_PHOTO) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File tmpfile = createTmpFile();
            if (tmpfile != null) {
                Uri uri = FileProvider.getUriForFile(this,
                        "iss.nus.androidgame", tmpfile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, REQ_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // listHolder -> only collect 6 photos. Will update the list every time
        // user change photo
        Bitmap bitmap = BitmapFactory.decodeFile(photoFile);
        String filename = photoFile.substring(photoFile.lastIndexOf("/") + 1);
        ImageView imgView;
        Button btn;
        switch (requestCode) {
            case REQ_TAKE_PHOTO_1:
                imgView = findViewById(R.id.imgview1);
                btn = findViewById(R.id.take_photo1);
                if (imgView != null) {
                    imgView.setImageBitmap(bitmap);
                    listHolder[REQ_TAKE_PHOTO_1] = filename;
                    btn.setText("Change Photo");
                }
                break;
            case REQ_TAKE_PHOTO_2:
                imgView = findViewById(R.id.imgview2);
                btn = findViewById(R.id.take_photo2);
                if (imgView != null) {
                    imgView.setImageBitmap(bitmap);
                    listHolder[REQ_TAKE_PHOTO_2] = filename;
                    btn.setText("Change Photo");
                }
                break;
            case REQ_TAKE_PHOTO_3:
                imgView = findViewById(R.id.imgview3);
                btn = findViewById(R.id.take_photo3);
                if (imgView != null) {
                    imgView.setImageBitmap(bitmap);
                    listHolder[REQ_TAKE_PHOTO_3] = filename;
                    btn.setText("Change Photo");
                }
                break;
            case REQ_TAKE_PHOTO_4:
                imgView = findViewById(R.id.imgview4);
                btn = findViewById(R.id.take_photo4);
                if (imgView != null) {
                    imgView.setImageBitmap(bitmap);
                    listHolder[REQ_TAKE_PHOTO_4] = filename;
                    btn.setText("Change Photo");
                }
                break;
            case REQ_TAKE_PHOTO_5:
                imgView = findViewById(R.id.imgview5);
                btn = findViewById(R.id.take_photo5);
                if (imgView != null) {
                    imgView.setImageBitmap(bitmap);
                    listHolder[REQ_TAKE_PHOTO_5] = filename;
                    btn.setText("Change Photo");
                }
                break;
            case REQ_TAKE_PHOTO_6:
                imgView = findViewById(R.id.imgview6);
                btn = findViewById(R.id.take_photo6);
                if (imgView != null) {
                    imgView.setImageBitmap(bitmap);
                    listHolder[REQ_TAKE_PHOTO_6] = filename;
                    btn.setText("Change Photo");
                }
                break;
        }

        if (!Arrays.stream(listHolder).anyMatch(""::equals)) {
            startGame.setVisibility(View.VISIBLE);
        }

    }

    protected File createTmpFile() {
        File tmpfile = null;

        try {
            File path = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            tmpfile = File.createTempFile("takephoto_", ".jpg", path);
            photoFile = tmpfile.getAbsolutePath();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return tmpfile;
    }
}