package com.github.rjbx.energen.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.rjbx.energen.util.AssetManager;
import com.github.rjbx.energen.util.Constants;
import com.github.rjbx.energen.util.Enums;
import com.github.rjbx.energen.util.Helpers;

public class Suspension extends Hazard implements Indestructible, Convertible {

    // fields
    public final static String TAG = Suspension.class.getName();

    private Vector2 position;
    private Enums.Energy type;
    private Vector2 collisionSpan; // class-level instantiation
    private Vector2 center; // class-level instantiation
    private Vector2 knockback; // class-level instantiation
    private Animation<TextureRegion> animation;
    private int damage;
    private boolean state;
    private boolean converted;
    private long startTime;

    // ctor
    public Suspension(Vector2 position, Enums.Energy type) {
        this.position = position;
        this.type = type;
        startTime = TimeUtils.nanoTime();
        collisionSpan = new Vector2();
        state = true;
        converted = false;
        center = new Vector2();
        knockback = new Vector2();
        damage = Constants.PROTRUSION_LIQUID_DAMAGE;
        setTypeAttributes(type);
    }

    @Override
    public Suspension safeClone() {
        Suspension clone = new Suspension(position, type);
        clone.setClonedHashCode(hashCode());
        return clone;
    }

    @Override
    public void update(float delta) {
        if (state) {
            if (knockback.equals(Vector2.Zero)) {
                setTypeAttributes(type);
            }
        } else {
            converted = false;
            if (!knockback.equals(Vector2.Zero)) {
                knockback.setZero();
            }
        }
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        if (state) {
            Helpers.drawTextureRegion(batch, viewport, animation.getKeyFrame(Helpers.secondsSince(startTime), true), position, center);
        } else {
            Helpers.drawTextureRegion(batch, viewport, AssetManager.getInstance().getSuspensionAssets().inactiveSuspension, position, center);
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
    @Override public final Enums.Energy getType() { return type; }
    public final long getStartTime() { return startTime; }
    public final void resetStartTime() { this.startTime = 0; }
    @Override public void convert() { state = !state; converted = true; }
    @Override public boolean isConverted() { return !state; }
    private final void setTypeAttributes(Enums.Energy type) {
        switch (type) {
            case ORE:
                animation = AssetManager.getInstance().getSuspensionAssets().oreSuspension;
                center.set(Constants.SUSPENSION_ORE_CENTER);
                collisionSpan.set(Constants.SUSPENSION_ORE_COLLISION_WIDTH, Constants.SUSPENSION_ORE_COLLISION_HEIGHT);
                knockback.set(Constants.SUSPENSION_ORE_KNOCKBACK);
                damage = Constants.SUSPENSION_ORE_DAMAGE;
                break;
            case PLASMA:
                animation = AssetManager.getInstance().getSuspensionAssets().plasmaSuspension;
                center.set(Constants.SUSPENSION_PLASMA_CENTER);
                collisionSpan.set(Constants.SUSPENSION_PLASMA_COLLISION_WIDTH, Constants.SUSPENSION_PLASMA_COLLISION_HEIGHT);
                knockback.set(Constants.SUSPENSION_PLASMA_KNOCKBACK);
                damage = Constants.SUSPENSION_PLASMA_DAMAGE;
                break;
            case GAS:
                animation = AssetManager.getInstance().getSuspensionAssets().gasSuspension;
                center.set(Constants.SUSPENSION_GAS_CENTER);
                collisionSpan.set(Constants.SUSPENSION_GAS_COLLISION_WIDTH, Constants.SUSPENSION_GAS_COLLISION_HEIGHT);
                knockback.set(Constants.SUSPENSION_GAS_KNOCKBACK);
                damage = Constants.SUSPENSION_GAS_DAMAGE;
                break;
            case LIQUID:
                animation = AssetManager.getInstance().getSuspensionAssets().liquidSuspension;
                center.set(Constants.SUSPENSION_LIQUID_CENTER);
                collisionSpan.set(Constants.SUSPENSION_LIQUID_COLLISION_WIDTH, Constants.SUSPENSION_LIQUID_COLLISION_HEIGHT);
                knockback.set(Constants.SUSPENSION_LIQUID_KNOCKBACK);
                damage = Constants.SUSPENSION_LIQUID_DAMAGE;
                break;
            case SOLID:
                animation = AssetManager.getInstance().getSuspensionAssets().solidSuspension;
                center.set(Constants.SUSPENSION_SOLID_CENTER);
                collisionSpan.set(Constants.SUSPENSION_SOLID_COLLISION_WIDTH, Constants.SUSPENSION_SOLID_COLLISION_HEIGHT);
                knockback.set(Constants.SUSPENSION_SOLID_KNOCKBACK);
                damage = Constants.SUSPENSION_SOLID_DAMAGE;
                break;
            case ANTIMATTER:
                animation = AssetManager.getInstance().getSuspensionAssets().antimatterSuspension;
                center.set(Constants.SUSPENSION_ANTIMATTER_CENTER);
                collisionSpan.set(Constants.SUSPENSION_ANTIMATTER_COLLISION_WIDTH, Constants.SUSPENSION_ANTIMATTER_COLLISION_HEIGHT);
                knockback.set(Constants.SUSPENSION_ANTIMATTER_KNOCKBACK);
                damage = Constants.SUSPENSION_ANTIMATTER_DAMAGE;
                break;
            default:
                animation = AssetManager.getInstance().getSuspensionAssets().oreSuspension;
                center.set(Constants.SUSPENSION_ORE_CENTER);
                collisionSpan.set(Constants.SUSPENSION_ORE_COLLISION_WIDTH, Constants.SUSPENSION_ORE_COLLISION_HEIGHT);
                knockback.set(Constants.SUSPENSION_ORE_KNOCKBACK);
                damage = Constants.SUSPENSION_ORE_DAMAGE;
                break;
        }
    }

    @Override
    public int getPriority() {
        return Constants.PRIORITY_MEDIUM;
    }
}
