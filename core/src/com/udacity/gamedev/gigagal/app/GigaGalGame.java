package com.udacity.gamedev.gigagal.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.udacity.gamedev.gigagal.overlays.ControlsOverlay;
import com.udacity.gamedev.gigagal.util.Assets;

// immutable
public final class GigaGalGame extends Game {

    // fields
    private Preferences prefs;

    // default ctor
    public GigaGalGame() {}

    @Override
    public void create() {
        prefs = Gdx.app.getPreferences("energraft-prefs");

        Assets.getInstance().init(new AssetManager());
        InputControls.getInstance().init();

        setScreen(new StartScreen(this));
    }

    @Override
    public void dispose() {
        prefs = null;
        ControlsOverlay.getInstance().dispose();
        Assets.getInstance().dispose();
        super.dispose();
        System.gc();
    }

    public Preferences getPreferences() { return prefs; }
}
