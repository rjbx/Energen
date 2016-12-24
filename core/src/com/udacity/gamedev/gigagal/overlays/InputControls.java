package com.udacity.gamedev.gigagal.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
public class InputControls extends InputAdapter {

    // fields
    public static final String TAG = InputControls.class.getName();
    private final Viewport viewport;
    private final Vector2 leftCenter;
    private final Vector2 rightCenter;
    private final Vector2 upCenter;
    private final Vector2 downCenter;
    private final Vector2 centerCenter;
    private final Vector2 pauseCenter;
    private final Vector2 shootCenter;
    private final Vector2 jumpCenter;
    private GigaGal gigaGal;
    private int leftPointer;
    private int rightPointer;
    private int upPointer;
    private int downPointer;
    private int pausePointer;
    private int jumpPointer;
    private int shootPointer;
    public boolean leftButtonPressed;
    public boolean rightButtonPressed;
    public boolean upButtonPressed;
    public boolean downButtonPressed;
    public boolean jumpButtonPressed;
    public boolean shootButtonPressed;
    public boolean pauseButtonPressed;
    public boolean leftButtonJustPressed;
    public boolean rightButtonJustPressed;
    public boolean upButtonJustPressed;
    public boolean downButtonJustPressed;
    public boolean jumpButtonJustPressed;
    public boolean shootButtonJustPressed;
    public boolean pauseButtonJustPressed;

    // default ctor
    public InputControls() {
        this.viewport = new ExtendViewport(
                Constants.ONSCREEN_CONTROLS_VIEWPORT_SIZE,
                Constants.ONSCREEN_CONTROLS_VIEWPORT_SIZE);

        leftCenter = new Vector2();
        rightCenter = new Vector2();
        upCenter = new Vector2();
        downCenter = new Vector2();
        centerCenter = new Vector2();
        pauseCenter = new Vector2();
        shootCenter = new Vector2();
        jumpCenter = new Vector2();

        // TODO: fix button positions
        recalculateButtonPositions();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        Vector2 viewportPosition = viewport.unproject(new Vector2(screenX, screenY));


        if (viewportPosition.dst(jumpCenter) < Constants.BUTTON_RADIUS) {
            // : Save the jumpPointer and set jumpButtonPressed = true
            this.jumpPointer = pointer;
            jumpButtonPressed = true;
        } else if (viewportPosition.dst(leftCenter) < Constants.BUTTON_RADIUS) {
            // : Save the leftPointer, and set leftButtonPressed = true
            this.leftPointer = pointer;
            leftButtonPressed = true;
        } else if (viewportPosition.dst(rightCenter) < Constants.BUTTON_RADIUS) {
            // : Save the rightPointer, and set rightButtonPressed = true
            this.rightPointer = pointer;
            rightButtonPressed = true;
        } else if (viewportPosition.dst(upCenter) < Constants.BUTTON_RADIUS) {
            // : Save the upPointer, and set upButtonPressed = true
            this.upPointer = pointer;
            upButtonPressed = true;
        }  else if (viewportPosition.dst(downCenter) < Constants.BUTTON_RADIUS) {
            // : Save the downPointer, and set downButtonPressed = true
            this.downPointer = pointer;
            downButtonPressed = true;
        }  else if (viewportPosition.dst(pauseCenter) < Constants.BUTTON_RADIUS) {
            // : Save the pausePointer, and set gigaGal.pauseButtonPressed = true
            this.pausePointer = pointer;
            pauseButtonPressed = true;
        }
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector2 viewportPosition = viewport.unproject(new Vector2(screenX, screenY));

        if (pointer == leftPointer && viewportPosition.dst(rightCenter) < Constants.BUTTON_RADIUS) {

            // : Handle the case where the left button touch has been dragged to the right button
            // Inform GigaGal that the left button is no longer pressed
            leftButtonPressed = false;
            rightButtonPressed = true;
            // Inform GigaGal that the right button is now pressed
        
            // Zero leftPointer
            leftPointer = 0;
            // Save rightPointer
            rightPointer = pointer;
        }

        if (pointer == rightPointer && viewportPosition.dst(leftCenter) < Constants.BUTTON_RADIUS) {

            // : Handle the case where the right button touch has been dragged to the left button
            rightButtonPressed = false;
            leftButtonPressed = true;
            
            // Inform GigaGal that the right button is now pressed

            // Zero leftPointer
            rightPointer = 0;
            // Save rightPointer
            leftPointer = pointer;
        }

        if (pointer == upPointer && viewportPosition.dst(downCenter) < Constants.BUTTON_RADIUS) {

            // : Handle the case where the up button touch has been dragged to the down button
            // Inform GigaGal that the up button is no longer pressed
            upButtonPressed = false;
            downButtonPressed = true;
            // Inform GigaGal that the down button is now pressed

            // Zero upPointer
            upPointer = 0;
            // Save downPointer
            downPointer = pointer;
        }

        if (pointer == downPointer && viewportPosition.dst(upCenter) < Constants.BUTTON_RADIUS) {

            // : Handle the case where the down button touch has been dragged to the up button
            downButtonPressed = false;
            upButtonPressed = true;

            // Inform GigaGal that the down button is now pressed

            // Zero upPointer
            downPointer = 0;
            // Save downPointer
            upPointer = pointer;
        }

        if (pointer == shootPointer && viewportPosition.dst(shootCenter) < Constants.BUTTON_RADIUS) {
            shootButtonPressed = true;
        }

        if (pointer == pausePointer && viewportPosition.dst(pauseCenter) < Constants.BUTTON_RADIUS) {
            pauseButtonPressed = true;
        }

        return super.touchDragged(screenX, screenY, pointer);
    }

