package com.udacity.gamedev.gigagal;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.udacity.gamedev.gigagal.util.Assets;

// immutable
public final class GigaGalGame extends Game {

    // default ctor
    public GigaGalGame() {}

    @Override
    public void create() {
        AssetManager am = new AssetManager();
        Assets.getInstance().init(am, 3);
        setScreen(new LevelSelectScreen());
       // setScreen(new GameplayScreen(3));
    }
}
