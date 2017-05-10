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
        damage = Constants.PROTRUSION_LIQUID_DAMAGE;
        switch (type) {
            case ORE:
                animation = ImageLoader.getInstance().getProtrusionAssets().oreProtrusion;
                center.set(Constants.PROTRUSION_ORE_CENTER);
                collisionSpan.set(Constants.PROTRUSION_ORE_COLLISION_WIDTH, Constants.PROTRUSION_ORE_COLLISION_HEIGHT);
                knockback.set(Constants.PROTRUSION_ORE_KNOCKBACK);
                damage = Constants.PROTRUSION_ORE_DAMAGE;
                break;
            case PLASMA:
                animation = ImageLoader.getInstance().getProtrusionAssets().plasmaProtrusion;
                center.set(Constants.PROTRUSION_PLASMA_CENTER);
                collisionSpan.set(Constants.PROTRUSION_PLASMA_COLLISION_WIDTH, Constants.PROTRUSION_PLASMA_COLLISION_HEIGHT);
                knockback.set(Constants.PROTRUSION_PLASMA_KNOCKBACK);
                damage = Constants.PROTRUSION_PLASMA_DAMAGE;
                break;
            case GAS:
                animation = ImageLoader.getInstance().getProtrusionAssets().gasProtrusion;
                center.set(Constants.PROTRUSION_GAS_CENTER);
                collisionSpan.set(Constants.PROTRUSION_GAS_COLLISION_WIDTH, Constants.PROTRUSION_GAS_COLLISION_HEIGHT);
                knockback.set(Constants.PROTRUSION_GAS_KNOCKBACK);
                damage = Constants.PROTRUSION_GAS_DAMAGE;
                break;
            case LIQUID:
                animation = ImageLoader.getInstance().getProtrusionAssets().liquidProtrusion;
                center.set(Constants.PROTRUSION_LIQUID_CENTER);
                collisionSpan.set(Constants.PROTRUSION_LIQUID_COLLISION_WIDTH, Constants.PROTRUSION_LIQUID_COLLISION_HEIGHT);
                knockback.set(Constants.PROTRUSION_LIQUID_KNOCKBACK);
                damage = Constants.PROTRUSION_LIQUID_DAMAGE;
                break;
            case SOLID:
                animation = ImageLoader.getInstance().getProtrusionAssets().solidProtrustion;
                center.set(Constants.PROTRUSION_SOLID_CENTER);
                collisionSpan.set(Constants.PROTRUSION_SOLID_COLLISION_WIDTH, Constants.PROTRUSION_SOLID_COLLISION_HEIGHT);
                knockback.set(Constants.PROTRUSION_SOLID_KNOCKBACK);
                damage = Constants.PROTRUSION_SOLID_DAMAGE;
                break;
            default:
                animation = ImageLoader.getInstance().getProtrusionAssets().oreProtrusion;
                center.set(Constants.PROTRUSION_ORE_CENTER);
                collisionSpan.set(Constants.PROTRUSION_ORE_COLLISION_WIDTH, Constants.PROTRUSION_ORE_COLLISION_HEIGHT);
                knockback.set(Constants.PROTRUSION_ORE_KNOCKBACK);
                damage = Constants.PROTRUSION_ORE_DAMAGE;
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
