package iss.nus.androidgame;

import android.content.Context;

import androidx.annotation.NonNull;

public class MemoryImageView extends androidx.appcompat.widget.AppCompatImageView {

    private boolean isSelected;

    public MemoryImageView(@NonNull Context context) {
        super(context);
        setImageDrawable(null);
        isSelected = false;
    }

    // For some reason, this toggle do not work when we are at position 0
    public void toggle() {
        if (isSelected) {
            isSelected = false;
            setImageDrawable(null);
        } else {
            isSelected = true;
            setImageResource(R.drawable.checkmark);
        }
    }

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean s) {
        isSelected = s;
    }
}
