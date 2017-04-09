package com.udacity.gamedev.gigagal.overlays;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.util.*;
import com.udacity.gamedev.gigagal.app.InputControls;

// mutable
public class ControlsOverlay {

    // fields
    public static final String TAG = ControlsOverlay.class.getName();
    private static final ControlsOverlay INSTANCE = new ControlsOverlay();
    private SpriteBatch batch; // class-level instantiation
    private static InputControls inputControls;
    private Viewport viewport; // class-level instantiation
    public boolean onMobile;

    // non-instantiable; cannot be subclassed
    private ControlsOverlay() {}

    public static ControlsOverlay getInstance() { return INSTANCE; }

    public void init() {
        this.inputControls = InputControls.getInstance();
        this.viewport = new ExtendViewport(
                Constants.CONTROLS_OVERLAY_VIEWPORT_SIZE,
                Constants.CONTROLS_OVERLAY_VIEWPORT_SIZE);
        this.batch = new SpriteBatch();
    }

    public void render(SpriteBatch batch, Viewport viewport) {
        if (onMobile) {
            viewport.apply();
            batch.setProjectionMatrix(viewport.getCamera().combined);
            batch.begin();

            Utils.drawTextureRegion(
                    batch,
                    Assets.getInstance().getOverlayAssets().left,
                    inputControls.leftCenter,
                    Constants.BUTTON_CENTER
            );

            Utils.drawTextureRegion(
                    batch,
                    Assets.getInstance().getOverlayAssets().right,
                    inputControls.rightCenter,
                    Constants.BUTTON_CENTER
            );

            Utils.drawTextureRegion(
                    batch,
                    Assets.getInstance().getOverlayAssets().up,
                    inputControls.upCenter,
                    Constants.BUTTON_CENTER
            );

            Utils.drawTextureRegion(
                    batch,
                    Assets.getInstance().getOverlayAssets().down,
                    inputControls.downCenter,
                    Constants.BUTTON_CENTER
            );

            Utils.drawTextureRegion(
                    batch,
                    Assets.getInstance().getOverlayAssets().center,
                    inputControls.centerCenter,
                    Constants.BUTTON_CENTER
            );
            Utils.drawTextureRegion(
                    batch,
                    Assets.getInstance().getOverlayAssets().shoot,
                    inputControls.shootCenter,
                    Constants.BUTTON_CENTER
            );
            Utils.drawTextureRegion(
                    batch,
                    Assets.getInstance().getOverlayAssets().jump,
                    inputControls.jumpCenter,
                    Constants.BUTTON_CENTER
            );
            Utils.drawTextureRegion(
                    batch,
                    Assets.getInstance().getOverlayAssets().pause,
                    inputControls.pauseCenter,
                    Constants.BUTTON_CENTER
            );

            batch.end();
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