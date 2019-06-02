package com.example.newgame;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GameView game = new GameView(this);
        setContentView(game);

    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
