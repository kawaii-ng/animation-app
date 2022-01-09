package com.example.animationassignment2021;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

/*
*
* HealthBar is a game object to show the health point of the player
*
* */

public class HealthBar {

    protected final int MAX_HEALTH = 10;
    private Player player;
    private int health;
    private Paint barPaint;
    private int barColor;

    public HealthBar(Context context, Player player){
        this.player = player;
        this.player.setHealth(MAX_HEALTH);
        barPaint = new Paint();
        health = player.getHealth();
        barColor = ContextCompat.getColor(context, R.color.health);
    }

    public void draw(Canvas canvas){

        health = player.getHealth();
        barPaint.setColor(Color.GRAY);
        canvas.drawRect(0, 0, canvas.getWidth(), 50, barPaint);
        barPaint.setColor(barColor);
        canvas.drawRect(0, 0, (float)health/MAX_HEALTH*canvas.getWidth(), 50, barPaint);

    }

}
