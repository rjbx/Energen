package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Utils;

public class Lift implements Ground, Moving {

    // fields
    private Vector2 position;
    private Enums.Direction direction;
    private final float startingY;
    private float velocityY;

    // ctor
    public Lift(Vector2 position) {
        this.position = position;
        direction = Enums.Direction.UP;
        startingY = position.y;
        velocityY = Constants.LIFT_SPEED;
    }

    public void update(float delta) {
        switch (direction) {
            case UP:
                velocityY = Constants.LIFT_SPEED * delta;
                break;
            case DOWN:
                velocityY = -Constants.LIFT_SPEED * delta;
                break;
        }

        position.y += velocityY;

        if (position.y < (startingY - (Constants.LIFT_RANGE / 2))) {
            position.y = startingY - (Constants.LIFT_RANGE / 2);
            direction = Enums.Direction.UP;
        } else if (position.y > (startingY + (Constants.LIFT_RANGE / 2))) {
            position.y = startingY + (Constants.LIFT_RANGE / 2);
            direction = Enums.Direction.DOWN;
        }
    }

    @Override
    public Enums.Direction getDirection() {
        return direction;
    }

    public void render(SpriteBatch batch) {
        final TextureRegion region = Assets.getInstance().getLiftAssets().lift;
        Utils.drawTextureRegion(batch, region, position, Constants.LIFT_CENTER);
    }

    public final Vector2 getPosition() { return position; }
    public final float getHeight() { return Constants.LIFT_CENTER.y * 2; }
    public final float getWidth() { return Constants.LIFT_CENTER.x * 2; }
    public final float getLeft() { return position.x - Constants.LIFT_CENTER.x; }
    public final float getRight() { return position.x + Constants.LIFT_CENTER.x; }
    public final float getTop() { return position.y + Constants.LIFT_CENTER.y; }
    public final float getBottom() { return position.y - Constants.LIFT_CENTER.y; }
    public final Class getSubclass() { return this.getClass(); }
    public final float getVelocityY() { return velocityY; }
}