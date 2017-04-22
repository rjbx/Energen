package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Helpers;

public class Lift implements Hoverable, Ground {

    // fields
    private Vector2 position;
    private Enums.Direction direction;
    private Enums.Orientation orientation;
    private Vector2 velocity; // class-level instantiation
    private final Vector2 startPosition; // class-level instantiation
    private float range;

    // ctor
    public Lift(Vector2 position, Enums.Orientation orientation) {
        this.position = position;
        this.orientation = orientation;
        switch (orientation) {
            case Y:
                direction = Enums.Direction.UP;
                break;
            case X:
                direction = Enums.Direction.RIGHT;
                break;
            default:
                direction = Enums.Direction.UP;
        }
        velocity = new Vector2();
        startPosition = new Vector2(position);
        range = Constants.LIFT_RANGE;
    }

    @Override
    public void update(float delta) {
        switch (orientation) {
            case Y:
                switch (direction) {
                    case UP:
                        velocity.set(0, Constants.LIFT_SPEED * delta);
                        break;
                    case DOWN:
                        velocity.set(0, -Constants.LIFT_SPEED * delta);
                        break;
                }
                position.add(velocity);
                if (position.y < (startPosition.y - (range / 2))) {
                    position.y = startPosition.y - (range / 2);
                    direction = Enums.Direction.UP;
                } else if (position.y > (startPosition.y + (range / 2))) {
                    position.y = startPosition.y + (range / 2);
                    direction = Enums.Direction.DOWN;
                }
                break;
            case X:
                switch (direction) {
                    case RIGHT:
                        velocity.set(Constants.LIFT_SPEED * delta, 0);
                        break;
                    case LEFT:
                        velocity.set(-Constants.LIFT_SPEED * delta, 0);
                        break;
                }
                position.add(velocity);
                if (position.x < (startPosition.x - (range / 2))) {
                    position.x = startPosition.x - (range / 2);
                    direction = Enums.Direction.RIGHT;
                } else if (position.x > (startPosition.x + (range / 2))) {
                    position.x = startPosition.x + (range / 2);
                    direction = Enums.Direction.LEFT;
                }
                break;
        }
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        Helpers.drawTextureRegion(batch, viewport, Assets.getInstance().getGroundAssets().lift, position, Constants.LIFT_CENTER);
    }

    @Override public final Vector2 getPosition() { return position; }
    @Override public final Vector2 getVelocity() { return velocity; }
    @Override public final float getHeight() { return Constants.LIFT_CENTER.y * 2; }
    @Override public final float getWidth() { return Constants.LIFT_CENTER.x * 2; }
    @Override public final float getLeft() { return position.x - Constants.LIFT_CENTER.x; }
    @Override public final float getRight() { return position.x + Constants.LIFT_CENTER.x; }
    @Override public final float getTop() { return position.y + Constants.LIFT_CENTER.y; }
    @Override public final float getBottom() { return position.y - Constants.LIFT_CENTER.y; }
    @Override public Enums.Direction getDirection() { return direction; }
    @Override public Enums.Orientation getOrientation() { return orientation; }
    public final float getRange() { return range; }

    public final void setRange(float range) { this.range = range; }
}