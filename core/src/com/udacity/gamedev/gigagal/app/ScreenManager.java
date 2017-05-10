package com.udacity.gamedev.gigagal.app;

import com.udacity.gamedev.gigagal.entity.GigaGal;
import com.udacity.gamedev.gigagal.overlay.Cursor;
import com.udacity.gamedev.gigagal.overlay.Menu;
import com.udacity.gamedev.gigagal.util.*;

// immutable singleton
public final class ScreenManager extends com.badlogic.gdx.Game {

    // fields
    private static final ScreenManager INSTANCE = new ScreenManager();

    // cannot be subclassed
    protected ScreenManager() {}

    // static factory method
    public static ScreenManager getInstance() { return INSTANCE; }

    @Override
    public void create() {
        GigaGal.getInstance().create();
        ChaseCam.getInstance().create();
        Cursor.getInstance().create();
        Menu.getInstance().create();
        com.udacity.gamedev.gigagal.util.ImageLoader.getInstance().create();
        InputControls.getInstance().create();
        LevelUpdater.getInstance().create();
        LaunchScreen.getInstance().create();
        OverworldScreen.getInstance().create();
        setScreen(LaunchScreen.getInstance());
    }

    @Override
    public void dispose() {
        super.dispose();
        System.gc();
    }
}