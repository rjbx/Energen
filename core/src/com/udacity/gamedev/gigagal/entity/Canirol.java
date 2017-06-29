package com.udacity.gamedev.gigagal.entity;


import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.app.LevelUpdater;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Helpers;

public class Canirol extends Hazard implements Indestructible, MultidirectionalX, Nonstatic, Convertible {
    // fields
    public final static String TAG = Cannon.class.getName();
    private Vector2 position;
    private Vector2 center;
    private Enums.Orientation orientation;
    private Enums.ShotIntensity intensity;
    private TextureRegion region;
    private long startTime;
    private boolean active;
    private boolean canDispatch;
    private LevelUpdater level;
    private Vector2 previousFramePosition; // class-level instantiation
    private Enums.Direction xDirection;
    private Enums.Material type;
    private Vector2 velocity; // class-level instantiation
    private float collision;
    private float health;
    private float speedAtChangeXDirection;
    private long rollStartTime;
    private float rollTimeSeconds;
    private float radius;
    private Animation animation;

    // ctor
    public Canirol(Vector2 position, Enums.Orientation orientation, Enums.ShotIntensity intensity, boolean active) {
        this.position = position;
        this.orientation = orientation;
        this.intensity = intensity;
        startTime = 0;
        canDispatch = false;
        this.active = active;
        switch (orientation) {
            case Y:
                region = Assets.getInstance().getGroundAssets().yCannon;
                center = Constants.Y_CANNON_CENTER;
                break;
            case X:
                region = Assets.getInstance().getGroundAssets().xCannon;
                center = Constants.X_CANNON_CENTER;
                break;
        }
    }

    public void update(float delta) {
        canDispatch = false;
        if (active) {
            if (this.getStartTime() == 0) {
                this.setStartTime(TimeUtils.nanoTime());
            }
            if ((Helpers.secondsSince(this.getStartTime()) > 1.5f)) {
                this.setStartTime(TimeUtils.nanoTime());
                canDispatch = true;
            }
        }

        previousFramePosition.set(position);
        position.mulAdd(velocity, delta);

        Viewport viewport = level.getViewport();
        Vector2 worldSpan = new Vector2(viewport.getWorldWidth(), viewport.getWorldHeight());
        Vector3 camera = new Vector3(viewport.getCamera().position);
        Vector2 activationDistance = new Vector2(worldSpan.x / 1.5f, worldSpan.y / 1.5f);

        boolean touchingSide = false;
        boolean touchingTop = false;
        for (Ground ground : LevelUpdater.getInstance().getGrounds()) {
            if (Helpers.overlapsPhysicalObject(this, ground)) {
                if (ground.isDense()) {
                    if (Helpers.overlapsBetweenTwoSides(position.x, radius, ground.getLeft(), ground.getRight())
                            && !(Helpers.overlapsBetweenTwoSides(previousFramePosition.x, radius, ground.getLeft(), ground.getRight()))) {
                        touchingSide = true;
                        if (position.x < ground.getPosition().x) {
                            velocity.x -= 5;
                        } else {
                            velocity.x += 5;
                        }
                    }
                }
                if (Helpers.overlapsBetweenTwoSides(position.y, radius, ground.getBottom(), ground.getTop())
                        && !(Helpers.overlapsBetweenTwoSides(previousFramePosition.y, radius, ground.getBottom(), ground.getTop()))) {
                    touchingTop = true;
                }
            }
        }

        if (touchingTop) {
            velocity.y = 0;
            position.y = previousFramePosition.y;
            if ((position.x < camera.x - activationDistance.x)
                    || (position.x > camera.x + activationDistance.x)) {
                xDirection = null;
                startTime = 0;
                velocity.x = 0;
            } else if ((position.x >= camera.x - activationDistance.x) && (position.x < camera.x)) {
                xDirection = Enums.Direction.RIGHT;
            } else if ((position.x < camera.x + activationDistance.x) && (position.x >= camera.x)) {
                xDirection = Enums.Direction.LEFT;
            }

            if (xDirection != null) {
                if (rollStartTime == 0) {
                    speedAtChangeXDirection = velocity.x;
                    rollStartTime = TimeUtils.nanoTime();
                }
                rollTimeSeconds = Helpers.secondsSince(rollStartTime);
                velocity.x = speedAtChangeXDirection + Helpers.absoluteToDirectionalValue(Math.min(Constants.ROLLEN_MOVEMENT_SPEED * rollTimeSeconds, Constants.ROLLEN_MOVEMENT_SPEED), xDirection, Enums.Orientation.X);
            }
            for (Hazard hazard : LevelUpdater.getInstance().getHazards()) {
                if (hazard instanceof Rollen && Helpers.overlapsPhysicalObject(this, hazard)) {
                    position.set(previousFramePosition);
                    if (!touchingSide && position.x < hazard.getPosition().x) {
                        velocity.x -= 5;
                    } else {
                        velocity.x += 5;
                    }
                }
            }
        } else {
            velocity.y -= Constants.GRAVITY;
        }

        if (touchingSide) {
            xDirection = null;
            startTime = 0;
            velocity.x = 0;
            position.x = previousFramePosition.x;
            rollStartTime = TimeUtils.nanoTime();
            rollTimeSeconds = 0;
        }
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        Helpers.drawTextureRegion(batch, viewport, region, getPosition(), getCenter());
    }

    @Override public final Vector2 getPosition() { return position; }
    public final Vector2 getCenter() { return center; }
    @Override public final float getWidth() { return center.x * 2; }
    @Override public final float getHeight() { return center.y * 2; }
    @Override public final float getLeft() { return position.x - center.x; }
    @Override public final float getRight() { return position.x + center.x; }
    @Override public final float getTop() { return position.y + center.y; }
    @Override public final float getBottom() { return position.y - center.y; }
    @Override public void convert() { active = !active; }
    @Override public boolean isConverted() {  return active; }
    public final boolean getDispatchStatus() { return canDispatch; }
    public final Enums.Orientation getOrientation() { return orientation; }
    public final Enums.ShotIntensity getIntensity() { return intensity; }
    public final long getStartTime() { return startTime; }
    public final void setStartTime(long startTime) { this.startTime = startTime; }
    @Override public final int getDamage() { return Constants.ROLLEN_STANDARD_DAMAGE; }
    @Override public final Vector2 getKnockback() { return Constants.ROLLEN_KNOCKBACK; }
    @Override public final Enums.Material getType() { return type; }
    @Override public Enums.Direction getDirectionX() { return xDirection; }
    @Override public void setDirectionX(Enums.Direction direction) { xDirection = direction; }
}
