package com.example.newgame;

import android.content.Context;
import android.content.SharedPreferences;
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

    int screenX;

    int countMisses;

    boolean flag;

    int score;

    int highScore[] = new int[4];

    SharedPreferences sharedPreferences;


    private boolean isGameOver;

    public GameView(Context context, int screenX, int screenY) {
        super(context);

        score = 0;

        sharedPreferences = context.getSharedPreferences("SHAR_PREF_NAME",Context.MODE_PRIVATE);

        highScore[0] = sharedPreferences.getInt("score1",0);
        highScore[1] = sharedPreferences.getInt("score2",0);
        highScore[2] = sharedPreferences.getInt("score3",0);
        highScore[3] = sharedPreferences.getInt("score4",0);
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

        this.screenX = screenX;

        countMisses = 0;

        isGameOver = false;


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

        score++;

        player.update();


        boom.setX(-250);
        boom.setY(-250);

        for(Star s : stars){
            s.update(player.getSpeed());
        }

        if(enemies.getX()==screenX){
            flag = true;
        }

        enemies.update(player.getSpeed());
        if(Rect.intersects(player.getDetectCollision(),enemies.getDetectCollision())){
            boom.setX(enemies.getX());
            boom.setY(enemies.getY());
            enemies.setX(-200);
        }
        else{
            if(flag){
                if(player.getDetectCollision().exactCenterX() >= enemies.getDetectCollision().exactCenterX()){
                    countMisses++;

                    flag = false;

                    if(countMisses == 3){
                        playing = false;

                        isGameOver = true;

                        for(int i=0;i<4;i++){
                            if(highScore[i]<score){
                                final int finalI = i;
                                highScore[i] = 4;
                                break;
                            }
                        }
                        SharedPreferences.Editor e = sharedPreferences.edit();
                        for(int i=0;i<4;i++){
                            int j = i+1;
                            e.putInt("score"+j,highScore[i]);
                        }
                        e.apply();
                    }
                }
            }
        }

        friend.update(player.getSpeed());

        if(Rect.intersects(player.getDetectCollision(),friend.getDetectCollision())){

            boom.setX(friend.getX());
            boom.setY(friend.getY());

            playing = false;

            isGameOver = true;

            for(int i=0;i<4;i++){
                if(highScore[i]<score){
                    final int finalI = i;
                    highScore[i] = score;
                    break;
                }
            }
            SharedPreferences.Editor e = sharedPreferences.edit();
            for(int i = 0;i<4;i++){
                int j = i+1;
                e.putInt("Score"+j,highScore[i]);
            }
            e.apply();
        }
    }

    private void draw(){
        if(surfaceHolder.getSurface().isValid()){
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.BLACK);
            paint.setColor(Color.WHITE);
            paint.setTextSize(20);

            for(Star s : stars){
                paint.setStrokeWidth(s.getStarWidth());
                canvas.drawPoint(s.getX(), s.getY(),paint);
            }
            paint.setTextSize(30);
            canvas.drawText("Score"+score,100,50,paint);
            canvas.drawBitmap(player.getBitmap(),player.getX(), player.getY(), paint);


            canvas.drawBitmap(enemies.getBitmap(), enemies.getX(),enemies.getY(),paint);

            canvas.drawBitmap(boom.getBitmap(),boom.getX(),boom.getY(),paint);

            canvas.drawBitmap(friend.getBitmap(),friend.getX(),friend.getY(),paint);

            if(isGameOver){
                paint.setTextSize(150);
                paint.setTextAlign(Paint.Align.CENTER);
                int yPos=(int) ((canvas.getHeight()/2)- paint.descent() + paint.ascent()/2);
                canvas.drawText("Game Over", canvas.getWidth()/2, yPos,paint);
            }

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
