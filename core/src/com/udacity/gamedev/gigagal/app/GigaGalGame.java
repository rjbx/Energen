package com.udacity.gamedev.gigagal.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.udacity.gamedev.gigagal.overlays.ControlsOverlay;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Enums;

// immutable
public final class GigaGalGame extends Game {

    // fields
    private StartScreen startScreen;
    private Preferences prefs;

    // default ctor
    public GigaGalGame() {}

    @Override
    public void create() {
        prefs = Gdx.app.getPreferences("energraft-prefs");
        startScreen = new StartScreen(this);

        Assets.getInstance().init(new AssetManager());
        InputControls.getInstance().init();

        setScreen(startScreen);
    }

    @Override
    public void dispose() {
        prefs = null;
        ControlsOverlay.getInstance().dispose();
        Assets.getInstance().dispose();
        super.dispose();
    }

    public Preferences getPreferences() { return prefs; }
}
