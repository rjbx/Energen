package com.udacity.gamedev.gigagal.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.udacity.gamedev.gigagal.entities.GigaGal;
import com.udacity.gamedev.gigagal.overlays.Cursor;
import com.udacity.gamedev.gigagal.overlays.Menu;
import com.udacity.gamedev.gigagal.overlays.TouchInterface;
import com.udacity.gamedev.gigagal.screens.LaunchScreen;
import com.udacity.gamedev.gigagal.screens.OverworldScreen;
import com.udacity.gamedev.gigagal.util.*;

// immutable singleton
public final class Energraft extends Game {

    // fields
    private static final Energraft INSTANCE = new Energraft();

    // cannot be subclassed
    private Energraft() {}

    // static factory method
    public static Energraft getInstance() { return INSTANCE; }

    @Override
    public void create() {
        GigaGal.getInstance().create();
        ChaseCam.getInstance().create();
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
        super.dispose();
        System.gc();
    }

    private Preferences getPreferences() { return Gdx.app.getPreferences("energraft-prefs"); }

    public String getWeapons() { return getPreferences().getString("Weapons", Enums.WeaponType.NATIVE.name()); }
    public boolean getTouchscreen() { return getPreferences().getBoolean("Touchscreen", false); }
    public int getDifficulty() { return getPreferences().getInteger("Difficulty", -1); }
    public int getScore() { return getPreferences().getInteger("Score", 0); }
    public long getTime() { return getPreferences().getLong("Time", 0); }

    public void setWeapons(String weapons) { getPreferences().putString("Weapons", weapons); getPreferences().flush(); }
    public void setTouchscreen(boolean touchscreen) { getPreferences().putBoolean("Touchscreen", touchscreen); getPreferences().flush(); }
    public void setDifficulty(int difficulty) { getPreferences().putInteger("Difficulty", difficulty); getPreferences().flush(); }
    public void setScore(int score) { getPreferences().putInteger("Score", score); getPreferences().flush(); }
    public void setTime(long time) { getPreferences().putLong("Time", time); getPreferences().flush(); }

    public void erase() { getPreferences().clear(); getPreferences().flush(); }
}