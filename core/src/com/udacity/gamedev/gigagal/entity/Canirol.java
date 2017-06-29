package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Helpers;

public class Canirol extends Ground implements Weaponized, Hoverable, Strikeable, Nonstatic, Convertible {

    // fields
    public final static String TAG = Lift.class.getName();
    private Vector2 position;
    private Enums.Direction direction;
    private Enums.Orientation orientation;
    private Vector2 velocity; // class-level instantiation
    private final Vector2 startPosition; // class-level instantiation
    private Vector2 center;
    private float range;
    private boolean converted;
    private Enums.ShotIntensity intensity;
    private long startTime;
    private Animation animation;
    private boolean canDispatch;
    private boolean active;

    // ctor
    public Canirol(Vector2 position, Enums.Orientation orientation, Enums.ShotIntensity intensity, float range, boolean active) {
        this.position = position;
        animation = Assets.getInstance().getCanirolAssets().xLeftCanirol;
        center = new Vector2();
        setOrientation(orientation);
        converted = false;
        velocity = new Vector2();
        startPosition = new Vector2(position);
        startTime = 0;
        this.range = range;
        this.intensity = intensity;
        canDispatch = false;
        this.active = active;
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
        switch (orientation) {
            case Y:
                velocity.setZero();
                animation = Assets.getInstance().getCanirolAssets().yCanirol;
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
                    animation = Assets.getInstance().getCanirolAssets().xRightCanirol;
                } else if (position.x > (startPosition.x + (range / 2))) {
                    position.x = startPosition.x + (range / 2);
                    direction = Enums.Direction.LEFT;
                    animation = Assets.getInstance().getCanirolAssets().xLeftCanirol;
                }
                break;
        }
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        Helpers.drawTextureRegion(batch, viewport, animation.getKeyFrame(Helpers.secondsSince(0)), position, center);
    }

    @Override public final Vector2 getPosition() { return position; }
    public final void setPosition(Vector2 position) { this.position = position; }
    @Override public final Vector2 getVelocity() { return velocity; }
    public final void setVelocity(Vector2 velocity) { this.velocity.set(velocity); }
    @Override public final float getHeight() { return center.y * 2; }
    @Override public final float getWidth() { return center.x * 2; }
    @Override public final float getLeft() { return position.x - center.x; }
    @Override public final float getRight() { return position.x + center.x; }
    @Override public final float getTop() { return position.y + center.y; }
    @Override public final float getBottom() { return position.y - center.y; }
    @Override public Enums.Direction getDirection() { return direction; }
    public void setDirection(Enums.Direction direction) { this.direction = direction; }
    @Override public Enums.Orientation getOrientation() { return orientation; }
    @Override public final boolean isDense() { return true; }
    @Override public void convert() { converted = !converted; setOrientation(Helpers.getOppositeOrientation(orientation)); }
    @Override public boolean isConverted() { return converted; }
    public final void setRange(float range) { this.range = range; }
    public final long getStartTime() { return startTime; }
    public final void setStartTime(long startTime) { this.startTime = startTime; }
    @Override public final boolean getDispatchStatus() { return canDispatch; }
    @Override public final Enums.ShotIntensity getIntensity() { return intensity; }
    private void setOrientation(Enums.Orientation orientation) {
        this.orientation = orientation;
        switch (orientation) {
            case Y:
                direction = null;
                center.set(Constants.Y_CANIROL_CENTER);
                animation = Assets.getInstance().getCanirolAssets().yCanirol;
                break;
            case X:
                direction = Enums.Direction.LEFT;
                center.set(Constants.X_CANIROL_CENTER);
                animation = Assets.getInstance().getCanirolAssets().xLeftCanirol;
                break;
            default:
                direction = null;
        }
    }
}