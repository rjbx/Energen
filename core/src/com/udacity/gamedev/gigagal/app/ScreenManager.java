package com.udacity.gamedev.gigagal.app;

import com.udacity.gamedev.gigagal.entity.Blade;
import com.udacity.gamedev.gigagal.entity.GigaGal;
import com.udacity.gamedev.gigagal.overlay.Cursor;
import com.udacity.gamedev.gigagal.overlay.Menu;
import com.udacity.gamedev.gigagal.util.*;

// immutable singleton
public final class ScreenManager extends com.badlogic.gdx.Game {

    // fields
    public final static String TAG = ScreenManager.class.getName();
    private static final ScreenManager INSTANCE = new ScreenManager();

    // cannot be subclassed
    protected ScreenManager() {}

    // static factory method
    public static ScreenManager getInstance() { return INSTANCE; }

    @Override
    public void create() {
        Assets.getInstance().create();
        GigaGal.getInstance().create();
        Blade.getInstance().create();
        ChaseCam.getInstance().create();
        StaticCam.getInstance().create();
        Cursor.getInstance().create();
        Menu.getInstance().create();
        InputControls.getInstance().create();
        LevelUpdater.getInstance().create();
        LaunchScreen.getInstance().create();
        OverworldScreen.getInstance().create();
        setScreen(LaunchScreen.getInstance());
    }

    @Override
    public void dispose() {
        Assets.getInstance().dispose();
        super.dispose();
        System.gc();
    }
}