package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.Level;
import com.udacity.gamedev.gigagal.overlays.PauseOverlay;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Utils;

public class Rollen implements DestructibleHazard {

    // fields
    private Level level;
    private Vector2 position;
    private Enums.Direction lateralDirection;
    private Enums.Direction verticalDirection;
    private Enums.WeaponType type;
    private Vector2 velocity;
    private long startTime;
    private int health;
    private boolean grounded;
    private Enums.AerialState aerialState;
    private float speedAtChangeFacing;
    private long rollStartTime;
    private float rollTimeSeconds;
    private Array<Ground> grounds;

    // ctor
    public Rollen(Level level, Vector2 position, Enums.WeaponType type) {
        this.level = level;
        this.type = type;
        this.position = position;
        lateralDirection = null;
        verticalDirection = null;
        speedAtChangeFacing = 0;
        rollStartTime = 0;
        rollTimeSeconds = 0;
        velocity = new Vector2(0, 0);
        health = Constants.ROLLEN_MAX_HEALTH;
        grounds = level.getGrounds();
    }

    public void update(float delta) {
        position.x += velocity.x;
        position.y += velocity.y;

        Viewport viewport = level.getViewport();
        Vector2 worldSpan = new Vector2(viewport.getWorldWidth(), viewport.getWorldHeight());
        Vector3 camera = new Vector3(viewport.getCamera().position);
        Vector2 activationDistance = new Vector2(worldSpan.x / 2, worldSpan.y / 2);
        
        if (lateralDirection != null) {
            if (rollStartTime == 0) {
                speedAtChangeFacing = velocity.x;
                rollStartTime = TimeUtils.nanoTime();
            }
            rollTimeSeconds = Utils.secondsSince(rollStartTime);
            velocity.x = speedAtChangeFacing + Utils.absoluteToDirectionalValue(Math.min(Constants.ROLLEN_MOVEMENT_SPEED * rollTimeSeconds / 2, Constants.ROLLEN_MOVEMENT_SPEED * 2), lateralDirection, Enums.Orientation.LATERAL);
        }

        grounded = false;
        for (Ground ground : grounds) {
            if (Utils.equilateralWithinBounds(ground, position.x, getWidth() / 2)) {
                aerialState = Enums.AerialState.GROUNDED;
                position.y = ground.getTop();
                grounded = true;
            }
        }
        if (grounded) {
            velocity.y = 0;
            if ((position.x < camera.x - activationDistance.x)
                || (position.x > camera.x + activationDistance.x)) {
                lateralDirection = null;
                startTime = 0;
            } else if ((position.x > camera.x - activationDistance.x) && (position.x < camera.x)) {
                lateralDirection = Enums.Direction.RIGHT;
            } else if ((position.x > camera.x) && (position.x < camera.x + activationDistance.x)) {
                lateralDirection = Enums.Direction.LEFT;
            }
        } else {
            aerialState = Enums.AerialState.FALLING;
            velocity.y = -Constants.GRAVITY / 10;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        final TextureRegion region;
        switch (type) {
            case ELECTRIC:
                region = Assets.getInstance().getRollenAssets().chargedRollen.getKeyFrame(rollTimeSeconds, true);
                break;
            case FIRE:
                region = Assets.getInstance().getRollenAssets().fieryRollen.getKeyFrame(rollTimeSeconds, true);
                break;
            case METAL:
                region = Assets.getInstance().getRollenAssets().sharpRollen.getKeyFrame(rollTimeSeconds, true);
                break;
            case RUBBER:
                region = Assets.getInstance().getRollenAssets().whirlingRollen.getKeyFrame(rollTimeSeconds, true);
                break;
            case WATER:
                region = Assets.getInstance().getRollenAssets().gushingRollen.getKeyFrame(rollTimeSeconds, true);
                break;
            default:
                region = null;
        }
        Utils.drawTextureRegion(batch, region, position, Constants.ROLLEN_CENTER);
    }

    @Override public Vector2 getPosition() { return position; }
    @Override public final int getHealth() { return health; }
    @Override public final float getWidth() { return Constants.ROLLEN_COLLISION_WIDTH; }
    @Override public final float getHeight() { return Constants.ROLLEN_COLLISION_HEIGHT; }
    @Override public final float getLeft() { return position.x - Constants.ROLLEN_CENTER.x; }
    @Override public final float getRight() { return position.x + Constants.ROLLEN_CENTER.x; }
    @Override public final float getTop() { return position.y + Constants.ROLLEN_CENTER.y; }
    @Override public final float getBottom() { return position.y - Constants.ROLLEN_CENTER.y; }
    @Override public final float getShotRadius() { return Constants.ROLLEN_SHOT_RADIUS; }
    @Override public final int getHitScore() { return Constants.ROLLEN_HIT_SCORE; }
    @Override public final int getKillScore() { return Constants.ROLLEN_KILL_SCORE; }
    @Override public final int getDamage() { return Constants.ROLLEN_STANDARD_DAMAGE; }
    @Override public final Vector2 getKnockback() { return Constants.ROLLEN_KNOCKBACK; }
    @Override public final void setHealth( int health ) { this.health = health; }
    @Override public final Enums.WeaponType getType() { return type; }
    public final long getStartTime() { return startTime; }
}