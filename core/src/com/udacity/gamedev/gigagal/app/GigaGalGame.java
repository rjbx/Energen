package com.udacity.gamedev.gigagal.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.udacity.gamedev.gigagal.util.Assets;

// immutable
public final class GigaGalGame extends Game {

    // fields
    private LevelSelectScreen levelSelectScreen;
    private GameplayScreen gameplayScreen;
    private StartScreen startScreen;
    private Preferences prefs;

    // default ctor
    public GigaGalGame() {}

    @Override
    public void create() {
        prefs = Gdx.app.getPreferences("energraft-prefs");
        gameplayScreen = new GameplayScreen(this);
        levelSelectScreen = new LevelSelectScreen(this);
        startScreen = new StartScreen(this);
        Assets.getInstance().setLevelName("MECHANICAL");
        Assets.getInstance().init(new AssetManager());
        InputControls.getInstance().init();
        setScreen(startScreen);
    }

    public LevelSelectScreen getLevelSelectScreen() { return levelSelectScreen; }
    public GameplayScreen getGameplayScreen() { return gameplayScreen; }
    public Preferences getPreferences() { return prefs; }
}
