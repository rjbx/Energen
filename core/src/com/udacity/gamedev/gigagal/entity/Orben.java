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

public class Orben implements Destructible, Hazard {

    // fields
    private LevelUpdater level;
    private Vector2 position;
    private Vector2 previousFramePosition;
    private Enums.Direction xDirection;
    private Enums.Direction yDirection;
    private Enums.Material type;
    private Vector2 velocity; // class-level instantiation
    private long startTime;
    private float health;
    private boolean active;
    private Animation animation;

    // ctor
    public Orben(LevelUpdater level, Vector2 position, Enums.Material type) {
        this.level = level;
        this.type = type;
        this.position = position;
        this.previousFramePosition = new Vector2();
        xDirection = null;
        yDirection = null;
        velocity = new Vector2(0, 0);
        health = Constants.ORBEN_MAX_HEALTH;
        switch (type) {
            case ORE:
                animation = Assets.getInstance().getOrbenAssets().oreOrben;
                break;
            case PLASMA:
                animation = Assets.getInstance().getOrbenAssets().plasmaOrben;
                break;
            case GAS:
                animation = Assets.getInstance().getOrbenAssets().gasOrben;
                break;
            case LIQUID:
                animation = Assets.getInstance().getOrbenAssets().liquidOrben;
                break;
            case SOLID:
                animation = Assets.getInstance().getOrbenAssets().solidOrben;
                break;
            default:
                animation = Assets.getInstance().getOrbenAssets().oreOrben;
        }
    }

    public void update(float delta) {
        previousFramePosition.set(position);
        position.x += velocity.x;
        position.y += velocity.y;

        Viewport viewport = level.getViewport();
        Vector2 worldSpan = new Vector2(viewport.getWorldWidth(), viewport.getWorldHeight());
        Vector3 camera = new Vector3(viewport.getCamera().position);
        Vector2 activationDistance = new Vector2(worldSpan.x / 4, worldSpan.y / 4);

        if (xDirection != null) {
            switch (xDirection) {
                case LEFT:
                    velocity.x = -Constants.ORBEN_MOVEMENT_SPEED * delta;
                    break;
                case RIGHT:
                    velocity.x = Constants.ORBEN_MOVEMENT_SPEED * delta;
                    break;
            }
        } else {
            velocity.x = 0;
        }
        if ((position.x < camera.x - activationDistance.x)
        || (position.x > camera.x + activationDistance.x)) {
            xDirection = null;
        } else if ((position.x > camera.x - activationDistance.x) && (position.x < camera.x)) {
            xDirection = Enums.Direction.RIGHT;
        } else if ((position.x > camera.x) && (position.x < camera.x + activationDistance.x)) {
            xDirection = Enums.Direction.LEFT;
        }

        if (yDirection != null) {
            switch (yDirection) {
                case DOWN:
                    velocity.y = -Constants.ORBEN_MOVEMENT_SPEED * delta;
                    break;
                case UP:
                    velocity.y = Constants.ORBEN_MOVEMENT_SPEED * delta;
                    break;
            }
        } else {
            velocity.y = 0;
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
            active = false;
        }

        for (Ground ground : LevelUpdater.getInstance().getGrounds()) {
            if (ground instanceof Dense && !((Dense) ground).isLedge()) {
                if (Helpers.overlapsBetweenFourSides(position, getWidth(), getHeight(), ground.getLeft(), ground.getRight(), ground.getBottom(), ground.getTop())) {
                    velocity.setZero();
                    position.set(previousFramePosition);
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        final TextureRegion region;
        if (xDirection == null || yDirection == null) {
            region = Assets.getInstance().getOrbenAssets().dormantOrben;
        } else {
            region = animation.getKeyFrame(Helpers.secondsSince(startTime), true);
        }
        Helpers.drawTextureRegion(batch, viewport, region, position, Constants.ORBEN_CENTER, Constants.ORBEN_TEXTURE_SCALE);
    }

    @Override public Vector2 getPosition() { return position; }
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
    @Override public Enums.Material getType() { return type; }
    public final long getStartTime() { return startTime; }
    public final boolean isActive() { return active; }
}
