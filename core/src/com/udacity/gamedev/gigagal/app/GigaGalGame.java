package com.udacity.gamedev.gigagal.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.udacity.gamedev.gigagal.entities.GigaGal;
import com.udacity.gamedev.gigagal.overlays.Cursor;
import com.udacity.gamedev.gigagal.overlays.Menu;
import com.udacity.gamedev.gigagal.overlays.OnscreenControls;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Timer;

// immutable singleton
public final class GigaGalGame extends Game {

    // fields
    private static final GigaGalGame INSTANCE = new GigaGalGame();
    private Preferences prefs;
    private Timer time;
    private int score;

    // cannot be subclassed
    private GigaGalGame() {}

    // static factory method
    public static GigaGalGame getInstance() { return INSTANCE; }

    @Override
    public void create() {
        prefs = Gdx.app.getPreferences("energraft-prefs");
        time = new Timer().start(prefs.getLong("Time")).suspend();
        score = Integer.valueOf(prefs.getInteger("Score"));

        GigaGal.getInstance().create();
        Cursor.getInstance().create();
        Menu.getInstance().create();
        Assets.getInstance().create();
        InputControls.getInstance().create();
        Level.getInstance().create();
        StartScreen.getInstance().create();
        LevelSelectScreen.getInstance().create();
        GameplayScreen.getInstance().create();

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

    public Timer getTime() { return time; }
    public Integer getScore() { return score; }
}
