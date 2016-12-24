package com.udacity.gamedev.gigagal.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.entities.GigaGal;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
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


        if (viewportPosition.dst(jumpCenter) < Constants.TOUCH_RADIUS) {
            // : Save the jumpPointer and set jumpButtonPressed = true
            this.jumpPointer = pointer;
            jumpButtonPressed = true;
            jumpButtonJustPressed = true;
        } else if (viewportPosition.dst(shootCenter) < Constants.TOUCH_RADIUS) {
            // : Save the shootPointer and set shootButtonPressed = true
            this.shootPointer = pointer;
            shootButtonPressed = true;
            shootButtonJustPressed = true;
        } else if (viewportPosition.dst(leftCenter) < Constants.TOUCH_RADIUS) {
            // : Save the leftPointer, and set leftButtonPressed = true
            this.leftPointer = pointer;
            leftButtonPressed = true;
            leftButtonJustPressed = true;
        } else if (viewportPosition.dst(rightCenter) < Constants.TOUCH_RADIUS) {
            // : Save the rightPointer, and set rightButtonPressed = true
            this.rightPointer = pointer;
            rightButtonPressed = true;
            rightButtonJustPressed = true;
        } else if (viewportPosition.dst(upCenter) < Constants.TOUCH_RADIUS) {
            // : Save the upPointer, and set upButtonPressed = true
            this.upPointer = pointer;
            upButtonPressed = true;
            upButtonJustPressed = true;
        }  else if (viewportPosition.dst(downCenter) < Constants.TOUCH_RADIUS) {
            // : Save the downPointer, and set downButtonPressed = true
            this.downPointer = pointer;
            downButtonPressed = true;
            downButtonJustPressed = true;
        }  else if (viewportPosition.dst(pauseCenter) < Constants.TOUCH_RADIUS) {
            // : Save the pausePointer, and set gigaGal.pauseButtonPressed = true
            this.pausePointer = pointer;
            pauseButtonPressed = true;
            pauseButtonJustPressed = true;
        }
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector2 viewportPosition = viewport.unproject(new Vector2(screenX, screenY));

        if (pointer == leftPointer && viewportPosition.dst(rightCenter) < Constants.TOUCH_RADIUS) {

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

        if (pointer == rightPointer && viewportPosition.dst(leftCenter) < Constants.TOUCH_RADIUS) {

            // : Handle the case where the right button touch has been dragged to the left button
            rightButtonPressed = false;
            leftButtonPressed = true;
            
            // Inform GigaGal that the right button is now pressed

            // Zero leftPointer
            rightPointer = 0;
            // Save rightPointer
            leftPointer = pointer;
        }

        if (pointer == upPointer && viewportPosition.dst(downCenter) < Constants.TOUCH_RADIUS) {

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

        if (pointer == downPointer && viewportPosition.dst(upCenter) < Constants.TOUCH_RADIUS) {

            // : Handle the case where the down button touch has been dragged to the up button
            downButtonPressed = false;
            upButtonPressed = true;

            // Inform GigaGal that the down button is now pressed

            // Zero upPointer
            downPointer = 0;
            // Save downPointer
            upPointer = pointer;
        }

        if (pointer == jumpPointer && viewportPosition.dst(jumpCenter) < Constants.TOUCH_RADIUS) {
            jumpButtonPressed = true;
        }

        if (pointer == shootPointer && viewportPosition.dst(shootCenter) < Constants.TOUCH_RADIUS) {
            shootButtonPressed = true;
        }

        if (pointer == pausePointer && viewportPosition.dst(pauseCenter) < Constants.TOUCH_RADIUS) {
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
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            downButtonJustPressed = true;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            leftButtonJustPressed = true;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            rightButtonJustPressed = true;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSLASH)) {
            jumpButtonJustPressed = true;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            shootButtonJustPressed = true;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            pauseButtonJustPressed = true;
        }
    }

    public void render(SpriteBatch batch) {

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        if (!Gdx.input.isTouched(shootPointer) && !Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            shootButtonPressed = false;
        }
        if (!Gdx.input.isKeyJustPressed(shootPointer) && !Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            shootButtonJustPressed = false;
            shootPointer = 0;
        }
        if (!Gdx.input.isTouched(leftPointer) && !Gdx.input.isKeyPressed(Input.Keys.A)) {
            leftButtonPressed = false;
        }
        if (!Gdx.input.isKeyJustPressed(leftPointer) && !Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            leftButtonJustPressed = false;
            leftPointer = 0;
        }
        if (!Gdx.input.isTouched(rightPointer) && !Gdx.input.isKeyPressed(Input.Keys.S)) {
            rightButtonPressed = false;
        }
        if (!Gdx.input.isKeyJustPressed(rightPointer) && !Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            rightButtonJustPressed = false;
            rightPointer = 0;
        }

        if (!Gdx.input.isTouched(upPointer) && !Gdx.input.isKeyPressed(Input.Keys.W)) {
            upButtonPressed = false;
        }
        if (!Gdx.input.isKeyJustPressed(upPointer) && !Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            upButtonJustPressed = false;
            upPointer = 0;
        }

        if (!Gdx.input.isTouched(downPointer) && !Gdx.input.isKeyPressed(Input.Keys.Z)) {
            downButtonPressed = false;
        }
        if (!Gdx.input.isKeyJustPressed(downPointer) && !Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            downButtonJustPressed = false;
            downPointer = 0;
        }

        if (!Gdx.input.isTouched(pausePointer) && !Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            pauseButtonPressed = false;
        }
        if (!Gdx.input.isKeyJustPressed(pausePointer) && !Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            pauseButtonJustPressed = false;
            pausePointer = 0;
        }

        if (!Gdx.input.isTouched(jumpPointer) && !Gdx.input.isKeyPressed(Input.Keys.BACKSLASH)) {
            jumpButtonPressed = false;
        }
        if (!Gdx.input.isKeyJustPressed(jumpPointer) && !Gdx.input.isKeyJustPressed(Input.Keys.BACKSLASH)) {
            jumpButtonJustPressed = false;
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
        leftCenter.set(Constants.POSITION_DIAMETER * .975f, Constants.POSITION_DIAMETER * 1.25f);
        rightCenter.set(Constants.POSITION_DIAMETER * 2.55f, Constants.POSITION_DIAMETER * 1.25f);
        upCenter.set(Constants.POSITION_DIAMETER * 1.7f, Constants.POSITION_DIAMETER * 2.1f);
        downCenter.set(Constants.POSITION_DIAMETER * 1.7f, Constants.POSITION_DIAMETER * .54f);
        centerCenter.set(Constants.POSITION_DIAMETER * 1.74f, Constants.POSITION_DIAMETER * 1.3125f);
        pauseCenter.set(viewport.getWorldWidth() / 2 - 6.25f, Constants.POSITION_DIAMETER * .8f);
        jumpCenter.set(
                viewport.getWorldWidth() - Constants.POSITION_DIAMETER * 2.5f,
                Constants.POSITION_DIAMETER
        );
        shootCenter.set(
                viewport.getWorldWidth() - Constants.POSITION_DIAMETER,
                Constants.POSITION_DIAMETER
        );
    }

    public final Viewport getViewport() { return viewport; }
}
