package com.github.rjbx.energen.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
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

public class Orben extends Hazard implements Roving, Aerial, Destructible, Nonstatic, Impermeable {

    // fields
    public final static String TAG = Orben.class.getName();

    private Vector2 position;
    private Vector2 previousFramePosition;
    private Enums.Direction xDirection;
    private Enums.Direction yDirection;
    private Enums.Energy type;
    private Vector2 velocity; // class-level instantiation
    private boolean canDispatch;
    private long startTime;
    private float health;
    private boolean active;
    private Animation<TextureRegion> animation;

    // ctor
    public Orben(Vector2 position, Enums.Energy type) {
        this.type = type;
        this.position = position;
        this.previousFramePosition = new Vector2();
        xDirection = null;
        yDirection = null;
        velocity = new Vector2(0, 0);
        canDispatch = false;
        health = Constants.ORBEN_MAX_HEALTH;
        switch (type) {
            case ORE:
                animation = AssetManager.getInstance().getOrbenAssets().oreOrben;
                break;
            case PLASMA:
                animation = AssetManager.getInstance().getOrbenAssets().plasmaOrben;
                break;
            case GAS:
                animation = AssetManager.getInstance().getOrbenAssets().gasOrben;
                break;
            case LIQUID:
                animation = AssetManager.getInstance().getOrbenAssets().liquidOrben;
                break;
            case SOLID:
                animation = AssetManager.getInstance().getOrbenAssets().solidOrben;
                break;
            default:
                animation = AssetManager.getInstance().getOrbenAssets().oreOrben;
        }
    }

    public Orben safeClone() {
        Orben clone = new Orben(position, type);
        clone.setClonedHashCode(hashCode());
        return clone;
    }

    public void update(float delta) {
        move(delta);
        shoot();
    }

    private void move(float delta) {
        previousFramePosition.set(position);
        position.x += velocity.x;
        position.y += velocity.y;

        Viewport viewport = ChaseCam.getInstance().getViewport();
        Vector2 worldSpan = new Vector2(viewport.getWorldWidth(), viewport.getWorldHeight());
        Vector3 camera = new Vector3(viewport.getCamera().position);
        Vector2 activationDistance = new Vector2(worldSpan.x / 4, worldSpan.y / 4);

        if ((position.x < camera.x - activationDistance.x)
                || (position.x > camera.x + activationDistance.x)) {
            xDirection = null;
        } else if ((position.x > camera.x - activationDistance.x) && (position.x < camera.x)) {
            xDirection = Enums.Direction.RIGHT;
        } else if ((position.x > camera.x) && (position.x < camera.x + activationDistance.x)) {
            xDirection = Enums.Direction.LEFT;
        }

        if ((position.y < camera.y - activationDistance.y)
                || (position.y > camera.y + activationDistance.y)) {
            yDirection = null;
        } else if ((position.y > camera.y - activationDistance.y) && (position.y < camera.y)) {
            yDirection = Enums.Direction.UP;
        } else if ((position.y > camera.y) && (position.y < camera.y + activationDistance.y)) {
            yDirection = Enums.Direction.DOWN;
        }

        if (xDirection != null && yDirection != null) {
            active = true;
        } else {
            startTime = TimeUtils.nanoTime();
            velocity.x = 0;
            velocity.y = 0;
            active = false;
        }

        if (active) {
            boolean colliding = true;
            for (Hazard hazard : LevelAssets.getClonedHazards()) {
                if (Helpers.overlapsPhysicalObject(this, hazard) && !hazard.equals(this)) {
                    colliding = false;
                }
            }

            for (Ground ground : LevelAssets.getClonedGrounds()) {
                if (ground.isDense()) {
                    if (Helpers.overlapsPhysicalObject(this, ground)) {
                        colliding = false;
                    }
                }
            }

            if (colliding) {
                switch (xDirection) {
                    case LEFT:
                        velocity.x = -Constants.ORBEN_MOVEMENT_SPEED * delta;
                        break;
                    case RIGHT:
                        velocity.x = Constants.ORBEN_MOVEMENT_SPEED * delta;
                        break;
                }

                switch (yDirection) {
                    case DOWN:
                        velocity.y = -Constants.ORBEN_MOVEMENT_SPEED * delta;
                        break;
                    case UP:
                        velocity.y = Constants.ORBEN_MOVEMENT_SPEED * delta;
                        break;
                }
            } else {
                position.set(previousFramePosition);
                velocity.x *= -1;
                velocity.y *= -1;
            }
        }
    }

    private void shoot() {
        canDispatch = false;
        float secondsSinceModOne = Helpers.secondsSince(this.getStartTime()) % 1;
        if ((secondsSinceModOne >= 0 && secondsSinceModOne < 0.01f) && this.isActive()) {
           canDispatch = true;
        }
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        final TextureRegion region;
        if (xDirection == null || yDirection == null) {
            region = AssetManager.getInstance().getOrbenAssets().dormantOrben;
        } else {
            region = animation.getKeyFrame(Helpers.secondsSince(startTime), true);
        }
        Helpers.drawTextureRegion(batch, viewport, region, position, Constants.ORBEN_CENTER, Constants.ORBEN_TEXTURE_SCALE);
    }

    @Override public Vector2 getPosition() { return position; }
    @Override public Vector2 getVelocity() { return velocity; }
    @Override public final float getHealth() { return health; }
    @Override public final float getWidth() { return Constants.ORBEN_COLLISION_WIDTH; }
    @Override public final float getHeight() { return Constants.ORBEN_COLLISION_HEIGHT; }
    @Override public final float getLeft() { return position.x - Constants.ORBEN_CENTER.x; }
    @Override public final float getRight() { return position.x + Constants.ORBEN_CENTER.x; }
    @Override public final float getTop() { return position.y + Constants.ORBEN_CENTER.y; }
    @Override public final float getBottom() { return position.y - Constants.ORBEN_CENTER.y; }
    @Override public final float getShotRadius() { return Constants.ORBEN_SHOT_RADIUS; }
    @Override public final int getHitScore() { return Constants.ORBEN_HIT_SCORE; }
    @Override public final int getKillScore() { return Constants.ORBEN_KILL_SCORE; }
    @Override public final int getDamage() { return Constants.ORBEN_STANDARD_DAMAGE; }
    @Override public final Vector2 getKnockback() { return Constants.ORBEN_KNOCKBACK; }
    @Override public final void setHealth( float health ) { this.health = health; }
    @Override public Enums.Energy getType() { return type; }
    @Override public Enums.Direction getDirectionX() { return xDirection; }
    @Override public Enums.Direction getDirectionY() { return yDirection; }
    @Override public void setDirectionX(Enums.Direction direction) { xDirection = direction; }
    @Override public void setDirectionY(Enums.Direction direction) { yDirection = direction; }
    public final boolean getDispatchStatus() { return canDispatch; }
    public final long getStartTime() { return startTime; }
    public final boolean isActive() { return active; }

    @Override
    public int getPriority() {
        return Constants.PRIORITY_MEDIUM;
    }
}