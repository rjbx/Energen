package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Utils;

public class Lift implements HoverableGround {

    // fields
    private Vector2 position;
    private Enums.Direction direction;
    private Enums.Orientation orientation;
    private Vector2 velocity;
    private final Vector2 startPosition;

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
                if (position.y < (startPosition.y - (Constants.LIFT_RANGE / 2))) {
                    position.y = startPosition.y - (Constants.LIFT_RANGE / 2);
                    direction = Enums.Direction.UP;
                } else if (position.y > (startPosition.y + (Constants.LIFT_RANGE / 2))) {
                    position.y = startPosition.y + (Constants.LIFT_RANGE / 2);
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
                if (position.x < (startPosition.x - (Constants.LIFT_RANGE / 2))) {
                    position.x = startPosition.x - (Constants.LIFT_RANGE / 2);
                    direction = Enums.Direction.RIGHT;
                } else if (position.x > (startPosition.x + (Constants.LIFT_RANGE / 2))) {
                    position.x = startPosition.x + (Constants.LIFT_RANGE / 2);
                    direction = Enums.Direction.LEFT;
                }
                break;
            default:
                switch (direction) {
                    case UP:
                        velocity.set(0, Constants.LIFT_SPEED * delta);
                        break;
                    case DOWN:
                        velocity.set(0, -Constants.LIFT_SPEED * delta);
                        break;
                }
                position.add(velocity);
                if (position.y < (startPosition.y - (Constants.LIFT_RANGE / 2))) {
                    position.y = startPosition.y - (Constants.LIFT_RANGE / 2);
                    direction = Enums.Direction.UP;
                } else if (position.y > (startPosition.y + (Constants.LIFT_RANGE / 2))) {
                    position.y = startPosition.y + (Constants.LIFT_RANGE / 2);
                    direction = Enums.Direction.DOWN;
                }
                break;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        final TextureRegion region = Assets.getInstance().getLiftAssets().lift;
        Utils.drawTextureRegion(batch, region, position, Constants.LIFT_CENTER);
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
}