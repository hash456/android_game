package iss.nus.androidgame;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    private int numberOfElements;

    private MemoryButton[] buttons;

    // List of button position and images
    private int[] buttonGraphicLocations;
    // Image ID
    private ArrayList<Integer> buttonGraphics;

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

    private SoundEffect sound;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_AppCompat_Light_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        animation= AnimationUtils.loadAnimation(GameActivity.this,R.anim.bounce);
        sound=new SoundEffect(this);

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
            }

            public void onFinish() {
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
        buttonGraphics = getIntent().getIntegerArrayListExtra("images");

        // Shuffle the images and button position
        buttonGraphicLocations = new int[numberOfElements];
        shuffleButtonGraphics();

        // Add the image to GridLayout and add on click listener
        for(int r = 0; r < numRows; r++) {
            for(int c = 0; c < numColumns; c++) {
                int index = buttonGraphicLocations[r * numColumns + c];
                MemoryButton tempButton = new MemoryButton(this, r, c, buttonGraphics.get(index));
                tempButton.setId(View.generateViewId());
                tempButton.setOnClickListener(this);
                tempButton.setWidth(200);
                tempButton.setHeight(200);
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
        if(isBusy)
            return;

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
        if(selectedButton1.getId() == button.getId())
            return;
        else {
            numberOfTries++;
            numTries.setText(numberOfTries.toString());
        }

        if(selectedButton1.getFrontImageDrawableId() == button.getFrontImageDrawableId()) {
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
                String title = "You Won!";
                String msg = "Congrats, you beat the game in " + numberOfTries.toString() + " tries.";
                dlg.setMessage(msg).setTitle(title).show();
            }

            return;
        } else {
            sound.playFailedSound();
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
                }
            }, 500);
        }

    }
}