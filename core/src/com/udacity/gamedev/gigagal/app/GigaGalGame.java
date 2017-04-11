package com.udacity.gamedev.gigagal.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.udacity.gamedev.gigagal.entities.GigaGal;
import com.udacity.gamedev.gigagal.overlays.Cursor;
import com.udacity.gamedev.gigagal.overlays.Menu;
import com.udacity.gamedev.gigagal.overlays.OnscreenControls;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Enums;

// immutable singleton
public final class GigaGalGame extends Game {

    // fields
    private static final GigaGalGame INSTANCE = new GigaGalGame();
    private Preferences prefs;

    // cannot be subclassed
    private GigaGalGame() {}

    // static factory method
    public static GigaGalGame getInstance() { return INSTANCE; }

    @Override
    public void create() {
        prefs = Gdx.app.getPreferences("energraft-prefs");

        // init singletons
        Cursor.getInstance().init();
        Menu.getInstance().create();
        Assets.getInstance().init(new AssetManager());
        InputControls.getInstance().init();
        Level.getInstance().create();
        StartScreen.getInstance().create();
        LevelSelectScreen.getInstance().create();
        GameplayScreen.getInstance().create(Enums.LevelName.HOME);

        setScreen(StartScreen.getInstance());
    }

    @Override
    public void dispose() {
        prefs = null;
        OnscreenControls.getInstance().dispose();
        super.dispose();
        System.gc();
    }

    public Preferences getPreferences() { return prefs; }
}
