package com.udacity.gamedev.gigagal;

import com.badlogic.gdx.Game;

// immutable
public final class GigaGalGame extends Game {

    // default ctor
    public GigaGalGame() {}

    @Override
    public void create() {
        setScreen(new LevelSelectScreen());
        setScreen(new GameplayScreen(3));
    }
}
