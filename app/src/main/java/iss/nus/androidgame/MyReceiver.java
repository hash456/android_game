package iss.nus.androidgame;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action.equals("HURRY_UP")) {
            Toast.makeText(context, "Hurry up! Time is counting down!",
                    Toast.LENGTH_SHORT).show();
        }

        else if (action.equals("HALF_TIME")) {
            Toast.makeText(context, "You have 30s left!",
                    Toast.LENGTH_SHORT).show();
        }

        else if (action.equals("TEN_SEC")) {
            Toast.makeText(context, "10s left!!! You can do it!",
                    Toast.LENGTH_SHORT).show();
        }
    }
}