package iss.nus.androidgame;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundEffect {
    private static SoundPool soundPool;
    private static int clickSound;
    private static int matchSound;
    private static int failSound;
    public SoundEffect(Context context)
    {
        soundPool=new SoundPool(2, AudioManager.STREAM_MUSIC,0);
        clickSound=soundPool.load(context,R.raw.done,1);
        matchSound=soundPool.load(context,R.raw.matched,1);
        failSound=soundPool.load(context,R.raw.failed,1);
    }

    public void playClickSound(){
        soundPool.play(clickSound,10.0f,10.0f,1,0,1.0f);
    }
    public void playMatchedSound(){
        soundPool.play(matchSound,10.0f,10.0f,1,0,1.0f);
    }
    public void playFailedSound(){
        soundPool.play(failSound,10.0f,10.0f,1,0,1.0f);
    }
}
