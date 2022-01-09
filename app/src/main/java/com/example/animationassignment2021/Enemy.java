package com.example.animationassignment2021;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/*
*
* Enemy is the game object that is attacking the player
* so it will reduce the health point of player once the player is colliding on them
*
* */

public class Enemy extends GraphicObject {

    public Enemy(Context context, int xPos, int yPos, int xSpeed, int ySpeed){

        super(BitmapFactory.decodeResource(
                context.getResources(),
                R.drawable.zombie_5));

        super.getCoordinates().setX(xPos);
        super.getCoordinates().setY(yPos);
        super.getMovement().setDirections((int)Math.random()*10>5?-1:1,1);
        super.getMovement().setXYSpeed(xSpeed, ySpeed);

    }

    public void draw(Canvas canvas){

        canvas.drawBitmap(
                super.getGraphic(),
                super.getCoordinates().getX(),
                super.getCoordinates().getY(),
                null);

    }

    // check whether the enemy is colliding on the player
    public boolean isCollide(Player player){

        int monsterX = this.getCoordinates().getX();
        int monsterY = this.getCoordinates().getY();
        int playerX = player.getCoordinates().getX();
        int playerY = player.getCoordinates().getY();
        int playerH, playerW;
        playerW = playerH = 190;

        if(monsterY >= (playerY - playerH/2)
                && monsterY <= (playerY + playerH/2)
                && monsterX >= (playerX - playerW/2)
                && monsterX <= (playerX + playerW/2)
        ){
            // collision
            return true;

        }else
            return false;

    }

    public void update(int width, int height){

        // updating the position of enemy
        int xPos = super.getCoordinates().getX();
        int xSpeed = super.getMovement().getXSpeed();

        if(xPos < 0){
            super.getMovement().toggleXDirection();
        }else if(xPos > width){
            super.getMovement().toggleXDirection();
        }

        if(super.getMovement().getXDirection() == Movement.X_DIRECTION_LEFT)
            xSpeed *= Movement.X_DIRECTION_LEFT;

        super.getCoordinates().setX(xPos + xSpeed);

        int yPos = super.getCoordinates().getY();
        int ySpeed = super.getMovement().getYSpeed();

        super.getCoordinates().setY(yPos + ySpeed);

    }

}
