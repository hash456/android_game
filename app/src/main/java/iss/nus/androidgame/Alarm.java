package iss.nus.androidgame;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Date;

public class Alarm {

    public static void halfTime(Context context) {
        Date when = new Date(System.currentTimeMillis());

        Intent intent = new Intent(context, MyReceiver.class);
        intent.setAction("HALF_TIME");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.setExact(AlarmManager.RTC_WAKEUP, when.getTime() + 29000, pendingIntent);
    }

    public static void tenSec(Context context) {
        Date when = new Date(System.currentTimeMillis());

        Intent intent = new Intent(context, MyReceiver.class);
        intent.setAction("TEN_SEC");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.setExact(AlarmManager.RTC_WAKEUP, when.getTime() + 49000, pendingIntent);
    }

    public static void hurryUp(Context context) {
        Date when = new Date(System.currentTimeMillis());
        Intent intent = new Intent(context, MyReceiver.class);
        intent.setAction("HURRY_UP");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.setExact(AlarmManager.RTC_WAKEUP, when.getTime() + 9000, pendingIntent);
    }

    public static void hurryUp_Stop(Context context) {
        Date when = new Date(System.currentTimeMillis());
        Intent intent = new Intent(context, MyReceiver.class);
        intent.setAction("HURRY_UP");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pendingIntent);
    }

    public static void halfTime_stop(Context context) {
        Date when = new Date(System.currentTimeMillis());
        Intent intent= new Intent(context, MyReceiver.class);
        intent.setAction("HALF_TIME");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pendingIntent);
    }

    public static void tenSec_stop(Context context) {
        Date when = new Date(System.currentTimeMillis());
        Intent intent = new Intent(context, MyReceiver.class);
        intent.setAction("TEN_SEC");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pendingIntent);
    }
}
