package com.github.rjbx.energen.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.rjbx.energen.util.AssetManager;
import com.github.rjbx.energen.util.Constants;
import com.github.rjbx.energen.util.Enums.*;
import com.github.rjbx.energen.util.Helpers;
import com.github.rjbx.energen.util.InputControls;

// immutable
public final class Blade extends Hazard implements Indestructible {

    // fields
    public final static String TAG = Blade.class.getName();
    public static final Blade INSTANCE = new Blade();
    private Vector2 position;
    private Energy energy;
    private Rectangle bounds;
    private int damage;
    private Vector2 center;
    private float scale;
    private boolean active;
    private int hitScore;
    private Vector2 knockback; // class-level instantiation
    private Animation<TextureRegion> animation;

    // cannot be subclassed
    private Blade() {}

    public static Blade getInstance() { return INSTANCE; }

    public void create() {
        position = Avatar.getInstance().getPosition();
        center = Constants.BLADE_CENTER;
        bounds = new Rectangle(getLeft(), getBottom(), getWidth(), getHeight());
        this.energy = Avatar.getInstance().getEnergy();
        knockback = new Vector2();
        damage = 0;
        active = true;
        hitScore = 0;
        animation = null;
        scale = 1;
    }

    public Blade safeClone() {
        Blade clone = Blade.getInstance();
        clone.setClonedHashCode(hashCode());
        return clone;
    }

    public void update(float delta) {
        setAttributes(Avatar.getInstance().getEnergy());
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        if (Avatar.getInstance().getBladeState() != BladeState.RETRACTED) {
            boolean flipX = false;
            boolean flipY = false;
            if (((Avatar.getInstance().getBladeState() != BladeState.RUSH && Avatar.getInstance().getDirectionX() == Direction.LEFT) || InputControls.getInstance().leftButtonPressed)) {
                flipX = true;
            }
            if (Avatar.getInstance().getBladeState() != BladeState.RUSH && Avatar.getInstance().getDirectionY() == Direction.DOWN) {
                flipY = true;
            }
            Helpers.drawTextureRegion(batch, viewport, animation.getKeyFrame(Avatar.getInstance().getSwipeTimeSeconds()), position, Constants.BLADE_CENTER, 1, 0, flipX, flipY);
        }
    }

