package com.udacity.gamedev.gigagal.app;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
    private SpriteBatch batch;

    // cannot be subclassed
    private ScreenManager() {}

    // static factory method
    public static ScreenManager getInstance() { return INSTANCE; }

    @Override
    public void create() {
        batch = new SpriteBatch();
        Assets.getInstance().create();
        ChaseCam.getInstance().create();
        StaticCam.getInstance().create();
        GigaGal.getInstance().create();
        Blade.getInstance().create();
        Cursor.getInstance().create();
        Menu.getInstance().create();
        InputControls.getInstance().create();
        LevelUpdater.getInstance().create();
        setScreen(LaunchScreen.getInstance());
    }

    @Override
    public void dispose() {
        Assets.getInstance().dispose();
        batch.dispose();
        super.dispose();
        System.gc();
    }

    protected final SpriteBatch getBatch() { return batch; }
}