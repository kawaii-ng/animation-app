package com.example.animationassignment2021;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

/*
 *
 * Player Object that is controlled by a sensor
 * its health point will be reduce when colliding on enemy
 *
 * */

public class Player extends GraphicObject {

    private int health;
    private int state;
    // state : 0 -> not moving, 1 -> start moving, 2 -> is moving
    private long updateTime;

    public Player(Context context, int xPos, int yPos){

        super(BitmapFactory.decodeResource(
                context.getResources(),
                R.drawable.player));

        super.getCoordinates().setX(xPos);
        super.getCoordinates().setY(yPos);
        super.getMovement().setXYSpeed(10, 10);

        state = 0;

    }

    public void draw(Canvas canvas){

        int w = super.getGraphic().getWidth();
        int h = super.getGraphic().getHeight();
        int x = super.getCoordinates().getX();
        int y = super.getCoordinates().getY();

        canvas.drawBitmap(
                super.getGraphic(),
                new Rect(state*190, 0, (state+1)*190, 190),
                new Rect(x, y, 190+x, y+190),
                null);

    }

    public void setHealth(int h){
        health = h;
    }

    public int getHealth(){
        return health;
    }

    public void update(int x, int width){

        super.getMovement().setXYSpeed(x, 0);
        int xPos = super.getCoordinates().getX();
        int xSpeed = super.getMovement().getXSpeed();

        // update the state of the player
        switch (state){
            case 0:
                if(xSpeed != 0)
                    state = 1;
                updateTime = System.currentTimeMillis();
                break;
            case 1:
                if(System.currentTimeMillis()-updateTime > 100){
                    state = 2;
                    updateTime = System.currentTimeMillis();
                }
                break;
            case 2:
                if(xSpeed == 0){
                    state = 0;
                }else if(System.currentTimeMillis()-updateTime > 100){
                    state = 1;
                    updateTime = System.currentTimeMillis();
                }
                break;
            default:
                break;
        }

        if(!(xPos < 0 && xPos > width))
            super.getCoordinates().setX(xPos + xSpeed);
        if(xPos < 0)
            super.getCoordinates().setX(0);
        if((xPos + 190) > width)
            super.getCoordinates().setX(width-190);

    }

}
