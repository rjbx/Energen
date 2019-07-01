package com.github.rjbx.energen.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.rjbx.energen.app.LevelAssets;
import com.github.rjbx.energen.util.AssetManager;
import com.github.rjbx.energen.util.ChaseCam;
import com.github.rjbx.energen.util.Constants;
import com.github.rjbx.energen.util.Enums;
import com.github.rjbx.energen.util.Helpers;
import com.badlogic.gdx.graphics.g2d.Animation;

public class Rollen extends Hazard implements Destructible, Roving, Impermeable, Gravitating {

    // fields
    public final static String TAG = Rollen.class.getName();

    private Vector2 position;
    private Vector2 previousFramePosition; // class-level instantiationa
    private Enums.Direction xDirection;
    private Enums.Energy type;
    private Vector2 velocity; // class-level instantiation
    private float collision;
    private long startTime;
    private float health;
    private float speedAtChangeXDirection;
    private long rollStartTime;
    private float rollTimeSeconds;
    private float radius;
    private Animation<TextureRegion> animation;

    // ctor
    public Rollen(Vector2 position, Enums.Energy type) {
        this.type = type;
        this.position = position;
        previousFramePosition = new Vector2();
        velocity = new Vector2(0, 0);
        radius = getWidth() / 2;
        health = Constants.ROLLEN_MAX_HEALTH;
        xDirection = null;
        speedAtChangeXDirection = 0;
        rollStartTime = 0;
        rollTimeSeconds = 0;
        collision = rollTimeSeconds;
        switch (type) {
            case ORE:
                animation = AssetManager.getInstance().getRollenAssets().oreRollen;
                break;
            case PLASMA:
                animation = AssetManager.getInstance().getRollenAssets().plasmaRollen;
                break;
            case GAS:
                animation = AssetManager.getInstance().getRollenAssets().gasRollen;
                break;
            case LIQUID:
                animation = AssetManager.getInstance().getRollenAssets().liquidRollen;
                break;
            case SOLID:
                animation = AssetManager.getInstance().getRollenAssets().solidRollen;
                break;
            default:
                animation = AssetManager.getInstance().getRollenAssets().oreRollen;
        }
    }

    @Override
    public Rollen safeClone() {
        Rollen clone = new Rollen(position, type);
        clone.setClonedHashCode(hashCode());
        return clone;
    }

    public void update(float delta) {
        previousFramePosition.set(position);
        position.mulAdd(velocity, delta);

        Viewport viewport = ChaseCam.getInstance().getViewport();
        Vector2 worldSpan = new Vector2(viewport.getWorldWidth(), viewport.getWorldHeight());
        Vector3 camera = new Vector3(viewport.getCamera().position);
        Vector2 activationDistance = new Vector2(worldSpan.x / 1.5f, worldSpan.y / 1.5f);

        boolean touchingSide = false;
        boolean touchingTop = false;
        boolean canSink = false;
        for (Ground ground : LevelAssets.getClonedGrounds()) {
            if (Helpers.overlapsPhysicalObject(this, ground)) {
                if (ground instanceof Pourous) {
                    canSink = true;
                }
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
                    if (!canSink) {
                        touchingTop = true;
                    }
                }
            }
        }

        if (touchingTop) {
            velocity.y = 0;
            position.y = previousFramePosition.y;
            if (Helpers.betweenFourValues(position, camera.x - activationDistance.x, camera.x + activationDistance.x, camera.y - activationDistance.y, camera.y + activationDistance.y)) {
                if ((position.x >= camera.x - activationDistance.x) && (position.x < camera.x)) {
                    xDirection = Enums.Direction.RIGHT;
                } else if ((position.x < camera.x + activationDistance.x) && (position.x >= camera.x)) {
                    xDirection = Enums.Direction.LEFT;
                }
            } else {
                xDirection = null;
                startTime = 0;
                velocity.x = 0;
            }

            if (xDirection != null) {
                if (rollStartTime == 0) {
                    speedAtChangeXDirection = velocity.x;
                    rollStartTime = TimeUtils.nanoTime();
                }
                rollTimeSeconds = Helpers.secondsSince(rollStartTime);
                velocity.x = speedAtChangeXDirection + Helpers.speedToVelocity(Math.min(Constants.ROLLEN_MOVEMENT_SPEED * rollTimeSeconds, Constants.ROLLEN_MOVEMENT_SPEED), xDirection, Enums.Orientation.X);
            }
            for (Hazard hazard : LevelAssets.getClonedHazards()) {
                if (hazard instanceof Rollen && Helpers.overlapsPhysicalObject(this, hazard) && !(hazard.equals(this))) {
                    position.set(previousFramePosition);
                    if (!touchingSide && position.x < hazard.getPosition().x) {
                        velocity.x -= 5;
                    } else {
                        velocity.x += 5;
                    }
                }
            }
        } else {
            if (!canSink) {
                velocity.y -= Constants.GRAVITY;
            } else {
                velocity.y = -8;
            }
        }

        if (touchingSide) {
            xDirection = null;
            startTime = 0;
            velocity.x = 0;
            position.x = previousFramePosition.x;
            rollStartTime = TimeUtils.nanoTime();
            rollTimeSeconds = 0;
            if (canSink) {
                velocity.y = -5;
            }
        }
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        if (xDirection == Enums.Direction.RIGHT) {
            animation.setPlayMode(Animation.PlayMode.REVERSED);
        } else {
            animation.setPlayMode(Animation.PlayMode.NORMAL);
        }

        Helpers.drawTextureRegion(batch, viewport, animation.getKeyFrame(rollTimeSeconds, true), position, Constants.ROLLEN_CENTER, Constants.ROLLEN_TEXTURE_SCALE);
    }

    @Override public Vector2 getPosition() { return position; }
    public Vector2 getVelocity() { return velocity; }
    @Override public final float getHealth() { return health; }
    @Override public final float getWidth() { return Constants.ROLLEN_CENTER.x * 2; }
    @Override public final float getHeight() { return Constants.ROLLEN_CENTER.y * 2; }
    @Override public final float getLeft() { return position.x - Constants.ROLLEN_CENTER.x; }
    @Override public final float getRight() { return position.x + Constants.ROLLEN_CENTER.x; }
    @Override public final float getTop() { return position.y + Constants.ROLLEN_CENTER.y; }
    @Override public final float getBottom() { return position.y - Constants.ROLLEN_CENTER.y; }
    @Override public final float getShotRadius() { return Constants.ROLLEN_SHOT_RADIUS; }
    @Override public final int getHitScore() { return Constants.ROLLEN_HIT_SCORE; }
    @Override public final int getKillScore() { return Constants.ROLLEN_KILL_SCORE; }
    @Override public final int getDamage() { return Constants.ROLLEN_STANDARD_DAMAGE; }
    @Override public final Vector2 getKnockback() { return Constants.ROLLEN_KNOCKBACK; }
    @Override public final void setHealth( float health ) { this.health = health; }
    @Override public final Enums.Energy getType() { return type; }
    @Override public Enums.Direction getDirectionX() { return xDirection; }
    @Override public void setDirectionX(Enums.Direction direction) { xDirection = direction; }
    public final long getStartTime() { return startTime; }

    @Override
    public int getPriority() {
        return Constants.PRIORITY_MEDIUM;
    }
}