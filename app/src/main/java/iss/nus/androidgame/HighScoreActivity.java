package iss.nus.androidgame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class HighScoreActivity extends AppCompatActivity {

    private File mTargetFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_AppCompat_Light_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        TextView tv1 = findViewById(R.id.textView);
        TextView tv2 = findViewById(R.id.textView2);
        TextView tv3 = findViewById(R.id.textView3);
        TextView[] tvs = new TextView[]{tv1, tv2, tv3};

        String filePath = "SampleFolder";
        String fileName = "SampleFile.txt";
        mTargetFile = new File(getApplicationContext().getFilesDir(), filePath + "/" + fileName);
        File parent = mTargetFile.getParentFile();
        try {
            if (!parent.exists()) {
                parent.mkdirs();
                parent.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<Integer> data = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(mTargetFile);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                String timeStr = strLine.trim();
                Integer time = Integer.parseInt(timeStr) ;
                data.add(time);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.sort(data);

        int size = Math.min(data.size(), 3);

        for(int i = 0; i < size ; i++) {
            tvs[i].setText(data.get(i).toString());
        }
    }
}