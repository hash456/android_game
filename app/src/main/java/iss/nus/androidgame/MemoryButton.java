package iss.nus.androidgame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.GridLayout;

import androidx.appcompat.widget.AppCompatDrawableManager;

public class MemoryButton extends androidx.appcompat.widget.AppCompatImageButton {
    protected int row;
    protected int col;
    protected int frontImageDrawableId;

    protected boolean isFlipped = false;
    protected boolean isMatched = false;

    protected Drawable front;
    protected Drawable back;

    protected GridLayout.LayoutParams tempParams;

    @SuppressLint("RestrictedApi")
    public MemoryButton(Context context, int row, int col, int frontImageDrawableId) {
        super(context);
        this.row = row;
        this.col = col;
        this.frontImageDrawableId = frontImageDrawableId;

        // Load images
        front = AppCompatDrawableManager.get().getDrawable(context, frontImageDrawableId);
        back = AppCompatDrawableManager.get().getDrawable(context, R.drawable.qnmark);

        setBackground(back);

        // Set Grid Layout parameters
        tempParams = new GridLayout.LayoutParams(GridLayout.spec(row), GridLayout.spec(col));

        // Set the size of each button
        tempParams.width = (int) getResources().getDisplayMetrics().density * 150;
        tempParams.height = (int) getResources().getDisplayMetrics().density * 150;
        setLayoutParams(tempParams);
    }

    public boolean isMatched() {
        return isMatched;
    }

    public void setWidth(int n) {
        tempParams.width = (int) getResources().getDisplayMetrics().density * n;
    }

    public void setHeight(int n) {
        tempParams.height = (int) getResources().getDisplayMetrics().density * n;
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


