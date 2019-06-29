package com.github.rjbx.energen.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.rjbx.energen.util.AssetManager;
import com.github.rjbx.energen.util.Constants;
import com.github.rjbx.energen.util.Enums;
import com.github.rjbx.energen.util.Enums.*;
import com.github.rjbx.energen.util.Helpers;

// immutable
public final class Projectile extends Hazard implements Indestructible, Orientable {

    // fields
    public final static String TAG = Projectile.class.getName();

    private final Vector2 position;
    private Vector2 ammoCenter;
    private final Direction direction;
    private final Orientation orientation;
    private final ShotIntensity shotIntensity;
    private final Energy energy;
    private Rectangle bounds;
    private int damage;
    private float radius;
    private float scale;
    private float rotation;
    private final Entity source;
    private boolean active;
    private int hitScore;
    private Vector2 knockback; // class-level instantiation
    private Animation<TextureRegion> animation;
    private long startTime;

    // ctor
    public Projectile(Vector2 position, Direction direction, Orientation orientation, ShotIntensity shotIntensity, Energy energy, Entity source) {
        this.position = position;
        this.direction = direction;
        this.orientation = orientation;
        this.shotIntensity = shotIntensity;
        this.energy = energy;
        this.source = source;
        startTime = TimeUtils.nanoTime();
        knockback = new Vector2();
        damage = 0;
        active = true;
        hitScore = 0;
        animation = null;
        scale = 1;
        rotation = 0;
        if (shotIntensity == ShotIntensity.BLAST) {
            radius = Constants.BLAST_RADIUS;
        } else if (shotIntensity == ShotIntensity.CHARGED) {
            scale += (Constants.BLAST_CHARGE_DURATION / 3);
            radius = Constants.SHOT_RADIUS;
            radius *= scale;
        } else {
            radius = Constants.SHOT_RADIUS;
        }
        if (orientation == Orientation.Y) {
            rotation = 90;
            ammoCenter = new Vector2(-radius, radius);
        } else {
            ammoCenter = new Vector2(radius, radius);
        }
        bounds = new Rectangle(getLeft(), getBottom(), getWidth(), getHeight());
        switch (energy) {
            case NATIVE:
                damage = Constants.AMMO_STANDARD_DAMAGE;
                knockback = Constants.ZOOMBA_KNOCKBACK;
                if (shotIntensity == ShotIntensity.BLAST) {
                    animation = AssetManager.getInstance().getAmmoAssets().nativeBlast;
                } else {
                    animation = AssetManager.getInstance().getAmmoAssets().nativeShot;
                }
                break;
            case GAS:
                damage = Constants.PROTRUSION_GAS_DAMAGE;
                knockback = Constants.PROTRUSION_GAS_KNOCKBACK;
                if (shotIntensity == ShotIntensity.BLAST) {
                    animation = AssetManager.getInstance().getAmmoAssets().gasBlast;
                } else {
                    animation = AssetManager.getInstance().getAmmoAssets().gasShot;
                }
                break;
            case LIQUID:
                damage = Constants.PROTRUSION_LIQUID_DAMAGE;
                knockback = Constants.PROTRUSION_LIQUID_KNOCKBACK;
                if (shotIntensity == ShotIntensity.BLAST) {
                    animation = AssetManager.getInstance().getAmmoAssets().liquidBlast;
                } else {
                    animation = AssetManager.getInstance().getAmmoAssets().liquidShot;
                }
                break;
            case PLASMA:
                damage = Constants.SUSPENSION_PLASMA_DAMAGE;
                knockback = Constants.SUSPENSION_PLASMA_KNOCKBACK;
                if (shotIntensity == ShotIntensity.BLAST) {
                    animation = AssetManager.getInstance().getAmmoAssets().plasmaBlast;
                } else {
                    animation = AssetManager.getInstance().getAmmoAssets().plasmaShot;
                }
                break;
            case ORE:
                damage = Constants.SUSPENSION_ORE_DAMAGE;
                knockback = Constants.SUSPENSION_ORE_KNOCKBACK;
                if (shotIntensity == ShotIntensity.BLAST) {
                    animation = AssetManager.getInstance().getAmmoAssets().oreBlast;
                } else {
                    animation = AssetManager.getInstance().getAmmoAssets().oreShot;
                }
                break;
            case SOLID:
                damage = Constants.PROTRUSION_SOLID_DAMAGE;
                knockback = Constants.PROTRUSION_SOLID_KNOCKBACK;
                if (shotIntensity == ShotIntensity.BLAST) {
                    animation = AssetManager.getInstance().getAmmoAssets().solidBlast;
                } else {
                    animation = AssetManager.getInstance().getAmmoAssets().solidShot;
                }
                break;
            case ANTIMATTER:
                damage = Constants.MAX_HEALTH / 2;
                knockback = Constants.ZOOMBA_KNOCKBACK;
                if (shotIntensity == ShotIntensity.BLAST) {
                    animation = AssetManager.getInstance().getAmmoAssets().antimatterBlast;
                } else {
                    animation = AssetManager.getInstance().getAmmoAssets().antimatterShot;
                }
                break;
            case HYBRID:
                damage = Constants.PROTRUSION_SOLID_DAMAGE * 2;
                knockback = Constants.ZOOMBA_KNOCKBACK;
                if (shotIntensity == ShotIntensity.BLAST) {
                    animation = AssetManager.getInstance().getAmmoAssets().hybridBlast;
                } else {
                    animation = AssetManager.getInstance().getAmmoAssets().hybridShot;
                }
                break;
            default:
                damage = Constants.AMMO_STANDARD_DAMAGE;
                knockback = Constants.ZOOMBA_KNOCKBACK;
        }
    }

