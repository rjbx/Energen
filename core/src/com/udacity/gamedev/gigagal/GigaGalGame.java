package com.udacity.gamedev.gigagal;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.udacity.gamedev.gigagal.overlays.ControlsOverlay;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.InputControls;

// immutable
public final class GigaGalGame extends Game {

    // fields
    private LevelSelectScreen levelSelect;
    private GameplayScreen gameplay;

    // default ctor
    public GigaGalGame() {
    }

    @Override
    public void create() {
        gameplay = new GameplayScreen(this);
        levelSelect = new LevelSelectScreen(this);
        AssetManager am = new AssetManager();
        Assets.getInstance().init(am, 0);
        ControlsOverlay.getInstance().init();
        InputControls.getInstance().init();
        setScreen(levelSelect);
    }

    public LevelSelectScreen getLevelSelectScreen() { return levelSelect; }
    public GameplayScreen getGameplayScreen() { return gameplay; }
}
