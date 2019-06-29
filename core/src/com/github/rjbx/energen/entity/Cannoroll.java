package com.github.rjbx.energen.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.rjbx.energen.app.LevelAssets;
import com.github.rjbx.energen.util.AssetManager;
import com.github.rjbx.energen.util.Constants;
import com.github.rjbx.energen.util.Enums;
import com.github.rjbx.energen.util.Helpers;

public class Cannoroll extends Ground implements Energized, Orientable, Roving, Strikeable, Convertible, Impermeable {

    // fields
    public final static String TAG = Lift.class.getName();
    private Vector2 position;
    private Enums.Direction direction;
    private Enums.Orientation orientation;
    private Vector2 velocity; // class-level instantiation
    private final Vector2 startPosition; // class-level instantiation
    private Vector2 center;
    private float range;
    private float speed;
    private boolean converted;
    private Enums.ShotIntensity intensity;
    private long startTime;
    private Animation<TextureRegion> animation;
    private boolean canDispatch;
    private boolean active;

    // ctor
    public Cannoroll(Vector2 position, Enums.Orientation orientation, Enums.ShotIntensity intensity, float range, boolean active) {
        this.position = position;
        center = new Vector2();
        converted = false;
        velocity = new Vector2();
        startPosition = new Vector2(position);
        startTime = TimeUtils.nanoTime();
        this.range = range;
        speed = Math.min(80, range * .8f);
        updateOrientation(orientation);
        this.intensity = intensity;
        canDispatch = false;
        this.active = active;
    }

    public Cannoroll safeClone() {
        Cannoroll clone = new Cannoroll(position, orientation, intensity, range, active);
        clone.setClonedHashCode(hashCode());
        return clone;
    }

    @Override
    public void update(float delta) {
        canDispatch = false;
        if (this.getStartTime() == 0) {
            this.setStartTime(TimeUtils.nanoTime());
        }
        if ((Helpers.secondsSince(this.getStartTime()) > 1.5f)) {
            this.setStartTime(TimeUtils.nanoTime());
            canDispatch = true;
        }
        boolean encompassed = false;
        for (Ground ground : LevelAssets.getClonedGrounds()) {
            encompassed = Helpers.encompassesPhysicalObject(ground, this);
            if (encompassed) break;
        }
        if (!encompassed && orientation == Enums.Orientation.X) {
            position.mulAdd(velocity, delta);
            if (position.x < (startPosition.x - (range / 2))) {
                position.x = startPosition.x - (range / 2);
                updateDirection(Enums.Direction.RIGHT);
            } else if (position.x > (startPosition.x + (range / 2))) {
                position.x = startPosition.x + (range / 2);
                updateDirection(Enums.Direction.LEFT);
            }
        }
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        Helpers.drawTextureRegion(batch, viewport, animation.getKeyFrame(Helpers.secondsSince(startTime), true), position, center);
    }

    @Override public final Vector2 getPosition() { return position; }
    @Override public final Vector2 getVelocity() { return velocity; }
    public final void setVelocity(Vector2 velocity) { this.velocity.set(velocity); }
    @Override public final float getHeight() { return center.y * 2; }
    @Override public final float getWidth() { return center.x * 2; }
    @Override public final float getLeft() { return position.x - center.x; }
    @Override public final float getRight() { return position.x + center.x; }
    @Override public final float getTop() { return position.y + center.y; }
    @Override public final float getBottom() { return position.y - center.y; }
    @Override public Enums.Orientation getOrientation() { return orientation; }
    @Override public Enums.Direction getDirectionX() { return direction; }
    @Override public void setDirectionX(Enums.Direction direction) { updateDirection(direction); }
    public void setDirection(Enums.Direction direction) { updateDirection(direction); }
    @Override public final boolean isDense() { return true; }
    @Override public void convert() { converted = !converted; position.add(-center.x, -center.y); updateOrientation(Helpers.getOppositeOrientation(orientation)); position.add(center.x, center.y); startTime = TimeUtils.nanoTime(); }
    @Override public boolean isConverted() { return converted; }
    public final void setRange(float range) { this.range = range; }
    public final long getStartTime() { return startTime; }
    public final void setStartTime(long startTime) { this.startTime = startTime; }
    @Override public final boolean getDispatchStatus() { return canDispatch; }
    @Override public final Enums.ShotIntensity getIntensity() { return intensity; }
    private void updateOrientation(Enums.Orientation orientation) {
        this.orientation = orientation;
        switch (orientation) {
            case Y:
                updateDirection(Enums.Direction.UP);
                center.set(Constants.Y_CANIROL_CENTER);
                break;
            case X:
                updateDirection(Enums.Direction.LEFT);
                center.set(Constants.X_CANIROL_CENTER);
                break;
            default:
                direction = Enums.Direction.UP;
        }
    }
    private void updateDirection(Enums.Direction direction) {
        this.direction = direction;
        switch (direction) {
            case LEFT:
                animation = AssetManager.getInstance().getCanirolAssets().xLeftCanirol;
                velocity.set(-speed, 0);
                break;
            case RIGHT:
                animation = AssetManager.getInstance().getCanirolAssets().xRightCanirol;
                velocity.set(speed, 0);
                break;
            case UP:
                animation = AssetManager.getInstance().getCanirolAssets().yCanirol;
                velocity.setZero();
                break;
        }
        animation.setFrameDuration(Constants.CANIROL_FRAME_DURATION * (40 / speed));
    }

    @Override
    public int getPriority() {
        return Constants.PRIORITY_MEDIUM;
    }
}