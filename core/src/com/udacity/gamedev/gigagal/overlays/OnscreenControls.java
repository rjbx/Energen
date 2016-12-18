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
    private final Vector2 leftCenter;
    private final Vector2 rightCenter;
    private final Vector2 upCenter;
    private final Vector2 downCenter;
    private final Vector2 centerCenter;
    private final Vector2 shootCenter;
    private final Vector2 jumpCenter;
    private GigaGal gigaGal;
    private int leftPointer;
    private int rightPointer;
    private int upPointer;
    private int downPointer;
    private int jumpPointer;
    private int shootPointer;

    // default ctor
    public OnscreenControls() {
        this.viewport = new ExtendViewport(
                Constants.ONSCREEN_CONTROLS_VIEWPORT_SIZE,
                Constants.ONSCREEN_CONTROLS_VIEWPORT_SIZE);

        leftCenter = new Vector2();
        rightCenter = new Vector2();
        upCenter = new Vector2();
        downCenter = new Vector2();
        centerCenter = new Vector2();
        shootCenter = new Vector2();
        jumpCenter = new Vector2();

        // TODO: fix button positions
        recalculateButtonPositions();
    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        Vector2 viewportPosition = viewport.unproject(new Vector2(screenX, screenY));

        if (viewportPosition.dst(shootCenter) < Constants.BUTTON_RADIUS) {
            Enums.ShotIntensity shotIntensity = Enums.ShotIntensity.NORMAL;
            if (gigaGal.isCharged()) {
                shotIntensity = Enums.ShotIntensity.CHARGED;
            }
            gigaGal.shoot(shotIntensity, gigaGal.getWeapon(), Utils.useAmmo(shotIntensity));
            gigaGal.setChargeStartTime(TimeUtils.nanoTime());
            this.shootPointer = pointer;
            gigaGal.shootButtonPressed = true;
        } else if (viewportPosition.dst(jumpCenter) < Constants.BUTTON_RADIUS) {
            // : Save the jumpPointer and set gigaGal.jumpButtonPressed = true
            this.jumpPointer = pointer;
            gigaGal.jumpButtonPressed = true;
        } else if (viewportPosition.dst(leftCenter) < Constants.BUTTON_RADIUS) {
            // : Save the leftPointer, and set gigaGal.leftButtonPressed = true
            this.leftPointer = pointer;
            gigaGal.leftButtonPressed = true;
        } else if (viewportPosition.dst(rightCenter) < Constants.BUTTON_RADIUS) {
            // : Save the rightPointer, and set gigaGal.rightButtonPressed = true
            this.rightPointer = pointer;
            gigaGal.rightButtonPressed = true;
        } else if (viewportPosition.dst(upCenter) < Constants.BUTTON_RADIUS) {
            // : Save the upPointer, and set gigaGal.upButtonPressed = true
            this.upPointer = pointer;
            gigaGal.upButtonPressed = true;
        }  else if (viewportPosition.dst(downCenter) < Constants.BUTTON_RADIUS) {
            // : Save the downPointer, and set gigaGal.downButtonPressed = true
            this.downPointer = pointer;
            gigaGal.downButtonPressed = true;
        }

        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector2 viewportPosition = viewport.unproject(new Vector2(screenX, screenY));

        if (pointer == leftPointer && viewportPosition.dst(rightCenter) < Constants.BUTTON_RADIUS) {

            // : Handle the case where the left button touch has been dragged to the right button
            // Inform GigaGal that the left button is no longer pressed
            gigaGal.leftButtonPressed = false;
            gigaGal.rightButtonPressed = true;
            // Inform GigaGal that the right button is now pressed
        
            // Zero leftPointer
            leftPointer = 0;
            // Save rightPointer
            rightPointer = pointer;
        }

        if (pointer == rightPointer && viewportPosition.dst(leftCenter) < Constants.BUTTON_RADIUS) {

            // : Handle the case where the right button touch has been dragged to the left button
            gigaGal.rightButtonPressed = false;
            gigaGal.leftButtonPressed = true;
            
            // Inform GigaGal that the right button is now pressed

            // Zero leftPointer
            rightPointer = 0;
            // Save rightPointer
            leftPointer = pointer;
        }

        if (pointer == upPointer && viewportPosition.dst(downCenter) < Constants.BUTTON_RADIUS) {

            // : Handle the case where the up button touch has been dragged to the down button
            // Inform GigaGal that the up button is no longer pressed
            gigaGal.upButtonPressed = false;
            gigaGal.downButtonPressed = true;
            // Inform GigaGal that the down button is now pressed

            // Zero upPointer
            upPointer = 0;
            // Save downPointer
            downPointer = pointer;
        }

        if (pointer == downPointer && viewportPosition.dst(upCenter) < Constants.BUTTON_RADIUS) {

            // : Handle the case where the down button touch has been dragged to the up button
            gigaGal.downButtonPressed = false;
            gigaGal.upButtonPressed = true;

            // Inform GigaGal that the down button is now pressed

            // Zero upPointer
            downPointer = 0;
            // Save downPointer
            upPointer = pointer;
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

        // : If the leftPointer is no longer touched, inform GigaGal and zero leftPointer
        if (!Gdx.input.isTouched(leftPointer)) {
            gigaGal.leftButtonPressed = false;
        }
        // : Do the same for rightPointer
        if (!Gdx.input.isTouched(rightPointer)) {
            gigaGal.rightButtonPressed = false;
        }

        // : If the upPointer is no longer touched, inform GigaGal and zero upPointer
        if (!Gdx.input.isTouched(upPointer)) {
            gigaGal.upButtonPressed = false;
        }
        // : Do the same for downPointer
        if (!Gdx.input.isTouched(downPointer)) {
            gigaGal.downButtonPressed = false;
        }

        if (!Gdx.input.isKeyJustPressed(jumpPointer)) {
            gigaGal.jumpButtonPressed = false;
            jumpPointer = 0;
        }

        Utils.drawTextureRegion(
                batch,
                Assets.getInstance().getOnscreenControlsAssets().left,
                leftCenter,
                Constants.BUTTON_CENTER
        );

        Utils.drawTextureRegion(
                batch,
                Assets.getInstance().getOnscreenControlsAssets().right,
                rightCenter,
                Constants.BUTTON_CENTER
        );

        Utils.drawTextureRegion(
                batch,
                Assets.getInstance().getOnscreenControlsAssets().up,
                upCenter,
                Constants.BUTTON_CENTER
        );

        Utils.drawTextureRegion(
                batch,
                Assets.getInstance().getOnscreenControlsAssets().down,
                downCenter,
                Constants.BUTTON_CENTER
        );

        Utils.drawTextureRegion(
                batch,
                Assets.getInstance().getOnscreenControlsAssets().center,
                centerCenter,
                Constants.BUTTON_CENTER
        );

        if (!gigaGal.isCharged()) {
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
        leftCenter.set(Constants.BUTTON_RADIUS * .775f, Constants.BUTTON_RADIUS * 1.25f);
        rightCenter.set(Constants.BUTTON_RADIUS * 2.35f, Constants.BUTTON_RADIUS * 1.25f);
        upCenter.set(Constants.BUTTON_RADIUS * 1.5f, Constants.BUTTON_RADIUS * 2.1f);
        downCenter.set(Constants.BUTTON_RADIUS * 1.5f, Constants.BUTTON_RADIUS * .54f);
        centerCenter.set(Constants.BUTTON_RADIUS * 1.54f, Constants.BUTTON_RADIUS * 1.3125f);
        shootCenter.set(
                viewport.getWorldWidth() - Constants.BUTTON_RADIUS * 3f,
                Constants.BUTTON_RADIUS * 3 / 4
        );
        jumpCenter.set(
                viewport.getWorldWidth() - Constants.BUTTON_RADIUS,
                Constants.BUTTON_RADIUS
        );
    }

    public final Viewport getViewport() { return viewport; }
    public final void setGigaGal(GigaGal gigaGal) { this.gigaGal = gigaGal; }
}
