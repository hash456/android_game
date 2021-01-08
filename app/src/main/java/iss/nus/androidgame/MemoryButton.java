package iss.nus.androidgame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.GridLayout;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatDrawableManager;

public class MemoryButton extends androidx.appcompat.widget.AppCompatImageButton {
    protected int row;
    protected int col;
    protected Drawable frontImageDrawableId;

    protected boolean isFlipped = false;
    protected boolean isMatched = false;

    protected Drawable front;
    protected Drawable back;

    protected GridLayout.LayoutParams tempParams;

    @SuppressLint("RestrictedApi")
    public MemoryButton(Context context, int row, int col, Drawable frontImageDrawableId) {
        super(context);
        this.row = row;
        this.col = col;
        this.frontImageDrawableId = frontImageDrawableId;

        // Load images
        front = frontImageDrawableId;
        back = AppCompatDrawableManager.get().getDrawable(context, R.drawable.qnmark);

        setBackground(back);

        // Set Grid Layout parameters
//        tempParams = new GridLayout.LayoutParams(GridLayout.spec(row), GridLayout.spec(col));
//        tempParams.width = (int) getResources().getDisplayMetrics().density * 150;
//        tempParams.height = (int) getResources().getDisplayMetrics().density * 150;
//        setLayoutParams(tempParams);

        tempParams= new GridLayout.LayoutParams(
                GridLayout.spec(row,GridLayout.CENTER,1f),
                GridLayout.spec(col,GridLayout.CENTER,1f));
        tempParams.width = (int) getResources().getDisplayMetrics().density * 180;
        tempParams.height = (int) getResources().getDisplayMetrics().density * 180;
        setScaleType(ImageView.ScaleType.CENTER_CROP);
        setPadding(10, 10, 10, 10);
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


