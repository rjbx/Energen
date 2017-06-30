package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Helpers;

public class Lift extends Ground implements Roving, Convertible {

    // fields
    public final static String TAG = Lift.class.getName();

    private Vector2 position;
    private Enums.Direction direction;
    private Enums.Orientation orientation;
    private Vector2 velocity; // class-level instantiation
    private final Vector2 startPosition; // class-level instantiation
    private float range;
    private boolean converted;

    // ctor
    public Lift(Vector2 position, Enums.Orientation orientation, float range) {
        this.position = position;
        setOrientation(orientation);
        converted = false;
        velocity = new Vector2();
        startPosition = new Vector2(position);
        this.range = range;
    }

    @Override
    public void update(float delta) {
        switch (orientation) {
            case Y:
                switch (direction) {
                    case UP:
                        velocity.set(0, Constants.LIFT_SPEED * Gdx.graphics.getDeltaTime());
                        break;
                    case DOWN:
                        velocity.set(0, -Constants.LIFT_SPEED * Gdx.graphics.getDeltaTime());
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
                        velocity.set(Constants.LIFT_SPEED * Gdx.graphics.getDeltaTime(), 0);
                        break;
                    case LEFT:
                        velocity.set(-Constants.LIFT_SPEED * Gdx.graphics.getDeltaTime(), 0);
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
    public final void setPosition(Vector2 position) { this.position = position; }
    @Override public final Vector2 getVelocity() { return velocity; }
    public final void setVelocity(Vector2 velocity) { this.velocity.set(velocity); }
    @Override public final float getHeight() { return Constants.LIFT_CENTER.y * 2; }
    @Override public final float getWidth() { return Constants.LIFT_CENTER.x * 2; }
    @Override public final float getLeft() { return position.x - Constants.LIFT_CENTER.x; }
    @Override public final float getRight() { return position.x + Constants.LIFT_CENTER.x; }
    @Override public final float getTop() { return position.y + Constants.LIFT_CENTER.y; }
    @Override public final float getBottom() { return position.y - Constants.LIFT_CENTER.y; }
    @Override public Enums.Direction getDirection() { return direction; }
    public void setDirection(Enums.Direction direction) { this.direction = direction; }
    @Override public Enums.Orientation getOrientation() { return orientation; }
    @Override public final boolean isDense() { return false; }
    @Override public void convert() { converted = !converted; position.set(startPosition); setOrientation(Helpers.getOppositeOrientation(orientation)); }
    @Override public boolean isConverted() { return converted; }
    public final void setRange(float range) { this.range = range; }
    private void setOrientation(Enums.Orientation orientation) {
        this.orientation = orientation;
        switch (orientation) {
            case Y:
                direction = Enums.Direction.DOWN;
                break;
            case X:
                direction = Enums.Direction.LEFT;
                break;
            default:
                direction = null;
        }
    }
}