package iss.nus.androidgame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.GridLayout;

import androidx.appcompat.widget.AppCompatDrawableManager;

public class MemoryButton extends androidx.appcompat.widget.AppCompatButton {
    protected int row;
    protected int col;
    protected int frontImageDrawableId;

    protected boolean isFlipped = false;
    protected boolean isMatched = false;

    protected Drawable front;
    protected Drawable back;

    @SuppressLint("RestrictedApi")
    public MemoryButton(Context context, int row, int col, int frontImageDrawableId) {
        super(context);
        this.row = row;
        this.col = col;
        this.frontImageDrawableId = frontImageDrawableId;

        // Load images
        front = AppCompatDrawableManager.get().getDrawable(context, frontImageDrawableId);
        back = AppCompatDrawableManager.get().getDrawable(context, R.drawable.question_mark);

        setBackground(back);

        // Set Grid Layout parameters
        GridLayout.LayoutParams tempParams = new GridLayout.LayoutParams(GridLayout.spec(row), GridLayout.spec(col));
        tempParams.width = (int) getResources().getDisplayMetrics().density * 150;
        tempParams.height = (int) getResources().getDisplayMetrics().density * 150;
        setLayoutParams(tempParams);
    }

    public boolean isMatched() {
        return isMatched;
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
    }

    public int getFrontImageDrawableId() {
        return frontImageDrawableId;
    }

    public void setFrontImageDrawableId(int frontImageDrawableId) {
        this.frontImageDrawableId = frontImageDrawableId;
    }

    public void flip() {
        if(isMatched) {
            return;
        }

        if(isFlipped) {
            setBackground(back);
            isFlipped = false;
        } else {
            setBackground(front);
            isFlipped = true;
        }
    }
}


