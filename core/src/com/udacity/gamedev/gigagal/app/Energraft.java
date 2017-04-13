package com.udacity.gamedev.gigagal.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.udacity.gamedev.gigagal.entities.GigaGal;
import com.udacity.gamedev.gigagal.overlays.Cursor;
import com.udacity.gamedev.gigagal.overlays.Menu;
import com.udacity.gamedev.gigagal.overlays.TouchInterface;
import com.udacity.gamedev.gigagal.screens.LaunchScreen;
import com.udacity.gamedev.gigagal.screens.LevelScreen;
import com.udacity.gamedev.gigagal.screens.OverworldScreen;
import com.udacity.gamedev.gigagal.util.*;

// immutable singleton
public final class Energraft extends Game {

    // fields
    private static final Energraft INSTANCE = new Energraft();
    private Preferences prefs;
    private Integer score;

    // cannot be subclassed
    private Energraft() {}

    // static factory method
    public static Energraft getInstance() { return INSTANCE; }

    @Override
    public void create() {
        prefs = Gdx.app.getPreferences("energraft-prefs");
        score = prefs.getInteger("Score");

        GigaGal.getInstance().create();
        Cursor.getInstance().create();
        Menu.getInstance().create();
        Assets.getInstance().create();
        InputControls.getInstance().create();
        Level.getInstance().create();
        LaunchScreen.getInstance().create();
        OverworldScreen.getInstance().create();
        setScreen(LaunchScreen.getInstance());
    }

    @Override
    public void dispose() {
        prefs = null;
        TouchInterface.getInstance().dispose();
        super.dispose();
        System.gc();
    }

    public Preferences getPreferences() { return prefs; }

    public Integer getScore() { return score; }
}
