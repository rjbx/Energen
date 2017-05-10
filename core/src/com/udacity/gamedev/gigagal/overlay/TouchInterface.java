package com.udacity.gamedev.gigagal.overlay;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.util.ImageLoader;
import com.udacity.gamedev.gigagal.app.SaveData;
import com.udacity.gamedev.gigagal.util.*;
import com.udacity.gamedev.gigagal.util.InputControls;

// mutable
public final class TouchInterface {

    // fields
    public static final String TAG = TouchInterface.class.getName();
    private static final TouchInterface INSTANCE = new TouchInterface();
    private SpriteBatch batch; // class-level instantiation
    private static InputControls inputControls;
    private Viewport viewport; // class-level instantiation
    private boolean onMobile;

    // non-instantiable; cannot be subclassed
    private TouchInterface() {}

    public static TouchInterface getInstance() { return INSTANCE; }

    public final void init() {
        this.inputControls = InputControls.getInstance();
        this.viewport = new ExtendViewport(
                Constants.CONTROLS_OVERLAY_VIEWPORT_SIZE,
                Constants.CONTROLS_OVERLAY_VIEWPORT_SIZE);
        this.batch = new SpriteBatch();
    }

    public void render(SpriteBatch batch) {
        if (SaveData.hasTouchscreen()) {
            Helpers.drawTextureRegion(
                    batch,
                    viewport,
                    ImageLoader.getInstance().getOverlayAssets().left,
                    inputControls.leftCenter,
                    Constants.BUTTON_CENTER
            );

            Helpers.drawTextureRegion(
                    batch,
                    viewport,
                    ImageLoader.getInstance().getOverlayAssets().right,
                    inputControls.rightCenter,
                    Constants.BUTTON_CENTER
            );

            Helpers.drawTextureRegion(
                    batch,
                    viewport,
                    ImageLoader.getInstance().getOverlayAssets().up,
                    inputControls.upCenter,
                    Constants.BUTTON_CENTER
            );

            Helpers.drawTextureRegion(
                    batch,
                    viewport,
                    ImageLoader.getInstance().getOverlayAssets().down,
                    inputControls.downCenter,
                    Constants.BUTTON_CENTER
            );

            Helpers.drawTextureRegion(
                    batch,
                    viewport,
                    ImageLoader.getInstance().getOverlayAssets().center,
                    inputControls.centerCenter,
                    Constants.BUTTON_CENTER
            );
            Helpers.drawTextureRegion(
                    batch,
                    viewport,
                    ImageLoader.getInstance().getOverlayAssets().shoot,
                    inputControls.shootCenter,
                    Constants.BUTTON_CENTER
            );
            Helpers.drawTextureRegion(
                    batch,
                    viewport,
                    ImageLoader.getInstance().getOverlayAssets().jump,
                    inputControls.jumpCenter,
                    Constants.BUTTON_CENTER
            );
            Helpers.drawTextureRegion(
                    batch,
                    viewport,
                    ImageLoader.getInstance().getOverlayAssets().pause,
                    inputControls.pauseCenter,
                    Constants.BUTTON_CENTER
            );
        }
    }

    public void recalculateButtonPositions() {
        inputControls.leftCenter.set(Constants.POSITION_DIAMETER * .975f, Constants.POSITION_DIAMETER * 1.25f);
        inputControls.rightCenter.set(Constants.POSITION_DIAMETER * 2.55f, Constants.POSITION_DIAMETER * 1.25f);
        inputControls.upCenter.set(Constants.POSITION_DIAMETER * 1.7f, Constants.POSITION_DIAMETER * 2.1f);
        inputControls.downCenter.set(Constants.POSITION_DIAMETER * 1.7f, Constants.POSITION_DIAMETER * .54f);
        inputControls.centerCenter.set(Constants.POSITION_DIAMETER * 1.74f, Constants.POSITION_DIAMETER * 1.3125f);
        inputControls.pauseCenter.set(viewport.getWorldWidth() / 2 - 6.25f, Constants.POSITION_DIAMETER * .8f);
        inputControls.jumpCenter.set(
                viewport.getWorldWidth() - Constants.POSITION_DIAMETER * 2.5f,
                Constants.POSITION_DIAMETER
        );
        inputControls.shootCenter.set(
                viewport.getWorldWidth() - Constants.POSITION_DIAMETER,
                Constants.POSITION_DIAMETER
        );
    }

    public void dispose() { batch.dispose(); }

    public final Viewport getViewport() { return viewport; }
}