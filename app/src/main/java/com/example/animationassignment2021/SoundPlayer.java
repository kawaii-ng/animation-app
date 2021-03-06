package com.example.animationassignment2021;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/*
*
* SoundPlayer is to manage audio in the game
*
* */

public class SoundPlayer {

    private static SoundPool soundPool;
    private static int hitSound;
    private static int gameOverSound;

    public SoundPlayer(Context context){

        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        hitSound = soundPool.load(context, R.raw.collision, 1);
        gameOverSound = soundPool.load(context, R.raw.game_over, 1);

    }

    public void playHitSound(){
        soundPool.play(hitSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playGameOverSound(){
        soundPool.play(gameOverSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

}
