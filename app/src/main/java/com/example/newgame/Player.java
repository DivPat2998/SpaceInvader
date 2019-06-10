package com.example.newgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Player {

    private Bitmap bitmap;

    private int x;
    private int y;

    private int speed = 0;

    public Player(Context context){
        x=75;
        y=50;
        speed = 1;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
    }

    public void update(){

        x++;
    }
    public Bitmap getBitmap(){
        return bitmap;
    }

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public int getSpeed(){
        return speed;
    }
}
