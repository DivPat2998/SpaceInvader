package com.example.newgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class GameView extends SurfaceView implements Runnable {
    volatile boolean playing;

    private Thread gameThread = null;

    private Player player;

    private Paint paint;

    private Canvas canvas;

    private SurfaceHolder surfaceHolder;

    private Enemy enemies;

    private Friend friend;

    private int enemyCount = 3;

    private ArrayList<Star> stars = new

            ArrayList<Star>();
    private Boom boom;

    public GameView(Context context, int screenX, int screenY) {
        super(context);

        player = new Player(context, screenX, screenY);

        surfaceHolder = getHolder();
        paint = new Paint();

        int starNums = 100;
        for(int i = 0; i < starNums; i++){
            Star s = new Star(screenX, screenY);
            stars.add(s);

        }
       enemies = new Enemy(context, screenX, screenY);

        boom = new Boom(context);

        friend = new Friend(context, screenX, screenY);

    }

    @Override
    public void run() {
        while(playing){
            update();
            draw();
            control();
        }
    }

    private void update() {
        player.update();

        boom.setX(-250);
        boom.setY(-250);

        for(Star s : stars){
            s.update(player.getSpeed());
        }

        enemies.update(player.getSpeed());
        if(Rect.intersects(player.getDetectCollision(),enemies.getDetectCollision())){
            boom.setX(enemies.getX());
            boom.setY(enemies.getY());
            enemies.setX(-200);
        }
        friend.update(player.getSpeed());
    }

    private void draw(){
        if(surfaceHolder.getSurface().isValid()){
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.BLACK);
            paint.setColor(Color.WHITE);

            for(Star s : stars){
                paint.setStrokeWidth(s.getStarWidth());
                canvas.drawPoint(s.getX(), s.getY(),paint);
            }
            canvas.drawBitmap(player.getBitmap(),player.getX(), player.getY(), paint);


            canvas.drawBitmap(enemies.getBitmap(), enemies.getX(),enemies.getY(),paint);



            canvas.drawBitmap(boom.getBitmap(),boom.getX(),boom.getY(),paint);

            canvas.drawBitmap(friend.getBitmap(),friend.getX(),friend.getY(),paint);

            surfaceHolder.unlockCanvasAndPost(canvas);


        }

    }
    private void control(){
        try{
            gameThread.sleep(17);
        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }
    public void  pause(){
        playing = false;
        try{
            gameThread.join();

        } catch (InterruptedException e){

        }
    }
    public void resume(){
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_UP:
                player.stopBoosting();
                break;

            case MotionEvent.ACTION_DOWN:

                player.setBoosting();

                break;
        }
        return true;
    }
}
