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

public class Protrusion implements Indestructible, Hazard {

    // fields
    private Vector2 position;
    private Enums.Material type;
    private Vector2 collisionSpan; // class-level instantiation
    private Vector2 center; // class-level instantiation
    private Vector2 knockback; // class-level instantiation
    private int damage;
    private long startTime;
    private Animation animation;

    // ctor
    public Protrusion(Vector2 position, Enums.Material type) {
        this.position = position;
        this.type = type;
        startTime = TimeUtils.nanoTime();
        collisionSpan = new Vector2();
        center = new Vector2();
        knockback = new Vector2();
        damage = Constants.GEISER_DAMAGE;
        switch (type) {
            case PLASMA:
                animation = Assets.getInstance().getProtrusionAssets().rod;
                center.set(Constants.ROD_CENTER);
                collisionSpan.set(Constants.ROD_COLLISION_WIDTH, Constants.ROD_COLLISION_HEIGHT);
                knockback.set(Constants.ROD_KNOCKBACK);
                damage = Constants.ROD_DAMAGE;
                break;
            case GAS:
                animation = Assets.getInstance().getProtrusionAssets().flame;
                center.set(Constants.FLAME_CENTER);
                collisionSpan.set(Constants.FLAME_COLLISION_WIDTH, Constants.FLAME_COLLISION_HEIGHT);
                knockback.set(Constants.FLAME_KNOCKBACK);
                damage = Constants.FLAME_DAMAGE;
                break;
            case LIQUID:
                animation = Assets.getInstance().getProtrusionAssets().geiser;
                center.set(Constants.GEISER_CENTER);
                collisionSpan.set(Constants.GEISER_COLLISION_WIDTH, Constants.GEISER_COLLISION_HEIGHT);
                knockback.set(Constants.GEISER_KNOCKBACK);
                damage = Constants.GEISER_DAMAGE;
                break;
            case SOLID:
                animation = Assets.getInstance().getProtrusionAssets().spike;
                center.set(Constants.SPIKE_CENTER);
                collisionSpan.set(Constants.SPIKE_COLLISION_WIDTH, Constants.SPIKE_COLLISION_HEIGHT);
                knockback.set(Constants.SPIKE_KNOCKBACK);
                damage = Constants.SPIKE_DAMAGE;
                break;
            default:
                animation = Assets.getInstance().getProtrusionAssets().geiser;
                center.set(Constants.GEISER_CENTER);
                collisionSpan.set(Constants.GEISER_COLLISION_WIDTH, Constants.GEISER_COLLISION_HEIGHT);
                knockback.set(Constants.GEISER_KNOCKBACK);
                damage = Constants.GEISER_DAMAGE;
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
