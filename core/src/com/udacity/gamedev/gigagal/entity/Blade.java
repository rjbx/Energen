package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums.*;
import com.udacity.gamedev.gigagal.util.Helpers;
import com.udacity.gamedev.gigagal.util.InputControls;

// immutable
public final class Blade extends Hazard implements Indestructible {

    // fields
    public final static String TAG = Blade.class.getName();
    public static final Blade INSTANCE = new Blade();
    private Vector2 position;
    private Material weapon;
    private Rectangle bounds;
    private int damage;
    private Vector2 center;
    private float scale;
    private boolean active;
    private int hitScore;
    private Vector2 knockback; // class-level instantiation
    private Animation animation;

    // non-instantiable
    private Blade() {}

    public static Blade getInstance() { return INSTANCE; }

    public void create() {
        position = GigaGal.getInstance().getPosition();
        center = Constants.BLADE_CENTER;
        bounds = new Rectangle(getLeft(), getBottom(), getWidth(), getHeight());
        this.weapon = GigaGal.getInstance().getWeapon();
        knockback = new Vector2();
        damage = 0;
        active = true;
        hitScore = 0;
        animation = null;
        scale = 1;
    }

    public void update(float delta) {
        setAttributes(GigaGal.getInstance().getWeapon());
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        if (GigaGal.getInstance().getBladeState() != BladeState.RETRACTED) {
            boolean flipX = false;
            boolean flipY = false;
            if (((GigaGal.getInstance().getBladeState() != BladeState.RUSH && GigaGal.getInstance().getDirectionX() == Direction.LEFT) || InputControls.getInstance().leftButtonPressed)) {
                flipX = true;
            }
            if (GigaGal.getInstance().getBladeState() != BladeState.RUSH && GigaGal.getInstance().getDirectionY() == Direction.DOWN) {
                flipY = true;
            }
            Helpers.drawTextureRegion(batch, viewport, animation.getKeyFrame(GigaGal.getInstance().getSwipeTimeSeconds()), position, Constants.BLADE_CENTER, 1, 0, flipX, flipY);
        }
    }

    public void setAttributes(Material weapon) {
        if (GigaGal.getInstance().getBladeState() != BladeState.RETRACTED) {
            switch (weapon) {
                case NATIVE:
                    switch (GigaGal.getInstance().getBladeState()) {
                        case FLIP:
                            animation = Assets.getInstance().getBladeAssets().nativeBackflip;
                            break;
                        case RUSH:
                            animation = Assets.getInstance().getBladeAssets().nativeForehand;
                            break;
                        case CUT:
                            animation = Assets.getInstance().getBladeAssets().nativeUppercut;
                            break;
                    }
                    damage = Constants.AMMO_STANDARD_DAMAGE;
                    knockback.set(Constants.ZOOMBA_KNOCKBACK);
                    break;
                case LIQUID:
                    switch (GigaGal.getInstance().getBladeState()) {
                        case FLIP:
                            animation = Assets.getInstance().getBladeAssets().liquidBackflip;
                            break;
                        case RUSH:
                            animation = Assets.getInstance().getBladeAssets().liquidForehand;
                            break;
                        case CUT:
                            animation = Assets.getInstance().getBladeAssets().liquidUppercut;
                            break;
                    }
                    damage = Constants.PROTRUSION_LIQUID_DAMAGE;
                    knockback.set(Constants.PROTRUSION_LIQUID_KNOCKBACK);
                    break;
                case PLASMA:
                    switch (GigaGal.getInstance().getBladeState()) {
                        case FLIP:
                            animation = Assets.getInstance().getBladeAssets().plasmaBackflip;
                            break;
                        case RUSH:
                            animation = Assets.getInstance().getBladeAssets().plasmaForehand;
                            break;
                        case CUT:
                            animation = Assets.getInstance().getBladeAssets().plasmaUppercut;
                            break;
                    }
                    damage = Constants.PROTRUSION_PLASMA_DAMAGE;
                    knockback.set(Constants.PROTRUSION_PLASMA_KNOCKBACK);
                    break;
                case GAS:
                    switch (GigaGal.getInstance().getBladeState()) {
                        case FLIP:
                            animation = Assets.getInstance().getBladeAssets().gasBackflip;
                            break;
                        case RUSH:
                            animation = Assets.getInstance().getBladeAssets().gasForehand;
                            break;
                        case CUT:
                            animation = Assets.getInstance().getBladeAssets().gasUppercut;
                            break;
                    }
                    damage = Constants.PROTRUSION_GAS_DAMAGE;
                    knockback.set(Constants.PROTRUSION_GAS_KNOCKBACK);
                    break;
                case SOLID:
                    switch (GigaGal.getInstance().getBladeState()) {
                        case FLIP:
                            animation = Assets.getInstance().getBladeAssets().solidBackflip;
                            break;
                        case RUSH:
                            animation = Assets.getInstance().getBladeAssets().solidForehand;
                            break;
                        case CUT:
                            animation = Assets.getInstance().getBladeAssets().solidUppercut;
                            break;
                    }
                    damage = Constants.PROTRUSION_SOLID_DAMAGE;
                    knockback.set(Constants.PROTRUSION_SOLID_KNOCKBACK);
                    break;
                case ORE:
                    switch (GigaGal.getInstance().getBladeState()) {
                        case FLIP:
                            animation = Assets.getInstance().getBladeAssets().oreBackflip;
                            break;
                        case RUSH:
                            animation = Assets.getInstance().getBladeAssets().oreForehand;
                            break;
                        case CUT:
                            animation = Assets.getInstance().getBladeAssets().oreUppercut;
                            break;
                    }
                    damage = Constants.PROTRUSION_ORE_DAMAGE;
                    knockback.set(Constants.PROTRUSION_ORE_KNOCKBACK);
                    break;
                case ANTIMATTER:
                    switch (GigaGal.getInstance().getBladeState()) {
                        case FLIP:
                            animation = Assets.getInstance().getBladeAssets().antimatterBackflip;
                            break;
                        case RUSH:
                            animation = Assets.getInstance().getBladeAssets().antimatterForehand;
                            break;
                        case CUT:
                            animation = Assets.getInstance().getBladeAssets().antimatterUppercut;
                            break;
                    }
                    damage = Constants.SUSPENSION_ANTIMATTER_DAMAGE;
                    knockback.set(Constants.SUSPENSION_ANTIMATTER_KNOCKBACK);
                    break;
                case HYBRID:
                    switch (GigaGal.getInstance().getBladeState()) {
                        case FLIP:
                            animation = Assets.getInstance().getBladeAssets().hybridBackflip;
                            break;
                        case RUSH:
                            animation = Assets.getInstance().getBladeAssets().hybridForehand;
                            break;
                        case CUT:
                            animation = Assets.getInstance().getBladeAssets().hybridUppercut;
                            break;
                    }
                    damage = Constants.SUSPENSION_ANTIMATTER_DAMAGE;
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
    public final Material getType() { return weapon; }
    public final int getHitScore() { return hitScore; }
}
