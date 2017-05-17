package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.app.LevelUpdater;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums.*;
import com.udacity.gamedev.gigagal.util.Helpers;

// immutable
public final class Ammo implements Indestructible, Hazard {

    // fields
    public final static String TAG = Ammo.class.getName();

    private final LevelUpdater level;
    private final Vector2 position;
    private Vector2 ammoCenter;
    private final Direction direction;
    private final Orientation orientation;
    private final ShotIntensity shotIntensity;
    private final Material weapon;
    private int damage;
    private float radius;
    private float scale;
    private float rotation;
    private final boolean fromGigagal;
    private boolean active;
    private int hitScore;
    private Vector2 knockback; // class-level instantiation
    private Animation animation;
    private long startTime;

    // ctor
    public Ammo(LevelUpdater level, Vector2 position, Direction direction, Orientation orientation, ShotIntensity shotIntensity, Material weapon, boolean fromGigagal) {
        this.level = level;
        this.position = position;
        this.direction = direction;
        this.orientation = orientation;
        this.shotIntensity = shotIntensity;
        this.weapon = weapon;
        this.fromGigagal = fromGigagal;
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
            scale += (Constants.CHARGE_DURATION / 3);
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
        switch (weapon) {
            case NATIVE:
                damage = Constants.AMMO_STANDARD_DAMAGE;
                knockback = Constants.ZOOMBA_KNOCKBACK;
                if (shotIntensity == ShotIntensity.BLAST) {
                    animation = Assets.getInstance().getAmmoAssets().nativeBlast;
                } else {
                    animation = Assets.getInstance().getAmmoAssets().nativeShot;
                }
                break;
            case GAS:
                damage = Constants.PROTRUSION_GAS_DAMAGE;
                knockback = Constants.PROTRUSION_GAS_KNOCKBACK;
                if (shotIntensity == ShotIntensity.BLAST) {
                    animation = Assets.getInstance().getAmmoAssets().gasBlast;
                } else {
                    animation = Assets.getInstance().getAmmoAssets().gasShot;
                }
                break;
            case LIQUID:
                damage = Constants.PROTRUSION_LIQUID_DAMAGE;
                knockback = Constants.PROTRUSION_LIQUID_KNOCKBACK;
                if (shotIntensity == ShotIntensity.BLAST) {
                    animation = Assets.getInstance().getAmmoAssets().liquidBlast;
                } else {
                    animation = Assets.getInstance().getAmmoAssets().liquidShot;
                }
                break;
            case PLASMA:
                damage = Constants.SUSPENSION_PLASMA_DAMAGE;
                knockback = Constants.SUSPENSION_PLASMA_KNOCKBACK;
                if (shotIntensity == ShotIntensity.BLAST) {
                    animation = Assets.getInstance().getAmmoAssets().plasmaBlast;
                } else {
                    animation = Assets.getInstance().getAmmoAssets().plasmaShot;
                }
                break;
            case ORE:
                damage = Constants.SUSPENSION_ORE_DAMAGE;
                knockback = Constants.SUSPENSION_ORE_KNOCKBACK;
                if (shotIntensity == ShotIntensity.BLAST) {
                    animation = Assets.getInstance().getAmmoAssets().oreBlast;
                } else {
                    animation = Assets.getInstance().getAmmoAssets().oreShot;
                }
                break;
            case SOLID:
                damage = Constants.PROTRUSION_SOLID_DAMAGE;
                knockback = Constants.PROTRUSION_SOLID_KNOCKBACK;
                if (shotIntensity == ShotIntensity.BLAST) {
                    animation = Assets.getInstance().getAmmoAssets().solidBlast;
                } else {
                    animation = Assets.getInstance().getAmmoAssets().solidShot;
                }
                break;
            case ANTIMATTER:
                damage = Constants.MAX_HEALTH / 2;
                knockback = Constants.ZOOMBA_KNOCKBACK;
                if (shotIntensity == ShotIntensity.BLAST) {
                    animation = Assets.getInstance().getAmmoAssets().antimatterBlast;
                } else {
                    animation = Assets.getInstance().getAmmoAssets().antimatterShot;
                }
                break;
            case HYBRID:
                damage = Constants.PROTRUSION_SOLID_DAMAGE * 2;
                knockback = Constants.ZOOMBA_KNOCKBACK;
                if (shotIntensity == ShotIntensity.BLAST) {
                    animation = Assets.getInstance().getAmmoAssets().hybridBlast;
                } else {
                    animation = Assets.getInstance().getAmmoAssets().hybridShot;
                }
                break;
            default:
                damage = Constants.AMMO_STANDARD_DAMAGE;
                knockback = Constants.ZOOMBA_KNOCKBACK;
        }
    }

