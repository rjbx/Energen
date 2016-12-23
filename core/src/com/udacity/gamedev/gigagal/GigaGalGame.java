package com.udacity.gamedev.gigagal;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.udacity.gamedev.gigagal.util.Assets;

// immutable
public final class GigaGalGame extends Game {

    // fields
    private LevelSelectScreen levelSelect;
    private GameplayScreen gameplay;
    private PauseScreen pause;


    // default ctor
    public GigaGalGame() {
    }

    @Override
    public void create() {
        gameplay = new GameplayScreen(this);
        levelSelect = new LevelSelectScreen(this);
        AssetManager am = new AssetManager();
        Assets.getInstance().init(am, 0);
        setScreen(levelSelect);
    }

    public LevelSelectScreen getLevelSelectScreen() { return levelSelect; }
    public GameplayScreen getGameplayScreen() { return gameplay; }
    public PauseScreen getPauseScreen() { return pause; }
}
