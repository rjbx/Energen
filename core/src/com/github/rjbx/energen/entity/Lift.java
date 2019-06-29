package com.github.rjbx.energen.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.rjbx.energen.util.AssetManager;
import com.github.rjbx.energen.util.Constants;
import com.github.rjbx.energen.util.Enums;
import com.github.rjbx.energen.util.Helpers;

public class Lift extends Ground implements Dynamic, Convertible {

    // fields
    public final static String TAG = Lift.class.getName();

    private Vector2 position;
    private Enums.Direction direction;
    private Enums.Orientation orientation;
    private Vector2 velocity; // class-level instantiation
    private final Vector2 startPosition; // class-level instantiation
    private float range;
    private float speed;
    private boolean converted;

    // ctor
    public Lift(Vector2 position, Enums.Orientation orientation, float range, float speed) {
        this.position = position;
        setOrientation(orientation);
        converted = false;
        velocity = new Vector2();
        startPosition = new Vector2(position);
        this.range = range;
        this.speed = speed;
    }

    public Lift safeClone() {
        Lift clone = new Lift(position, orientation, range, speed);
        clone.setClonedHashCode(hashCode());
        return clone;
    }

    @Override
    public void update(float delta) {
        switch (orientation) {
            case Y:
                switch (direction) {
                    case UP:
                        velocity.set(0, speed);
                        break;
                    case DOWN:
                        velocity.set(0, -speed);
                        break;
                }
                position.mulAdd(velocity, delta);
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
                        velocity.set(speed, 0);
                        break;
                    case LEFT:
                        velocity.set(-speed, 0);
                        break;
                }
                position.mulAdd(velocity, delta);
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
        Helpers.drawTextureRegion(batch, viewport, AssetManager.getInstance().getGroundAssets().lift, position, Constants.LIFT_CENTER);
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
    @Override public Enums.Direction getDirectionX() { if (orientation == Enums.Orientation.X) { return direction; } return null; }
    @Override public Enums.Direction getDirectionY() { if (orientation == Enums.Orientation.Y) { return direction; } return null; }
    @Override public void setDirectionX(Enums.Direction direction) { if (orientation == Enums.Orientation.X) { this.direction = direction; } }
    @Override public void setDirectionY(Enums.Direction direction) { if (orientation == Enums.Orientation.Y) { this.direction = direction; } }
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

    @Override
    public int getPriority() {
        return Constants.PRIORITY_HIGH;
    }
}