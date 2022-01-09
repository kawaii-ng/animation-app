package com.example.animationassignment2021;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.nfc.Tag;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/*
* Panel
* 1. define all objects in the game
* 2. update objects
* 3. draw all objects
*
* */

public class Panel extends SurfaceView implements SurfaceHolder.Callback {

    private Context context;
    private GameThread thread;
    private boolean isStart;
    private long startTime = System.currentTimeMillis();
    private long currentTime = System.currentTimeMillis();
    private long score = 0;
    private long changeTime = System.currentTimeMillis();
    private boolean isLeft = false;

    // game object
    private Player player;
    private ArrayList<Enemy> enemy = new ArrayList<>();
    private HealthBar healthbar;

    // other services
    private SoundPlayer sound;
    private OrientationData orientationData;
    private Vibrator vibrator;

    public Panel(Context context){

        super(context);
        this.context = context;
        getHolder().addCallback(this);
        thread = new GameThread(getHolder(),this); // initialize the GameThread object
        setFocusable(true);
        isStart = false;

        sound = new SoundPlayer(context);
        orientationData = new OrientationData(context);
        orientationData.register();
        player = new Player(context, 500, 1500);
        healthbar = new HealthBar(context, player);
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

    }

    // draw game object on screen
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.BLACK);

        for(Enemy monster: enemy)
                monster.draw(canvas);
        
        player.draw(canvas);

        drawScore(canvas);
        if(!isStart)
            drawStartBtn(canvas);

        healthbar.draw(canvas);

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {

        thread.setRunning(true);
        thread.start();

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        boolean retry = true;
        thread.setRunning(false);
        while(retry){
            try{
                thread.join();
                retry = false;
            }catch(InterruptedException e){
                // keep trying here
            }
        }
    }

    // handle the touch screen event
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        synchronized (thread.getSurfaceHolder()){

            int touchX = (int)event.getX();
            int touchY = (int)event.getY();

            if(event.getAction() == MotionEvent.ACTION_DOWN){

                if(!isStart){
                    reset();
                    isStart = true;
                }
                return true;
            }

        }

        return false;

    }

    public void drawScore(Canvas canvas){

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(50);
        canvas.drawText("Score: " + score, 100, 150, paint);

    }

    public void drawStartBtn (Canvas canvas){

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(200);

        if(score != 0){
            canvas.drawText("Try Again", 150, 1000, paint);
            paint.setTextSize(100);
            canvas.drawText("Score: " + score, 300, 800, paint);
        }else {
            canvas.drawText("Start", 350, 1000, paint);
        }

    }

    // reset the game
    public void reset() {

        orientationData.register();
        score = 0;
        enemy.clear();
        player.getCoordinates().setX(500);
        player.getCoordinates().setY(1500);
        player.setHealth(healthbar.MAX_HEALTH);
        startTime = System.currentTimeMillis();

    }

    // method to detect the collision of all objects
    public void checkCollision(){

        if(isStart){

            for(int i = 0; i < enemy.size(); i++){

                // check whether specific enemy is colliding the player
                if(enemy.get(i).isCollide(player)){

                    sound.playHitSound();
                    vibrator.vibrate(new long[]{100, 200, 100, 200}, 0);
                    player.setHealth(player.getHealth() - 1); // reduce the health of player
                    enemy.remove(i); // remove that enemy

                }

                // if player health reaches 0, game over
                if(player.getHealth() == 0) {

                    sound.playGameOverSound(); // play sound
                    isStart = false;
                    orientationData.unregister();
                    break;

                }
                vibrator.cancel();

            }

        }

    }

    // method that generate some monster
    public void generateMonster() {

        if((int)Math.floor(Math.random()*5000) < 200) {

            Enemy monster = new Enemy(
                    this.context,
                    (int)Math.floor(Math.random()*getWidth()),
                    0,
                    (int) Math.floor(Math.random() * 20 + 5),
                    (int) Math.floor(Math.random() * 20 + 5));
            enemy.add(monster);

        }

    }

    // remove the monster that is outside the screen
    public void removeMonster(){

        for(int i = 0; i < enemy.size(); i++){
            int yPos = enemy.get(i).getCoordinates().getY();
            if(yPos > getHeight()){
                enemy.remove(i);
            }
        }

    }

    // update the movement of the game object e.g. player & enemy
    public void updateMovement() {

        if(isStart){

            // generate monster
            generateMonster();

            // sensor to control the movement of player
            if(orientationData.getOrientation() != null){

                float yaw = orientationData.getOrientation()[0] * 2;
                if(yaw < 0 && isLeft == false){
                    changeTime = System.currentTimeMillis();
                    isLeft = true;
                }else if(yaw > 0 && isLeft == true){
                    changeTime = System.currentTimeMillis();
                    isLeft = false;
                }
                float xSpeed = yaw * (float) (System.currentTimeMillis()-changeTime)/100;
                Log.d(TAG, "yaw: " + yaw);
                Log.d(TAG, "xSpeed: " + xSpeed);
                player.update((int)xSpeed, getWidth());
            }

            // update enemy movement
            for(Enemy monster: enemy)
                monster.update(getWidth(), getHeight());

            // remove enemy that is outside the screen
            removeMonster();

            checkCollision();

            currentTime = System.currentTimeMillis();
            score = (currentTime - startTime) / 10L;

        }

    }

}
