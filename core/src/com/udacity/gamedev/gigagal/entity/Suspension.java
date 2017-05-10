package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.util.ImageLoader;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Helpers;

public class Suspension implements Indestructible, Hazard {

    // fields
    private Vector2 position;
    private Enums.Material type;
    private Vector2 collisionSpan; // class-level instantiation
    private Vector2 center; // class-level instantiation
    private Vector2 knockback; // class-level instantiation
    private Animation animation;
    private int damage;
    private long startTime;

    // ctor
    public Suspension(Vector2 position, Enums.Material type) {
        this.position = position;
        this.type = type;
        startTime = TimeUtils.nanoTime();
        collisionSpan = new Vector2();
        center = new Vector2();
        knockback = new Vector2();
        damage = Constants.PROTRUSION_LIQUID_DAMAGE;
        switch (type) {
            case ORE:
                animation = ImageLoader.getInstance().getSuspensionAssets().oreSuspension;
                center.set(Constants.SUSPENSION_ORE_CENTER);
                collisionSpan.set(Constants.SUSPENSION_ORE_COLLISION_WIDTH, Constants.SUSPENSION_ORE_COLLISION_HEIGHT);
                knockback.set(Constants.SUSPENSION_ORE_KNOCKBACK);
                damage = Constants.SUSPENSION_ORE_DAMAGE;
                break;
            case PLASMA:
                animation = ImageLoader.getInstance().getSuspensionAssets().plasmaSuspension;
                center.set(Constants.SUSPENSION_PLASMA_CENTER);
                collisionSpan.set(Constants.SUSPENSION_PLASMA_COLLISION_WIDTH, Constants.SUSPENSION_PLASMA_COLLISION_HEIGHT);
                knockback.set(Constants.SUSPENSION_PLASMA_KNOCKBACK);
                damage = Constants.SUSPENSION_PLASMA_DAMAGE;
                break;
            case GAS:
                animation = ImageLoader.getInstance().getSuspensionAssets().gasSuspension;
                center.set(Constants.SUSPENSION_GAS_CENTER);
                collisionSpan.set(Constants.SUSPENSION_GAS_COLLISION_WIDTH, Constants.SUSPENSION_GAS_COLLISION_HEIGHT);
                knockback.set(Constants.SUSPENSION_GAS_KNOCKBACK);
                damage = Constants.SUSPENSION_GAS_DAMAGE;
                break;
            case LIQUID:
                animation = ImageLoader.getInstance().getSuspensionAssets().liquidSuspension;
                center.set(Constants.SUSPENSION_LIQUID_CENTER);
                collisionSpan.set(Constants.SUSPENSION_LIQUID_COLLISION_WIDTH, Constants.SUSPENSION_LIQUID_COLLISION_HEIGHT);
                knockback.set(Constants.SUSPENSION_LIQUID_KNOCKBACK);
                damage = Constants.SUSPENSION_LIQUID_DAMAGE;
                break;
            case SOLID:
                animation = ImageLoader.getInstance().getSuspensionAssets().solidSuspension;
                center.set(Constants.SUSPENSION_SOLID_CENTER);
                collisionSpan.set(Constants.SUSPENSION_SOLID_COLLISION_WIDTH, Constants.SUSPENSION_SOLID_COLLISION_HEIGHT);
                knockback.set(Constants.SUSPENSION_SOLID_KNOCKBACK);
                damage = Constants.SUSPENSION_SOLID_DAMAGE;
                break;
            case ANTIMATTER:
                animation = ImageLoader.getInstance().getSuspensionAssets().antimatterSuspension;
                center.set(Constants.SUSPENSION_ANTIMATTER_CENTER);
                collisionSpan.set(Constants.SUSPENSION_ANTIMATTER_COLLISION_WIDTH, Constants.SUSPENSION_ANTIMATTER_COLLISION_HEIGHT);
                knockback.set(Constants.SUSPENSION_ANTIMATTER_KNOCKBACK);
                damage = Constants.SUSPENSION_ANTIMATTER_DAMAGE;
                break;
            default:
                animation = ImageLoader.getInstance().getSuspensionAssets().oreSuspension;
                center.set(Constants.SUSPENSION_ORE_CENTER);
                collisionSpan.set(Constants.SUSPENSION_ORE_COLLISION_WIDTH, Constants.SUSPENSION_ORE_COLLISION_HEIGHT);
                knockback.set(Constants.SUSPENSION_ORE_KNOCKBACK);
                damage = Constants.SUSPENSION_ORE_DAMAGE;
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
    @Override public final Enums.Material getType() { return type; }
}
