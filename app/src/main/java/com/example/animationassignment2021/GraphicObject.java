package com.example.animationassignment2021;

import android.graphics.Bitmap;

/*
*
* The property of the game object
* e.g.
* Coordinates : x position and y position
* Movement : speed
* Bitmap : the bitmap of the game object
*
* */

public class GraphicObject {

    private Bitmap bitmap;
    private Coordinates coordinates;
    private Movement movement;

    public GraphicObject(Bitmap bitmap){

        this.bitmap = bitmap;
        this.coordinates = new Coordinates(bitmap);
        this.movement = new Movement();

    }

    public Bitmap getGraphic(){ return bitmap; }

    public Coordinates getCoordinates(){ return coordinates; }

    public Movement getMovement(){
        return movement;
    }

}
