package com.example.newgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Enemy {

    private Bitmap bitmap;
    private  int x;
    private  int y;

    private int speed = 1;

    private int maxX;
    private int minX;

    private int maxY;
    private int minY;

    public Enemy(Context context, int screenX, int screenY){

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy);

        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;

        Random generator = new Random();
        speed = generator.nextInt(6) + 10;
        x = screenX;
        y = generator.nextInt(maxY) - bitmap.getHeight();
    }

    public void update(int playerSpeed){
        x-= playerSpeed;
        x-= speed;
        if(x<minX - bitmap.getWidth()){
            Random generator = new Random();
            speed = generator.nextInt(10)+10;
            x=maxX;
            y = generator.nextInt(maxY) - bitmap.getHeight();
        }
    }

public Bitmap getBitmap(){
        return bitmap;
    }

public int getX(){
        return  x;
}

public int getY(){
        return y;
}

public int getSpeed(){
        return speed;
}

}
