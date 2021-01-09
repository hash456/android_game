package iss.nus.androidgame;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridLayout;
import android.widget.TextView;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import android.os.CountDownTimer;
import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    private int numberOfElements;

    private MemoryButton[] buttons;

    // List of button position and images
    private int[] buttonGraphicLocations;
    // Image ID
    private ArrayList<String> buttonGraphics;

    // Reference to compare two buttons
    private MemoryButton selectedButton1;
    private MemoryButton selectedButton2;

    // Prevent the app from crashing by deferring the task if app is busy
    private boolean isBusy = false;

    private Integer numberOfMatches = 0;
    private TextView numMatches;

    private Integer numberOfTries = 0;
    private TextView numTries;

    private AlertDialog.Builder dlg;

    private CountDownTimer myStopwatch;
    private long time;
    private File mTargetFile;

    private Date currentClick;
    private Date lastClick;

    private SoundEffect sound;
    Animation animation;

    private boolean timeOut = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_AppCompat_Light_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        animation= AnimationUtils.loadAnimation(GameActivity.this,R.anim.bounce);
        sound=new SoundEffect(this);

        // Create new file to record highscore
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

        Alarm.halfTime(getApplicationContext());
        Alarm.tenSec(getApplicationContext());

        //Number of matches
        numMatches = findViewById(R.id.numMatches);
        if (numMatches != null)
        {
            numMatches.setText(numberOfMatches.toString() + " / 6");
        }

        numTries = findViewById(R.id.numTries);
        numTries.setText(numberOfTries.toString());

        //Countdown timer time's up alert
        dlg = new AlertDialog.Builder(this)
                .setPositiveButton("Play Again",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dlg, int which) {
                                finish();
                                startActivity(getIntent());
                            }
                        })
                .setNegativeButton("Return to Title",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(GameActivity.this, LandingActivity.class);
                                startActivity(intent);
                            }
                        });

        TextView Countdown = findViewById(R.id.countdown);

        //Countdown Timer
        myStopwatch = new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished <= 11000)
                {
                    if (Countdown != null)
                    {
                        Countdown.setText("Time Left: " + millisUntilFinished / 1000);
                        Countdown.setTextColor(Color.parseColor("#FA0C0C"));
                    }
                }
                else {
                    if (Countdown != null) {
                        Countdown.setText("Time Left: " + millisUntilFinished / 1000);

                    }
                }
                time = millisUntilFinished;
            }

            public void onFinish() {
                Alarm.hurryUp_Stop(getApplicationContext());
                timeOut = true;
                String title = "Time's up!";
                String msg = "You took " + numberOfTries.toString() +  " tries to get " + numberOfMatches.toString() + " number of matches.";
                dlg.setMessage(msg).setTitle(title).setIcon(android.R.drawable.ic_dialog_alert).show();
            }

        }.start();

        // GridLayout logic
        GridLayout gridLayout = (GridLayout)findViewById(R.id.gridLayout);

        int numColumns = gridLayout.getColumnCount();
        int numRows = gridLayout.getRowCount();

        numberOfElements = numColumns * numRows;

        buttons = new MemoryButton[numberOfElements];

        // Load the images
        buttonGraphics = getIntent().getStringArrayListExtra("images");

        // Shuffle the images and button position
        buttonGraphicLocations = new int[numberOfElements];
        shuffleButtonGraphics();

        // Add the image to GridLayout and add on click listener
        for(int r = 0; r < numRows; r++) {
            for(int c = 0; c < numColumns; c++) {
                int index = buttonGraphicLocations[r * numColumns + c];

                String imageName = buttonGraphics.get(index);
                File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                File file = new File(dir, imageName);
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                Drawable d = new BitmapDrawable(getResources(), bitmap);

                MemoryButton tempButton = new MemoryButton(this, r, c, d);
                tempButton.setTag(index);
                tempButton.setId(View.generateViewId());
                tempButton.setOnClickListener(this);
                buttons[r * numColumns + c] = tempButton;
                gridLayout.addView(tempButton);
            }
        }
    }

    // Randomize images
    protected void shuffleButtonGraphics() {
        Random rand = new Random();

        for(int i = 0; i < numberOfElements; i++) {
            buttonGraphicLocations[i] = i % (numberOfElements / 2);
        }

        for(int i = 0; i < numberOfElements; i++) {
            int temp = buttonGraphicLocations[i];
            int swapIndex = rand.nextInt(numberOfElements);
            buttonGraphicLocations[i] = buttonGraphicLocations[swapIndex];
            buttonGraphicLocations[swapIndex] = temp;
        }
    }

    // Check for game over
    protected boolean isGameOver() {
        for(int i = 0; i < buttons.length; i++) {
            if(!buttons[i].isMatched)
                return false;
        }
        myStopwatch.cancel();
        return true;
    }

    @Override
    public void onClick(View v) {
        animation= AnimationUtils.loadAnimation(GameActivity.this,R.anim.bounce);
        if(isBusy || timeOut)
            return;

        Alarm.hurryUp(getApplicationContext());

        MemoryButton button = (MemoryButton) v;
        button.startAnimation(animation);

        if(button.isMatched)
            return;

        if(selectedButton1 == null) {
            selectedButton1 = button;
            sound.playClickSound();
            selectedButton1.flip();
            return;
        }

        // If user click the same button repeatedly, we ignore them
        if(selectedButton1.getId() == button.getId()) {
            Alarm.hurryUp_Stop(getApplicationContext());
            return;
        } else {
            numberOfTries++;
            numTries.setText(numberOfTries.toString());
        }

        if(selectedButton1.getTag() == button.getTag()) {
            Alarm.hurryUp_Stop(getApplicationContext());

            button.flip();
            sound.playMatchedSound();
            button.setMatched(true);
            selectedButton1.setMatched(true);

            selectedButton1.setEnabled(false);
            button.setEnabled(false);

            selectedButton1 = null;

            numberOfMatches++;
            numMatches.setText(numberOfMatches.toString() + " / 6");

            if(isGameOver()) {
                Alarm.hurryUp_Stop(getApplicationContext());
                Alarm.halfTime_stop(getApplicationContext());
                Alarm.tenSec_stop(getApplicationContext());
                String title = "You Won!";

                Integer myTime = Math.round((60000 - time) / 1000);
                String msg = "Congrats, you took " + myTime.toString() + "s and " + numberOfTries.toString() + " tries.";
                dlg.setMessage(msg).setTitle(title).show();

                // Save time to local storage
                try {
                    File parent = mTargetFile.getParentFile();
                    if (!parent.exists()) {
                        throw new IllegalStateException("File don't exist");
                    }
//                    FileOutputStream fos = new FileOutputStream(mTargetFile, true);
//                    String fileContent =  myTime.toString();
//                    fos.write(fileContent.getBytes());
//                    fos.close();
                    FileWriter fileWriter = new FileWriter(mTargetFile, true);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                    bufferedWriter.write(myTime.toString() + "\n");
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return;
        } else {
            sound.playFailedSound();
            Alarm.hurryUp_Stop(getApplicationContext());
            selectedButton2 = button;
            selectedButton2.flip();
            isBusy = true;

            // If user select both wrong buttons, delay input and flip back both buttons first before accepting new input
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    selectedButton2.flip();
                    selectedButton1.flip();
                    selectedButton1 = null;
                    selectedButton2 = null;
                    isBusy = false;
                    Alarm.hurryUp(getApplicationContext());
                }
            }, 500);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Alarm.tenSec_stop(getApplicationContext());
        Alarm.halfTime_stop(getApplicationContext());
        Alarm.hurryUp_Stop(getApplicationContext());
    }
}