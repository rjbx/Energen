package com.udacity.gamedev.gigagal.app;

import com.udacity.gamedev.gigagal.entity.GigaGal;
import com.udacity.gamedev.gigagal.overlay.Cursor;
import com.udacity.gamedev.gigagal.overlay.Menu;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.ChaseCam;
import com.udacity.gamedev.gigagal.util.InputControls;

// immutable singleton
public final class Game extends com.badlogic.gdx.Game {

    // fields
    private static final Game INSTANCE = new Game();

    // cannot be subclassed
    protected Game() {}

    // static factory method
    public static Game getInstance() { return INSTANCE; }

    @Override
    public void create() {
        GigaGal.getInstance().create();
        ChaseCam.getInstance().create();
        Cursor.getInstance().create();
        Menu.getInstance().create();
        Assets.getInstance().create();
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