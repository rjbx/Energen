package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Helpers;

public class Suspension implements IndestructibleHazard {

    // fields
    private Vector2 position;
    private Enums.WeaponType type;
    private Vector2 collisionSpan; // class-level instantiation
    private Vector2 center; // class-level instantiation
    private Vector2 knockback; // class-level instantiation
    private Animation animation;
    private int damage;
    private long startTime;

    // ctor
    public Suspension(Vector2 position, Enums.WeaponType type) {
        this.position = position;
        this.type = type;
        startTime = TimeUtils.nanoTime();
        collisionSpan = new Vector2();
        center = new Vector2();
        knockback = new Vector2();
        damage = Constants.GEISER_DAMAGE;
        switch (type) {
            case PLASMA:
                animation = Assets.getInstance().getSuspensionAssets().coil;
                center.set(Constants.COIL_CENTER);
                collisionSpan.set(Constants.COIL_COLLISION_WIDTH, Constants.COIL_COLLISION_HEIGHT);
                knockback.set(Constants.COIL_KNOCKBACK);
                damage = Constants.COIL_DAMAGE;
                break;
            case GAS:
                animation = Assets.getInstance().getSuspensionAssets().burner;
                center.set(Constants.BURNER_CENTER);
                collisionSpan.set(Constants.BURNER_COLLISION_WIDTH, Constants.BURNER_COLLISION_HEIGHT);
                knockback.set(Constants.BURNER_KNOCKBACK);
                damage = Constants.BURNER_DAMAGE;
                break;
            case LIQUID:
                animation = Assets.getInstance().getSuspensionAssets().lump;
                center.set(Constants.LUMP_CENTER);
                collisionSpan.set(Constants.LUMP_COLLISION_WIDTH, Constants.LUMP_COLLISION_HEIGHT);
                knockback.set(Constants.LUMP_KNOCKBACK);
                damage = Constants.LUMP_DAMAGE;
                break;
            case SOLID:
                animation = Assets.getInstance().getSuspensionAssets().sharp;
                center.set(Constants.SHARP_CENTER);
                collisionSpan.set(Constants.SHARP_COLLISION_WIDTH, Constants.SHARP_COLLISION_HEIGHT);
                knockback.set(Constants.SHARP_KNOCKBACK);
                damage = Constants.SHARP_DAMAGE;
                break;
            case ANTIMATTER:
                animation = Assets.getInstance().getSuspensionAssets().vacuum;
                center.set(Constants.VACUUM_CENTER);
                collisionSpan.set(Constants.VACUUM_COLLISION_WIDTH, Constants.VACUUM_COLLISION_HEIGHT);
                knockback.set(Constants.VACUUM_KNOCKBACK);
                damage = Constants.VACUUM_DAMAGE;
                break;
            default:
                animation = Assets.getInstance().getSuspensionAssets().wheel;
                center.set(Constants.WHEEL_CENTER);
                collisionSpan.set(Constants.WHEEL_COLLISION_WIDTH, Constants.WHEEL_COLLISION_HEIGHT);
                knockback.set(Constants.WHEEL_KNOCKBACK);
                damage = Constants.WHEEL_DAMAGE;
                break;
        }
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        Helpers.drawTextureRegion(batch, viewport, animation.getKeyFrame(Helpers.secondsSince(startTime), true), position, center);
    }

    @Override public final Vector2 getPosition() { return position; }
    @Override public final float getWidth() { return collisionSpan.x; }
    @Override public final float getHeight() { return collisionSpan.y; }
    @Override public final float getLeft() { return position.x - center.x; }
    @Override public final float getRight() { return position.x + center.x; }
    @Override public final float getTop() { return position.y + center.y; }
    @Override public final float getBottom() { return position.y - center.y; }
    @Override public final int getDamage() { return damage; }
    @Override public final Vector2 getKnockback() { return knockback; }
    @Override public final Enums.WeaponType getType() { return type; }
}
