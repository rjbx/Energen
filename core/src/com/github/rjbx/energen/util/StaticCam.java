package com.github.rjbx.energen.util;

import com.badlogic.gdx.utils.viewport.ExtendViewport;

public final class StaticCam {
    // fields
    public static final String TAG = StaticCam.class.getName();
    private static final StaticCam INSTANCE = new StaticCam();
    private static ExtendViewport viewport;

    // cannot be subclassed
    private StaticCam() {}

    // static factory
    public static StaticCam getInstance() {
        return INSTANCE;
    }

    public void create() {
        viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE); // shared by all overlays
    }

    public final ExtendViewport getViewport() { return viewport; }
}
