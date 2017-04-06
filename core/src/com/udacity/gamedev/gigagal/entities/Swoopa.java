package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.udacity.gamedev.gigagal.app.Level;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Utils;

// mutable
public class Swoopa implements DestructibleHazard {

    // fields
    private final long startTime;
    private final float bobOffset;
    private Level level;
    private Vector2 velocity;
    private Vector2 position;
    private Enums.WeaponType type;
    private final Vector2 startingPosition;
    private float health;
    private long descentStartTime;

    // ctor
    public Swoopa(Level level, Vector2 position, Enums.WeaponType type) {
        this.level = level;
        this.position = position;
        this.type = type;
        startingPosition = new Vector2(position);
        velocity = new Vector2();
        startTime = TimeUtils.nanoTime();
        descentStartTime = 0;
        health = Constants.SWOOPA_MAX_HEALTH;
        bobOffset = MathUtils.random();
    }

    public void update(float delta) {
        Vector2 worldSpan = new Vector2(level.getViewport().getWorldWidth(), level.getViewport().getWorldHeight());
        Vector3 camera = new Vector3(level.getViewport().getCamera().position);
        // while the swoopa is witin a screens' width from the screen center on either side, permit movement
        if (position.x < (camera.x + worldSpan.x)
        && position.x > (camera.x - worldSpan.x)) {
            if (descentStartTime == 0) {
                descentStartTime = TimeUtils.nanoTime();
            }
            if (Utils.secondsSince(descentStartTime) < .75f) {
                velocity.x = Math.min(-20, velocity.x * 1.01f);
                velocity.y = Math.min(-Constants.SWOOPA_MOVEMENT_SPEED, velocity.y * 1.01f);
            } else {
                velocity.x = velocity.x * 1.035f;
                velocity.y = velocity.y / 1.035f;
            }
        }
        position = position.mulAdd(velocity, delta);

        // when the swoopa progresses past the center screen position with a margin of ten screen widths, reset x and y position
        if (position.x < (camera.x - (worldSpan.x * 10))) {
            descentStartTime = 0;
            position.x = (camera.x + worldSpan.x);
            position.y = (camera.y + worldSpan.y / 1.5f);
            velocity.set(0, 0);
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        final float elapsedTime = Utils.secondsSince(startTime);
        final TextureRegion region;
        switch (type) {
            case PLASMA:
                region = Assets.getInstance().getSwoopaAssets().chargedSwoopa.getKeyFrame(elapsedTime, true);
                break;
            case GAS:
                region = Assets.getInstance().getSwoopaAssets().fierySwoopa.getKeyFrame(elapsedTime, true);
                break;
            case SOLID:
                region = Assets.getInstance().getSwoopaAssets().sharpSwoopa.getKeyFrame(elapsedTime, true);
                break;
            case ORE:
                region = Assets.getInstance().getSwoopaAssets().whirlingSwoopa.getKeyFrame(elapsedTime, true);
                break;
            case LIQUID:
                region = Assets.getInstance().getSwoopaAssets().gushingSwoopa.getKeyFrame(elapsedTime, true);
                break;
            default:
                region = Assets.getInstance().getSwoopaAssets().swoopa;
        }
        Utils.drawTextureRegion(batch, region, position, Constants.SWOOPA_CENTER);
    }

    @Override public Vector2 getPosition() { return position; }
    @Override public final float getHealth() { return health; }
    @Override public final float getWidth() { return Constants.SWOOPA_COLLISION_WIDTH; }
    @Override public final float getHeight() { return Constants.SWOOPA_COLLISION_HEIGHT; }
    @Override public final float getLeft() { return position.x - Constants.SWOOPA_CENTER.x; }
    @Override public final float getRight() { return position.x + Constants.SWOOPA_CENTER.x; }
    @Override public final float getTop() { return position.y + Constants.SWOOPA_CENTER.y; }
    @Override public final float getBottom() { return position.y - Constants.SWOOPA_CENTER.y; }
    @Override public final float getShotRadius() { return Constants.SWOOPA_SHOT_RADIUS; }
    @Override public final int getHitScore() { return Constants.SWOOPA_HIT_SCORE; }
    @Override public final int getKillScore() { return Constants.SWOOPA_KILL_SCORE; }
    @Override public final int getDamage() { return Constants.SWOOPA_STANDARD_DAMAGE; }
    @Override public final Vector2 getKnockback() { return Constants.SWOOPA_KNOCKBACK; }
    @Override public Enums.WeaponType getType() { return type; }
    public int getMountDamage() { return Constants.SWOOPA_STANDARD_DAMAGE; }
    public Vector2 getMountKnockback() { return Constants.SWOOPA_KNOCKBACK; }
    public final long getStartTime() { return startTime; }

    @Override public final void setHealth( float health ) { this.health = health; }
}