    public Projectile safeClone() {
        Projectile clone = new Projectile(position, direction, orientation, shotIntensity, energy, source);
        clone.setClonedHashCode(hashCode());
        return clone;
    }

    public void update(float delta) {
        float ammoSpeed = Constants.AMMO_MAX_SPEED;
        if (!(source instanceof Avatar)) {
            ammoSpeed = Constants.AMMO_NORMAL_SPEED;
        }

        if (orientation == Orientation.X) {
            switch (direction) {
                case LEFT:
                    position.x -= delta * ammoSpeed;
                    break;
                case RIGHT:
                    position.x += delta * ammoSpeed;
                    break;
            }
        } else if (orientation == Orientation.Y) {
            switch (direction) {
                case DOWN:
                    position.y -= delta * ammoSpeed;
                    break;
                case UP:
                    position.y += delta * ammoSpeed;
                    break;
            }
        }
        bounds.setCenter(position.x, position.y);
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        if (orientation == Orientation.X) {
            final float rangeWidth = viewport.getWorldWidth() * 1.5f;
            final float cameraX = viewport.getCamera().position.x;
            if (position.x < (cameraX - rangeWidth)
                    || (position.x > (cameraX + rangeWidth))) {
                active = false;
            }
        } else if (orientation == Orientation.Y) {
            final float rangeHeight = viewport.getWorldHeight() * 1.5f;
            final float cameraY = viewport.getCamera().position.y;
            if (position.y < (cameraY - rangeHeight)
                    || (position.y > (cameraY + rangeHeight))) {
                active = false;
            }
        }
        if (active) {
            Helpers.drawTextureRegion(batch, viewport, animation.getKeyFrame(Helpers.secondsSince(startTime)), position, ammoCenter, scale, rotation);
        }
    }

    public final boolean isActive() { return active; }
    public final Enums.Direction getDirection() { return direction; }
    @Override public final Vector2 getPosition() { return position; }
    @Override public final float getWidth() { return radius * 2; }
    @Override public final float getHeight() { return radius * 2; }
    @Override public final float getLeft() { return position.x - radius; }
    @Override public final float getRight() { return position.x + radius; }
    @Override public final float getTop() { return position.y + radius; }
    @Override public final float getBottom() { return position.y - radius; }
    @Override public final Orientation getOrientation() { return orientation; }
    public Rectangle getBounds() { return bounds; }
    public final float getRadius() { return radius; }
    public final int getDamage() { return damage; }
    public final Vector2 getKnockback() { return knockback; }
    public final ShotIntensity getShotIntensity() { return shotIntensity; }
    public final Energy getType() { return energy; }
    public final TextureRegion getTexture() { float frame = energy.equals(Energy.ANTIMATTER) ? 1 : 0; return animation.getKeyFrame(frame); }
    public final int getHitScore() { return hitScore; }
    public final Entity getSource() { return source; }

    public final void deactivate() { active = false; }
    public final void setHitScore(int hitScore) { this.hitScore = hitScore; }

    @Override
    public int getPriority() {
        return Constants.PRIORITY_MEDIUM;
    }
}
