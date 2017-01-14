package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.udacity.gamedev.gigagal.Level;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums.*;
import com.udacity.gamedev.gigagal.util.Utils;

// immutable
public final class Ammo implements IndestructibleHazard {

    // fields
    public final static String TAG = Ammo.class.getName();
    private final Level level;
    private int damage;
    private Vector2 knockback;
    private final AmmoIntensity ammoIntensity;
    private final WeaponType weapon;
    private final Direction direction;
    private final Orientation orientation;
    private final Vector2 position;
    private final float radius;
    private final boolean fromGigagal;
    private TextureRegion region;
    private boolean active;

    // ctor
    public Ammo(Level level, Vector2 position, Direction direction, Orientation orientation, AmmoIntensity ammoIntensity, WeaponType weapon, boolean fromGigagal) {
        this.level = level;
        this.position = position;
        this.direction = direction;
        this.orientation = orientation;
        this.ammoIntensity = ammoIntensity;
        this.weapon = weapon;
        this.fromGigagal = fromGigagal;
        knockback = new Vector2();
        damage = 0;
        active = true;
        region = null;
        if (ammoIntensity == AmmoIntensity.BLAST) {
            radius = Constants.BLAST_RADIUS;
        } else {
            radius = Constants.SHOT_RADIUS;
        }
    }

    public void update(float delta) {
        switch (weapon) {
            case NATIVE:
                damage = Constants.AMMO_STANDARD_DAMAGE;
                knockback = Constants.ZOOMBA_KNOCKBACK;
                if (ammoIntensity == AmmoIntensity.BLAST) {
                    region = Assets.getInstance().getAmmoAssets().nativeBlast;
                } else {
                    region = Assets.getInstance().getAmmoAssets().nativeShot;
                }
                break;
            case FIRE:
                damage = Constants.FLAME_DAMAGE;
                knockback = Constants.FLAME_KNOCKBACK;
                if (ammoIntensity == AmmoIntensity.BLAST) {
                    if (orientation == Orientation.Y) {
                        region = Assets.getInstance().getAmmoAssets().fireBlastAlt;
                    } else {
                        region = Assets.getInstance().getAmmoAssets().fireBlast;
                    }
                } else if (orientation == Orientation.Y) {
                    region = Assets.getInstance().getAmmoAssets().fireShotAlt;
                } else {
                    region = Assets.getInstance().getAmmoAssets().fireShot;
                }
                break;
            case WATER:
                damage = Constants.GEISER_DAMAGE;
                knockback = Constants.GEISER_KNOCKBACK;
                if (ammoIntensity == AmmoIntensity.BLAST) {
                    if (orientation == Orientation.Y) {
                        region = Assets.getInstance().getAmmoAssets().waterBlastAlt;
                    } else {
                        region = Assets.getInstance().getAmmoAssets().waterBlast;
                    }
                } else if (orientation == Orientation.Y) {
                    region = Assets.getInstance().getAmmoAssets().waterShotAlt;
                } else {
                    region = Assets.getInstance().getAmmoAssets().waterShot;
                }
                break;
            case ELECTRIC:
                damage = Constants.COIL_DAMAGE;
                knockback = Constants.COIL_KNOCKBACK;
                if (ammoIntensity == AmmoIntensity.BLAST) {
                    region = Assets.getInstance().getAmmoAssets().electricBlast;
                }  else if (orientation == Orientation.Y) {
                    region = Assets.getInstance().getAmmoAssets().electricShotAlt;
                } else {
                    region = Assets.getInstance().getAmmoAssets().electricShot;
                }
                break;
            case RUBBER:
                damage = Constants.WHEEL_DAMAGE;
                knockback = Constants.WHEEL_KNOCKBACK;
                if (ammoIntensity == AmmoIntensity.BLAST) {
                    region = Assets.getInstance().getAmmoAssets().rubberBlast;
                } else if (orientation == Orientation.Y) {
                    region = Assets.getInstance().getAmmoAssets().rubberShotAlt;
                } else {
                    region = Assets.getInstance().getAmmoAssets().rubberShot;
                }
                break;
            case METAL:
                damage = Constants.SPIKE_DAMAGE;
                knockback = Constants.SPIKE_KNOCKBACK;
                if (ammoIntensity == AmmoIntensity.BLAST) {
                    if (orientation == Orientation.Y) {
                        region = Assets.getInstance().getAmmoAssets().metalBlastAlt;
                    } else {
                        region = Assets.getInstance().getAmmoAssets().metalBlast;
                    }
                } else if (orientation == Orientation.Y) {
                    region = Assets.getInstance().getAmmoAssets().metalShotAlt;
                } else {
                    region = Assets.getInstance().getAmmoAssets().metalShot;
                }
                break;
            case PSYCHIC:
                damage = Constants.MAX_HEALTH / 2;
                knockback = Constants.ZOOMBA_KNOCKBACK;
                if (ammoIntensity == AmmoIntensity.BLAST) {
                    region = Assets.getInstance().getAmmoAssets().psychicBlast;
                } else {
                    region = Assets.getInstance().getAmmoAssets().psychicShot;
                }
                break;
            default:
                damage = Constants.AMMO_STANDARD_DAMAGE;
                knockback = Constants.ZOOMBA_KNOCKBACK;
        }

        for (DestructibleHazard destructible : level.getDestructibles()) {
            if (position.dst(destructible.getPosition()) < (destructible.getShotRadius() + radius)) {
                level.spawnExplosion(position);
                active = false;
                TypeEffectiveness effectiveness = Utils.getAmmoEffectiveness(destructible.getType(), weapon);
                switch (effectiveness) {
                    case STRONG:
                        damage = Constants.AMMO_SPECIALIZED_DAMAGE;
                        break;
                    case WEAK:
                        damage = Constants.AMMO_WEAK_DAMAGE;
                        break;
                    case NORMAL:
                        damage = Constants.AMMO_STANDARD_DAMAGE;
                        break;
                    default:
                        damage = Constants.AMMO_STANDARD_DAMAGE;
                }
                if (!fromGigagal) {
                    damage -= Constants.AMMO_WEAK_DAMAGE;
                    damage /= 2;
                } else {
                    level.setLevelScore(level.getLevelScore() + destructible.getHitScore());
                }
                Utils.applyDamage(destructible, ammoIntensity, damage);
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
    public void render(SpriteBatch batch) {
        Vector2 ammoCenter = new Vector2();
        float scale = 1;
        if (ammoIntensity == AmmoIntensity.BLAST) {
            ammoCenter.set(Constants.BLAST_CENTER);
        } else if (ammoIntensity == AmmoIntensity.CHARGE_SHOT) {
            scale += (Constants.CHARGE_DURATION / 3);
            ammoCenter.set(Constants.SHOT_CENTER);
            ammoCenter.scl(scale);
        } else {
            ammoCenter.set(Constants.SHOT_CENTER);
        }
        if (!level.getGigaGal().getPauseState()) {
            Utils.drawTextureRegion(batch, region, position, ammoCenter, scale);
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
    public final AmmoIntensity getAmmoIntensity() { return ammoIntensity; }
    public final TextureRegion getTexture() { return region; }
    public final boolean isFromGigagal() { return fromGigagal; }
}
