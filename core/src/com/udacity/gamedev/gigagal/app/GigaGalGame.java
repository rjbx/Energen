package com.udacity.gamedev.gigagal.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.udacity.gamedev.gigagal.entities.GigaGal;
import com.udacity.gamedev.gigagal.overlays.OnscreenControls;
import com.udacity.gamedev.gigagal.util.Assets;

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

        Assets.getInstance().init(new AssetManager());
        InputControls.getInstance().init();
        StartScreen.getInstance().create();
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
