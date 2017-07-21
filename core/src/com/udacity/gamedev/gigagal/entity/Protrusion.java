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

public class Protrusion extends Hazard implements Indestructible, Convertible {

    // fields
    public final static String TAG = Protrusion.class.getName();

    private final Vector2 startPosition;
    private Vector2 position;
    private Animation animation;
    private Vector2 collisionSpan; // class-level instantiation
    private Vector2 center; // class-level instantiation
    private Vector2 knockback; // class-level instantiation
    private Enums.Material type;
    private final Vector2 offset;
    private final int rotation;
    private int damage;
    private long startTime;
    private boolean state;
    private boolean converted;

    // ctor
    public Protrusion(Vector2 position, Enums.Material type, float rotation) {
        this.position = position;
        startPosition = new Vector2(position);
        state = true;
        converted = false;
        this.type = type;
        this.rotation = (int) rotation;
        startTime = TimeUtils.nanoTime();
        collisionSpan = new Vector2();
        center = new Vector2();
        knockback = new Vector2();
        damage = Constants.PROTRUSION_LIQUID_DAMAGE;
        setTypeAttributes(type);
        switch (this.rotation) {
            case 90:
                offset = new Vector2(-center.x, center.y);
                break;
            case 180:
                offset = new Vector2(-center.x, -center.y);
                break;
            case 270:
                offset = new Vector2(center.x, -center.y);
                break;
            default:
                offset = center;
        }
    }

    @Override
    public void update(float delta) {
        if (state) {
            if (!position.equals(startPosition)) {
                position.set(startPosition);
                if (knockback.equals(Vector2.Zero)) {
                    setTypeAttributes(type);
                }
            }
        } else if (!knockback.equals(Vector2.Zero)) {
            knockback.setZero();
        }
        converted = false;
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        if (state) {
            Helpers.drawTextureRegion(batch, viewport, animation.getKeyFrame(Helpers.secondsSince(startTime), true), position, offset, 1, rotation);
        } else {
            Helpers.drawTextureRegion(batch, viewport, Assets.getInstance().getProtrusionAssets().inactiveProtrusion, startPosition.x, startPosition.y - center.y, Constants.PROTRUSION_INACTIVE_CENTER.x, Constants.PROTRUSION_INACTIVE_CENTER.y, 1, rotation);
        }
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
    public final long getStartTime() { return startTime; }
    public final void resetStartTime() { this.startTime = 0; }
    @Override public void convert() { state = !state; converted = true; }
    @Override public boolean isConverted() { return converted; }
    private final void setTypeAttributes(Enums.Material type) {
        switch (type) {
            case ORE:
                animation = Assets.getInstance().getProtrusionAssets().oreProtrusion;
                center.set(Constants.PROTRUSION_ORE_CENTER);
                collisionSpan.set(Constants.PROTRUSION_ORE_COLLISION_WIDTH, Constants.PROTRUSION_ORE_COLLISION_HEIGHT);
                knockback.set(Constants.PROTRUSION_ORE_KNOCKBACK);
                damage = Constants.PROTRUSION_ORE_DAMAGE;
                break;
            case PLASMA:
                animation = Assets.getInstance().getProtrusionAssets().plasmaProtrusion;
                center.set(Constants.PROTRUSION_PLASMA_CENTER);
                collisionSpan.set(Constants.PROTRUSION_PLASMA_COLLISION_WIDTH, Constants.PROTRUSION_PLASMA_COLLISION_HEIGHT);
                knockback.set(Constants.PROTRUSION_PLASMA_KNOCKBACK);
                damage = Constants.PROTRUSION_PLASMA_DAMAGE;
                break;
            case GAS:
                animation = Assets.getInstance().getProtrusionAssets().gasProtrusion;
                center.set(Constants.PROTRUSION_GAS_CENTER);
                collisionSpan.set(Constants.PROTRUSION_GAS_COLLISION_WIDTH, Constants.PROTRUSION_GAS_COLLISION_HEIGHT);
                knockback.set(Constants.PROTRUSION_GAS_KNOCKBACK);
                damage = Constants.PROTRUSION_GAS_DAMAGE;
                break;
            case LIQUID:
                animation = Assets.getInstance().getProtrusionAssets().liquidProtrusion;
                center.set(Constants.PROTRUSION_LIQUID_CENTER);
                collisionSpan.set(Constants.PROTRUSION_LIQUID_COLLISION_WIDTH, Constants.PROTRUSION_LIQUID_COLLISION_HEIGHT);
                knockback.set(Constants.PROTRUSION_LIQUID_KNOCKBACK);
                damage = Constants.PROTRUSION_LIQUID_DAMAGE;
                break;
            case SOLID:
                animation = Assets.getInstance().getProtrusionAssets().solidProtrustion;
                center.set(Constants.PROTRUSION_SOLID_CENTER);
                collisionSpan.set(Constants.PROTRUSION_SOLID_COLLISION_WIDTH, Constants.PROTRUSION_SOLID_COLLISION_HEIGHT);
                knockback.set(Constants.PROTRUSION_SOLID_KNOCKBACK);
                damage = Constants.PROTRUSION_SOLID_DAMAGE;
                break;
            default:
                animation = Assets.getInstance().getProtrusionAssets().oreProtrusion;
                center.set(Constants.PROTRUSION_ORE_CENTER);
                collisionSpan.set(Constants.PROTRUSION_ORE_COLLISION_WIDTH, Constants.PROTRUSION_ORE_COLLISION_HEIGHT);
                knockback.set(Constants.PROTRUSION_ORE_KNOCKBACK);
                damage = Constants.PROTRUSION_ORE_DAMAGE;
                break;
        }
    }
}
