package com.udacity.gamedev.gigagal.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.entities.GigaGal;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Utils;

// mutable
public class OnscreenControls extends InputAdapter {

    // fields
    public static final String TAG = OnscreenControls.class.getName();
    private final Viewport viewport;
    private final Vector2 moveLeftCenter;
    private final Vector2 moveRightCenter;
    private final Vector2 shootCenter;
    private final Vector2 jumpCenter;
    private GigaGal gigaGal;
    private int moveLeftPointer;
    private int moveRightPointer;
    private int jumpPointer;
    private int shootPointer;

    // default ctor
    public OnscreenControls() {
        this.viewport = new ExtendViewport(
                Constants.ONSCREEN_CONTROLS_VIEWPORT_SIZE,
                Constants.ONSCREEN_CONTROLS_VIEWPORT_SIZE);

        moveLeftCenter = new Vector2();
        moveRightCenter = new Vector2();
        shootCenter = new Vector2();
        jumpCenter = new Vector2();

        // TODO: fix button positions
        recalculateButtonPositions();
    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        Vector2 viewportPosition = viewport.unproject(new Vector2(screenX, screenY));

        if (viewportPosition.dst(shootCenter) < Constants.BUTTON_RADIUS) {
            gigaGal.shoot(Enums.ShotIntensity.NORMAL, gigaGal.getWeapon());
            gigaGal.setChargeStartTime(TimeUtils.nanoTime());
            this.shootPointer = pointer;
            gigaGal.shootButtonPressed = true;
        } else if (viewportPosition.dst(jumpCenter) < Constants.BUTTON_RADIUS) {
            // : Save the jumpPointer and set gigaGal.jumpButtonPressed = true
            this.jumpPointer = pointer;
            gigaGal.jumpButtonPressed = true;
        } else if (viewportPosition.dst(moveLeftCenter) < Constants.BUTTON_RADIUS) {
            // : Save the moveLeftPointer, and set gigaGal.leftButtonPressed = true
            this.moveLeftPointer = pointer;
            gigaGal.leftButtonPressed = true;
        } else if (viewportPosition.dst(moveRightCenter) < Constants.BUTTON_RADIUS) {
            // : Save the moveRightPointer, and set gigaGal.rightButtonPressed = true
            this.moveRightPointer = pointer;
            gigaGal.rightButtonPressed = true;
        }

        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector2 viewportPosition = viewport.unproject(new Vector2(screenX, screenY));

        if (pointer == moveLeftPointer && viewportPosition.dst(moveRightCenter) < Constants.BUTTON_RADIUS) {

            // : Handle the case where the left button touch has been dragged to the right button
            // Inform GigaGal that the left button is no longer pressed
            gigaGal.leftButtonPressed = false;
            gigaGal.rightButtonPressed = true;
            // Inform GigaGal that the right button is now pressed
        
            // Zero moveLeftPointer
            moveLeftPointer = 0;
            // Save moveRightPointer
            moveRightPointer = pointer;
        }

        if (pointer == moveRightPointer && viewportPosition.dst(moveLeftCenter) < Constants.BUTTON_RADIUS) {

            // : Handle the case where the right button touch has been dragged to the left button
            gigaGal.rightButtonPressed = false;
            gigaGal.leftButtonPressed = true;
            
            // Inform GigaGal that the right button is now pressed

            // Zero moveLeftPointer
            moveRightPointer = 0;
            // Save moveRightPointer
            moveLeftPointer = pointer;
        }

        if (pointer == shootPointer && viewportPosition.dst(shootCenter) < Constants.BUTTON_RADIUS) {
            gigaGal.shootButtonPressed = true;
        }

        return super.touchDragged(screenX, screenY, pointer);
    }

    public void render(SpriteBatch batch) {

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        if (!Gdx.input.isTouched(shootPointer)) {
            gigaGal.shootButtonPressed = false;
            shootPointer = 0;
        }

        // : If the moveLeftPointer is no longer touched, inform GigaGal and zero moveLeftPointer
        if (!Gdx.input.isTouched(moveLeftPointer)) {
            gigaGal.leftButtonPressed = false;
        }
        // : Do the same for moveRightPointer
        if (!Gdx.input.isTouched(moveRightPointer)) {
            gigaGal.rightButtonPressed = false;
        }

        if (!Gdx.input.isKeyJustPressed(jumpPointer)) {
            gigaGal.jumpButtonPressed = false;
            jumpPointer = 0;
        }

        Utils.drawTextureRegion(
                batch,
                Assets.getInstance().getOnscreenControlsAssets().moveLeft,
                moveLeftCenter,
                Constants.BUTTON_CENTER
        );

        Utils.drawTextureRegion(
                batch,
                Assets.getInstance().getOnscreenControlsAssets().moveRight,
                moveRightCenter,
                Constants.BUTTON_CENTER
        );

        if (!gigaGal.getChargeStatus()) {
            Utils.drawTextureRegion(
                    batch,
                    Assets.getInstance().getOnscreenControlsAssets().shoot,
                    shootCenter,
                    Constants.BUTTON_CENTER
            );
        } else {
            Utils.drawTextureRegion(
                    batch,
                    Assets.getInstance().getOnscreenControlsAssets().blast,
                    shootCenter,
                    Constants.BUTTON_CENTER
            );
        }

        if (!gigaGal.getRicochetStatus())  {
             if (!gigaGal.getJumpStatus() && gigaGal.getHoverStatus()) {
                Utils.drawTextureRegion(
                        batch,
                        Assets.getInstance().getOnscreenControlsAssets().hover,
                        jumpCenter,
                        Constants.BUTTON_CENTER
                );
            } else {
                 Utils.drawTextureRegion(
                         batch,
                         Assets.getInstance().getOnscreenControlsAssets().jump,
                         jumpCenter,
                         Constants.BUTTON_CENTER
                 );
             }
        } else {
            Utils.drawTextureRegion(
                    batch,
                    Assets.getInstance().getOnscreenControlsAssets().ricochet,
                    jumpCenter,
                    Constants.BUTTON_CENTER
            );
        }
        batch.end();
    }

    public void recalculateButtonPositions() {
        moveLeftCenter.set(Constants.BUTTON_RADIUS * 3 / 4, Constants.BUTTON_RADIUS);
        moveRightCenter.set(Constants.BUTTON_RADIUS * 2, Constants.BUTTON_RADIUS * 3 / 4);
        shootCenter.set(
                viewport.getWorldWidth() - Constants.BUTTON_RADIUS * 2f,
                Constants.BUTTON_RADIUS * 3 / 4
        );
        jumpCenter.set(
                viewport.getWorldWidth() - Constants.BUTTON_RADIUS * 3 / 4,
                Constants.BUTTON_RADIUS
        );
    }

    public final Viewport getViewport() { return viewport; }
    public final void setGigaGal(GigaGal gigaGal) { this.gigaGal = gigaGal; }
}
