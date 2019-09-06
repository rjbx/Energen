package com.github.rjbx.energen.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.rjbx.energen.overlay.TouchInterface;

// mutable
public class InputControls extends InputAdapter {

    // fields
    public static final String TAG = InputControls.class.getName();
    private static final InputControls INSTANCE = new InputControls();
    private Viewport viewport;
    public Vector2 leftCenter;
    public Vector2 rightCenter;
    public Vector2 upCenter;
    public Vector2 downCenter;
    public Vector2 centerCenter;
    public Vector2 pauseCenter;
    public Vector2 shootCenter;
    public Vector2 jumpCenter;
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

    // cannot be subclassed
    private InputControls() {}

    // static factory method
    public static InputControls getInstance() { return INSTANCE; }

    public void create() {
        TouchInterface.getInstance().create();
        this.viewport = TouchInterface.getInstance().getViewport();

        leftCenter = new Vector2();
        rightCenter = new Vector2();
        upCenter = new Vector2();
        downCenter = new Vector2();
        centerCenter = new Vector2();
        pauseCenter = new Vector2();
        shootCenter = new Vector2();
        jumpCenter = new Vector2();
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
            // : Save the pausePointer, and set avatar.pauseButtonPressed = true
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
            // Inform avatar that the left button is no longer pressed
            leftButtonPressed = false;
            rightButtonPressed = true;
            // Inform avatar that the right button is now pressed

            // Zero leftPointer
            leftPointer = 0;
            // Save rightPointer
            rightPointer = pointer;
        }

        if (pointer == rightPointer && viewportPosition.dst(leftCenter) < Constants.TOUCH_RADIUS) {

            // : Handle the case where the right button touch has been dragged to the left button
            rightButtonPressed = false;
            leftButtonPressed = true;

            // Inform avatar that the right button is now pressed

            // Zero leftPointer
            rightPointer = 0;
            // Save rightPointer
            leftPointer = pointer;
        }

        if (pointer == upPointer && viewportPosition.dst(downCenter) < Constants.TOUCH_RADIUS) {

            // : Handle the case where the up button touch has been dragged to the down button
            // Inform avatar that the up button is no longer pressed
            upButtonPressed = false;
            downButtonPressed = true;
            // Inform avatar that the down button is now pressed

            // Zero upPointer
            upPointer = 0;
            // Save downPointer
            downPointer = pointer;
        }

        if (pointer == downPointer && viewportPosition.dst(upCenter) < Constants.TOUCH_RADIUS) {

            // : Handle the case where the down button touch has been dragged to the up button
            downButtonPressed = false;
            upButtonPressed = true;

            // Inform avatar that the down button is now pressed

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
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            downButtonPressed = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            leftButtonPressed = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            rightButtonPressed = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.COMMA)) {
            jumpButtonPressed = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.PERIOD)) {
            shootButtonPressed = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            pauseButtonPressed = true;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            upButtonJustPressed = true;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            downButtonJustPressed = true;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            leftButtonJustPressed = true;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            rightButtonJustPressed = true;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.COMMA)) {
            jumpButtonJustPressed = true;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.PERIOD)) {
            shootButtonJustPressed = true;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            pauseButtonJustPressed = true;
        }


        if (!Gdx.input.isTouched(shootPointer) && !Gdx.input.isKeyPressed(Input.Keys.PERIOD)) {
            shootButtonPressed = false;
        }
        if (!Gdx.input.isKeyJustPressed(shootPointer) && !Gdx.input.isKeyJustPressed(Input.Keys.PERIOD)) {
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
        if (!Gdx.input.isTouched(rightPointer) && !Gdx.input.isKeyPressed(Input.Keys.D)) {
            rightButtonPressed = false;
        }
        if (!Gdx.input.isKeyJustPressed(rightPointer) && !Gdx.input.isKeyJustPressed(Input.Keys.D)) {
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

        if (!Gdx.input.isTouched(downPointer) && !Gdx.input.isKeyPressed(Input.Keys.S)) {
            downButtonPressed = false;
        }
        if (!Gdx.input.isKeyJustPressed(downPointer) && !Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            downButtonJustPressed = false;
            downPointer = 0;
        }

        if (!Gdx.input.isTouched(pausePointer) && !Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            pauseButtonPressed = false;
        }
        if (!Gdx.input.isKeyJustPressed(pausePointer) && !Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            pauseButtonJustPressed = false;
            pausePointer = 0;
        }

        if (!Gdx.input.isTouched(jumpPointer) && !Gdx.input.isKeyPressed(Input.Keys.COMMA)) {
            jumpButtonPressed = false;
        }
        if (!Gdx.input.isKeyJustPressed(jumpPointer) && !Gdx.input.isKeyJustPressed(Input.Keys.COMMA)) {
            jumpButtonJustPressed = false;
            jumpPointer = 0;
        }
    }

    public void clear() {
         leftButtonPressed = false;
         rightButtonPressed = false;
         upButtonPressed = false;
         downButtonPressed = false;
         jumpButtonPressed = false;
         shootButtonPressed = false;
         pauseButtonPressed = false;
         leftButtonJustPressed = false;
         rightButtonJustPressed = false;
         upButtonJustPressed = false;
         downButtonJustPressed = false;
         jumpButtonJustPressed = false;
         shootButtonJustPressed = false;
         pauseButtonJustPressed = false;
    }

    public final boolean hasInput() {
        return
            leftButtonPressed
            || rightButtonPressed
            || upButtonPressed
            || downButtonPressed
            || jumpButtonPressed
            || shootButtonPressed
            || pauseButtonPressed
            || leftButtonJustPressed
            || rightButtonJustPressed
            || upButtonJustPressed
            || downButtonJustPressed
            || jumpButtonJustPressed
            || shootButtonJustPressed
            || pauseButtonJustPressed;
    }

    public final boolean justSelected() {
        boolean select =  shootButtonJustPressed || pauseButtonJustPressed;
        shootButtonJustPressed = false;
        pauseButtonJustPressed = false;
        return select;
    }
}