    public void setAttributes(Energy energy) {
        if (Avatar.getInstance().getBladeState() != BladeState.RETRACTED) {
            switch (energy) {
                case NATIVE:
                    switch (Avatar.getInstance().getBladeState()) {
                        case FLIP:
                            animation = AssetManager.getInstance().getBladeAssets().nativeBackflip;
                            break;
                        case RUSH:
                            animation = AssetManager.getInstance().getBladeAssets().nativeForehand;
                            break;
                        case CUT:
                            animation = AssetManager.getInstance().getBladeAssets().nativeUppercut;
                            break;
                    }
                    // damage = Constants.AMMO_STANDARD_DAMAGE / Constants.BLADE_DAMAGE_FACTOR;
                    knockback.set(Constants.ZOOMBA_KNOCKBACK);
                    break;
                case LIQUID:
                    switch (Avatar.getInstance().getBladeState()) {
                        case FLIP:
                            animation = AssetManager.getInstance().getBladeAssets().liquidBackflip;
                            break;
                        case RUSH:
                            animation = AssetManager.getInstance().getBladeAssets().liquidForehand;
                            break;
                        case CUT:
                            animation = AssetManager.getInstance().getBladeAssets().liquidUppercut;
                            break;
                    }
                    // damage = Constants.PROTRUSION_LIQUID_DAMAGE / Constants.BLADE_DAMAGE_FACTOR;
                    knockback.set(Constants.PROTRUSION_LIQUID_KNOCKBACK);
                    break;
                case PLASMA:
                    switch (Avatar.getInstance().getBladeState()) {
                        case FLIP:
                            animation = AssetManager.getInstance().getBladeAssets().plasmaBackflip;
                            break;
                        case RUSH:
                            animation = AssetManager.getInstance().getBladeAssets().plasmaForehand;
                            break;
                        case CUT:
                            animation = AssetManager.getInstance().getBladeAssets().plasmaUppercut;
                            break;
                    }
                    // damage = Constants.PROTRUSION_PLASMA_DAMAGE / Constants.BLADE_DAMAGE_FACTOR;
                    knockback.set(Constants.PROTRUSION_PLASMA_KNOCKBACK);
                    break;
                case GAS:
                    switch (Avatar.getInstance().getBladeState()) {
                        case FLIP:
                            animation = AssetManager.getInstance().getBladeAssets().gasBackflip;
                            break;
                        case RUSH:
                            animation = AssetManager.getInstance().getBladeAssets().gasForehand;
                            break;
                        case CUT:
                            animation = AssetManager.getInstance().getBladeAssets().gasUppercut;
                            break;
                    }
                    // damage = Constants.PROTRUSION_GAS_DAMAGE / Constants.BLADE_DAMAGE_FACTOR;
                    knockback.set(Constants.PROTRUSION_GAS_KNOCKBACK);
                    break;
                case SOLID:
                    switch (Avatar.getInstance().getBladeState()) {
                        case FLIP:
                            animation = AssetManager.getInstance().getBladeAssets().solidBackflip;
                            break;
                        case RUSH:
                            animation = AssetManager.getInstance().getBladeAssets().solidForehand;
                            break;
                        case CUT:
                            animation = AssetManager.getInstance().getBladeAssets().solidUppercut;
                            break;
                    }
                    // damage = Constants.PROTRUSION_SOLID_DAMAGE / Constants.BLADE_DAMAGE_FACTOR;
                    knockback.set(Constants.PROTRUSION_SOLID_KNOCKBACK);
                    break;
                case ORE:
                    switch (Avatar.getInstance().getBladeState()) {
                        case FLIP:
                            animation = AssetManager.getInstance().getBladeAssets().oreBackflip;
                            break;
                        case RUSH:
                            animation = AssetManager.getInstance().getBladeAssets().oreForehand;
                            break;
                        case CUT:
                            animation = AssetManager.getInstance().getBladeAssets().oreUppercut;
                            break;
                    }
                    // damage = Constants.PROTRUSION_ORE_DAMAGE / Constants.BLADE_DAMAGE_FACTOR;
                    knockback.set(Constants.PROTRUSION_ORE_KNOCKBACK);
                    break;
                case ANTIMATTER:
                    switch (Avatar.getInstance().getBladeState()) {
                        case FLIP:
                            animation = AssetManager.getInstance().getBladeAssets().antimatterBackflip;
                            break;
                        case RUSH:
                            animation = AssetManager.getInstance().getBladeAssets().antimatterForehand;
                            break;
                        case CUT:
                            animation = AssetManager.getInstance().getBladeAssets().antimatterUppercut;
                            break;
                    }
                    // damage = Constants.SUSPENSION_ANTIMATTER_DAMAGE / Constants.BLADE_DAMAGE_FACTOR;
                    knockback.set(Constants.SUSPENSION_ANTIMATTER_KNOCKBACK);
                    break;
                case HYBRID:
                    switch (Avatar.getInstance().getBladeState()) {
                        case FLIP:
                            animation = AssetManager.getInstance().getBladeAssets().hybridBackflip;
                            break;
                        case RUSH:
                            animation = AssetManager.getInstance().getBladeAssets().hybridForehand;
                            break;
                        case CUT:
                            animation = AssetManager.getInstance().getBladeAssets().hybridUppercut;
                            break;
                    }
                    // damage = Constants.SUSPENSION_ANTIMATTER_DAMAGE / Constants.BLADE_DAMAGE_FACTOR;
                    knockback.set(Constants.SUSPENSION_ANTIMATTER_KNOCKBACK);
                    break;
            }
        }
    }

    public final boolean isActive() { return active; }
    @Override public final Vector2 getPosition() { return position; }
    @Override public final float getWidth() { return Constants.BLADE_CENTER.x * 2; }
    @Override public final float getHeight() { return Constants.BLADE_CENTER.y * 2; }
    @Override public final float getLeft() { return position.x - Constants.BLADE_CENTER.x; }
    @Override public final float getRight() { return position.x + Constants.BLADE_CENTER.x; }
    @Override public final float getTop() { return position.y + Constants.BLADE_CENTER.y; }
    @Override public final float getBottom() { return position.y - Constants.BLADE_CENTER.y; }
    public Rectangle getBounds() { return bounds; }
    public final int getDamage() { return damage; }
    public final Vector2 getKnockback() { return knockback; }
    public final Energy getType() { return energy; }
    public final int getHitScore() { return hitScore; }

    @Override
    public int getPriority() {
        return Constants.PRIORITY_HIGH;
    }
}
