package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.Level;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Utils;
import com.badlogic.gdx.graphics.g2d.Animation;

public class Rollen implements DestructibleHazard {

    // fields
    private Level level;
    private Vector2 position;
    private Vector2 previousFramePosition;
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
    private float radius;

    // ctor
    public Rollen(Level level, Vector2 position, Enums.WeaponType type) {
        this.level = level;
        this.type = type;
        this.position = position;
        this.previousFramePosition = new Vector2();
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
        previousFramePosition = position;
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
            velocity.x = speedAtChangeFacing + Utils.absoluteToDirectionalValue(Math.min(Constants.ROLLEN_MOVEMENT_SPEED * rollTimeSeconds / 10, Constants.ROLLEN_MOVEMENT_SPEED), lateralDirection, Enums.Orientation.LATERAL);
        }

        grounded = false;
        boolean bumpingSide = false;
        for (Ground ground : grounds) {
            if (Utils.equilateralWithinBounds(ground, position.x, position.y - getWidth() / 2, 0)) {
                aerialState = Enums.AerialState.GROUNDED;
                float groundTop = ground.getTop();
                grounded = true;
                if (!(Utils.equilateralWithinBounds(ground, position.x, position.y - getWidth() / 2, 0))) {
                /*
                if (!(position.x < Utils.absoluteToDirectionalValue(groundTop - (getWidth() / 2), lateralDirection, Enums.Orientation.LATERAL))) {
                    position.y = groundTop + getHeight() / 2;
                } else {
                    velocity.x = 0;
                    bumpingSide = true;
                }*/
            }
        }
        if (grounded) {
            velocity.y = 0;
            if ((position.x < camera.x - activationDistance.x)
                    || (position.x > camera.x + activationDistance.x)
                    || bumpingSide) {
                velocity.x = 0;
                startTime = 0;
                lateralDirection = null;
            } else if ((position.x > camera.x - activationDistance.x) && (position.x < camera.x)) {
                lateralDirection = Enums.Direction.RIGHT;
            } else if ((position.x > camera.x) && (position.x < camera.x + activationDistance.x)) {
                lateralDirection = Enums.Direction.LEFT;
            }
        } else {
            aerialState = Enums.AerialState.FALLING;
            velocity.y = -Constants.GRAVITY / 2;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        final TextureRegion region;
        final Animation animation;
        switch (type) {
            case ELECTRIC:
                animation = Assets.getInstance().getRollenAssets().chargedRollen;
                break;
            case FIRE:
                animation = Assets.getInstance().getRollenAssets().fieryRollen;
                break;
            case METAL:
                animation = Assets.getInstance().getRollenAssets().sharpRollen;
                break;
            case RUBBER:
                animation = Assets.getInstance().getRollenAssets().whirlingRollen;
                break;
            case WATER:
                animation = Assets.getInstance().getRollenAssets().gushingRollen;
                break;
            default:
                animation = Assets.getInstance().getRollenAssets().whirlingRollen;
        }
        if (lateralDirection == Enums.Direction.RIGHT) {
            animation.setPlayMode(Animation.PlayMode.REVERSED);
        } else {
            animation.setPlayMode(Animation.PlayMode.NORMAL);
        }
        region = animation.getKeyFrame(rollTimeSeconds, true);
        Utils.drawTextureRegion(batch, region, position, Constants.ROLLEN_CENTER, Constants.ROLLEN_TEXTURE_SCALE);
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