    public void update(float delta) {
        for (Hazard hazard : level.getHazards()) {
            if (hazard instanceof Destructible) {
                Destructible destructible = (Destructible) hazard;
                if (position.dst(destructible.getPosition()) < (destructible.getShotRadius() + this.radius)) {
                    LevelUpdater.getInstance().spawnExplosion(position, weapon);
                    active = false;
                    Helpers.applyDamage(destructible, this);
                }
            }
        }

        for (Ground ground : level.getGrounds()) {
            if (ground instanceof Strikeable) {
                Strikeable strikeable = (Strikeable) ground;
                if (position.dst(strikeable.getPosition()) < (strikeable.getShotRadius() + this.radius)) {
                    Assets.getInstance().getSoundAssets().hitGround.play();
                    if (strikeable instanceof Button) {
                        Switchable switchable = ((Switchable) strikeable);
                        switchable.resetStartTime();
                        switchable.setState(!switchable.getState());
                    } else if (strikeable instanceof Chargeable) {
                        Chargeable chargeable = (Chargeable) strikeable;
                        chargeable.deactivate();
                    } else if (strikeable instanceof Destructible) {
                        Helpers.applyDamage((Destructible) ground, this);
                    }
                    LevelUpdater.getInstance().spawnExplosion(position, weapon);
                    active = false;
                }
            }
        }

        float ammoSpeed = Constants.AMMO_MAX_SPEED;
        if (!fromGigagal) {
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

        final float margin = 25;
        if (orientation == Orientation.X) {
            final float halfWorldWidth = margin + level.getViewport().getWorldWidth() / 2;
            final float cameraX = level.getViewport().getCamera().position.x;
            if (position.x < (cameraX - halfWorldWidth)
            || (position.x > (cameraX + halfWorldWidth))) {
                active = false;
            }
        } else if (orientation == Orientation.Y) {
            final float halfWorldWidth = margin + level.getViewport().getWorldWidth() / 2;
            final float cameraY = level.getViewport().getCamera().position.y;
            if (position.y < (cameraY - halfWorldWidth)
            || (position.y > (cameraY + halfWorldWidth))) {
                active = false;
            }
        }
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {

        if (active) {
            Helpers.drawTextureRegion(batch, viewport, animation.getKeyFrame(Helpers.secondsSince(startTime)), position, ammoCenter, scale, rotation);
        }
    }

    public final boolean isActive() { return active; }
    @Override public final Vector2 getPosition() { return position; }
    @Override public final float getWidth() { return radius * 2; }
    @Override public final float getHeight() { return radius * 2; }
    @Override public final float getLeft() { return position.x - radius; }
    @Override public final float getRight() { return position.x + radius; }
    @Override public final float getTop() { return position.y + radius; }
    @Override public final float getBottom() { return position.y - radius; }
    public final int getDamage() { return damage; }
    public final Vector2 getKnockback() { return knockback; }
    public final ShotIntensity getShotIntensity() { return shotIntensity; }
    public final Material getType() { return weapon; }
    public final TextureRegion getTexture() { return animation.getKeyFrame(0); }
    public final int getHitScore() { return hitScore; }
    public final void setHitScore(int hitScore) { this.hitScore = hitScore; }
    public final boolean isFromGigagal() { return fromGigagal; }
}