    public void update() {
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            upButtonPressed = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
            downButtonPressed = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            leftButtonPressed = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            rightButtonPressed = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.BACKSLASH)) {
            jumpButtonPressed = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            shootButtonPressed = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            pauseButtonPressed = true;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            upButtonJustPressed = true;
        } else {
            upButtonJustPressed = false;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            downButtonJustPressed = true;
        } else {
            downButtonJustPressed = false;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            leftButtonJustPressed = true;
        } else {
            leftButtonJustPressed = false;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            rightButtonJustPressed = true;
        } else {
            rightButtonJustPressed = false;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSLASH)) {
            jumpButtonJustPressed = true;
        } else {
            jumpButtonJustPressed = false;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            shootButtonJustPressed = true;
        } else {
            shootButtonJustPressed = false;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            pauseButtonJustPressed = true;
        } else {
            pauseButtonJustPressed = false;
        }
    }

    public void render(SpriteBatch batch) {

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        if (!Gdx.input.isTouched(shootPointer)) {
            shootButtonPressed = false;
            shootPointer = 0;
        }

        // : If the leftPointer is no longer touched, inform GigaGal and zero leftPointer
        if (!Gdx.input.isTouched(leftPointer)) {
            leftButtonPressed = false;
        }
        // : Do the same for rightPointer
        if (!Gdx.input.isTouched(rightPointer)) {
            rightButtonPressed = false;
        }

        // : If the upPointer is no longer touched, inform GigaGal and zero upPointer
        if (!Gdx.input.isTouched(upPointer)) {
            upButtonPressed = false;
        }
        // : Do the same for downPointer
        if (!Gdx.input.isTouched(downPointer)) {
            downButtonPressed = false;
        }
        // : Do the same for pausePointer
        if (!Gdx.input.isTouched(pausePointer)) {
            pauseButtonPressed = false;
        }

        if (!Gdx.input.isKeyJustPressed(jumpPointer)) {
            jumpButtonPressed = false;
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
        Utils.drawTextureRegion(
                batch,
                Assets.getInstance().getOnscreenControlsAssets().shoot,
                shootCenter,
                Constants.BUTTON_CENTER
        );
        Utils.drawTextureRegion(
             batch,
             Assets.getInstance().getOnscreenControlsAssets().jump,
             jumpCenter,
             Constants.BUTTON_CENTER
        );
        Utils.drawTextureRegion(
                batch,
                Assets.getInstance().getOnscreenControlsAssets().pause,
                pauseCenter,
                Constants.BUTTON_CENTER
        );

        batch.end();
    }

    public void recalculateButtonPositions() {
        leftCenter.set(Constants.BUTTON_RADIUS * .975f, Constants.BUTTON_RADIUS * 1.25f);
        rightCenter.set(Constants.BUTTON_RADIUS * 2.55f, Constants.BUTTON_RADIUS * 1.25f);
        upCenter.set(Constants.BUTTON_RADIUS * 1.7f, Constants.BUTTON_RADIUS * 2.1f);
        downCenter.set(Constants.BUTTON_RADIUS * 1.7f, Constants.BUTTON_RADIUS * .54f);
        centerCenter.set(Constants.BUTTON_RADIUS * 1.74f, Constants.BUTTON_RADIUS * 1.3125f);
        pauseCenter.set(viewport.getWorldWidth() / 2 - 6.25f, Constants.BUTTON_RADIUS * .8f);
        jumpCenter.set(
                viewport.getWorldWidth() - Constants.BUTTON_RADIUS * 2.5f,
                Constants.BUTTON_RADIUS
        );
        shootCenter.set(
                viewport.getWorldWidth() - Constants.BUTTON_RADIUS,
                Constants.BUTTON_RADIUS
        );
    }

    public final Viewport getViewport() { return viewport; }
}
