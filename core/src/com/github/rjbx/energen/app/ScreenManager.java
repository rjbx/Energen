package com.github.rjbx.energen.app;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.rjbx.energen.entity.Blade;
import com.github.rjbx.energen.entity.Avatar;
import com.github.rjbx.energen.overlay.*;
import com.github.rjbx.energen.util.*;

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
        AssetManager.getInstance().create();
        ChaseCam.getInstance().create();
        StaticCam.getInstance().create();
        Avatar.getInstance().create();
        Blade.getInstance().create();
        Cursor.getInstance().create();
        Menu.getInstance().create();
        InputControls.getInstance().create();
        LevelUpdater.getInstance().create();
        setScreen(LaunchScreen.getInstance());
    }

    @Override
    public void dispose() {
        AssetManager.getInstance().dispose();
        batch.dispose();
        super.dispose();
        System.gc();
    }

    protected final SpriteBatch getBatch() { return batch; }
}