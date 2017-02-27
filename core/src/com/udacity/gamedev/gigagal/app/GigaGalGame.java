package com.udacity.gamedev.gigagal.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.udacity.gamedev.gigagal.util.Assets;

// immutable
public final class GigaGalGame extends Game {

    // fields
    private LevelSelectScreen levelSelect;
    private GameplayScreen gameplay;
    private StartScreen startScreen;
    private Preferences prefs;

    // default ctor
    public GigaGalGame() {}

    @Override
    public void create() {
        gameplay = new GameplayScreen(this);
        levelSelect = new LevelSelectScreen(this);
        startScreen = new StartScreen(this);
        AssetManager am = new AssetManager();
        Assets.getInstance().init(am, 0);
        InputControls.getInstance().init();
        setScreen(startScreen);

        prefs = Gdx.app.getPreferences("energraft-prefs");
        int score = prefs.getInteger("Score", 0);
        gameplay.setTotalScore(score);
    }

    public LevelSelectScreen getLevelSelectScreen() { return levelSelect; }
    public GameplayScreen getGameplayScreen() { return gameplay; }
    public Preferences getPreferences() { return prefs; }
}
