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
public final class Ammo extends Indestructible {

    // fields
    public final static String TAG = Ammo.class.getName();
    private final Level level;
    private int damage;
    private Vector2 knockback;
    private final ShotIntensity shotIntensity;
    private final Weapon weapon;
    private final Direction direction;
    private final Orientation orientation;
    private final Vector2 position;
    private TextureRegion region;
    private boolean active;

    // ctor
    public Ammo(Level level, Vector2 position, Direction direction, Orientation orientation, ShotIntensity shotIntensity, Weapon weapon) {
        this.level = level;
        this.position = position;
        this.direction = direction;
        this.orientation = orientation;
        this.shotIntensity = shotIntensity;
        this.weapon = weapon;
        knockback = new Vector2();
        damage = 0;
        active = true;
        region = null;
    }

    public void update(float delta) {
        if (orientation == Orientation.LATERAL) {
            switch (direction) {
                case LEFT:
                    position.x -= delta * Constants.AMMO_MOVE_SPEED;
                    break;
                case RIGHT:
                    position.x += delta * Constants.AMMO_MOVE_SPEED;
                    break;
            }
        } else if (orientation == Orientation.VERTICAL) {
            switch (direction) {
                case DOWN:
                    position.y -= delta * Constants.AMMO_MOVE_SPEED;
                    break;
                case UP:
                    position.y += delta * Constants.AMMO_MOVE_SPEED;
                    break;
            }
        }

        Class specializedZoomba = null;
        Class specializedSwoopa = null;

        switch (weapon) {
            case NATIVE:
                damage = Constants.AMMO_STANDARD_DAMAGE;
                knockback = Constants.ZOOMBA_KNOCKBACK;
                if (shotIntensity == ShotIntensity.CHARGED) {
                    region = Assets.getInstance().getAmmoAssets().nativeBlast;
                } else {
                    region = Assets.getInstance().getAmmoAssets().nativeShot;
                }
                break;
            case FIRE:
                specializedZoomba = SharpZoomba.class;
                specializedSwoopa = SharpSwoopa.class;
                damage = Constants.FLAME_DAMAGE;
                knockback = Constants.FLAME_KNOCKBACK;
                if (shotIntensity == ShotIntensity.CHARGED) {
                    region = Assets.getInstance().getAmmoAssets().fireBlast;
                } else {
                    region = Assets.getInstance().getAmmoAssets().fireShot;
                }
                break;
            case WATER:
                specializedZoomba = FireyZoomba.class;
                specializedSwoopa = FireySwoopa.class;
                damage = Constants.GEISER_DAMAGE;
                knockback = Constants.GEISER_KNOCKBACK;
                if (shotIntensity == ShotIntensity.CHARGED) {
                    region = Assets.getInstance().getAmmoAssets().waterBlast;
                } else {
                    region = Assets.getInstance().getAmmoAssets().waterShot;
                }
                break;
            case ELECTRIC:
                specializedZoomba = GushingZoomba.class;
                specializedSwoopa = GushingSwoopa.class;
                damage = Constants.COIL_DAMAGE;
                knockback = Constants.COIL_KNOCKBACK;
                if (shotIntensity == ShotIntensity.CHARGED) {
                    region = Assets.getInstance().getAmmoAssets().electricBlast;
                } else {
                    region = Assets.getInstance().getAmmoAssets().electricShot;
                }
                break;
            case RUBBER:
                specializedZoomba = ChargedZoomba.class;
                specializedSwoopa = ChargedSwoopa.class;
                damage = Constants.WHEEL_DAMAGE;
                knockback = Constants.WHEEL_KNOCKBACK;
                if (shotIntensity == ShotIntensity.CHARGED) {
                    region = Assets.getInstance().getAmmoAssets().rubberBlast;
                } else {
                    region = Assets.getInstance().getAmmoAssets().rubberShot;
                }
                break;
            case METAL:
                specializedZoomba = WhirlingZoomba.class;
                specializedSwoopa = WhirlingSwoopa.class;
                damage = Constants.SPIKE_DAMAGE;
                knockback = Constants.SPIKE_KNOCKBACK;
                if (shotIntensity == ShotIntensity.CHARGED) {
                    region = Assets.getInstance().getAmmoAssets().metalBlast;
                } else {
                    region = Assets.getInstance().getAmmoAssets().metalShot;
                }
                break;
            case PSYCHIC:
                damage = 50;
                knockback = Constants.ZOOMBA_KNOCKBACK;
                if (shotIntensity == ShotIntensity.CHARGED) {
                    region = Assets.getInstance().getAmmoAssets().psychicBlast;
                } else {
                    region = Assets.getInstance().getAmmoAssets().psychicShot;
                }
                break;
            default:
                damage = Constants.AMMO_STANDARD_DAMAGE;
                knockback = Constants.ZOOMBA_KNOCKBACK;
        }

        for (Destructible destructible : level.getDestructibles()) {
            if (position.dst(destructible.getPosition()) < destructible.getShotRadius()) {
                level.spawnExplosion(position);
                active = false;
                damage = Constants.AMMO_STANDARD_DAMAGE;
                if (destructible.getClass() == specializedZoomba) {
                    damage = Utils.specializeDamage(destructible, specializedZoomba, Constants.AMMO_SPECIALIZED_DAMAGE, Constants.AMMO_STANDARD_DAMAGE / 3);
                } else if (destructible.getClass() == specializedSwoopa) {
                    damage = Utils.specializeDamage(destructible, specializedSwoopa, Constants.AMMO_SPECIALIZED_DAMAGE, Constants.AMMO_STANDARD_DAMAGE / 3);
                }
                Utils.applyDamage(destructible, shotIntensity, damage);
                level.setScore(level.getScore() + destructible.getHitScore());
            }
        }

        if (orientation == Orientation.LATERAL) {
            final float halfWorldWidth = level.getViewport().getWorldWidth() / 2;
            final float cameraX = level.getViewport().getCamera().position.x;
            if ((position.x < (cameraX - (halfWorldWidth * 2))) || position.x > (cameraX + (halfWorldWidth * 2))) {
                active = false;
            }
        } else if (orientation == Orientation.VERTICAL) {
            final float halfWorldHeight = level.getViewport().getWorldHeight() / 2;
            final float cameraY = level.getViewport().getCamera().position.y;
            if ((position.y < (cameraY - (halfWorldHeight * 2))) || position.y > (cameraY + (halfWorldHeight * 2))) {
                active = false;
            }
        }
    }

    public void render(SpriteBatch batch) {
        Vector2 ammoCenter = new Vector2();
        if (shotIntensity == ShotIntensity.CHARGED) {
            ammoCenter.set(Constants.BLAST_CENTER);
        } else {
            ammoCenter.set(Constants.SHOT_CENTER);
        }
        Utils.drawTextureRegion(batch, region, position, ammoCenter);
    }

    public final boolean isActive() { return active; }
    public final Vector2 getPosition() { return position; }
    public final float getWidth() { return Constants.SHOT_CENTER.x * 2; }
    public final float getHeight() { return Constants.SHOT_CENTER.y * 2; }
    public final float getLeft() { return position.x - Constants.SHOT_CENTER.x; }
    public final float getRight() { return position.x + Constants.SHOT_CENTER.x; }
    public final float getTop() { return position.y + Constants.SHOT_CENTER.y; }
    public final float getBottom() { return position.y - Constants.SHOT_CENTER.y; }
    public final Class getSubclass() { return this.getClass(); }
    public final int getDamage() { return damage / 3; }
    public final Vector2 getKnockback() { return knockback; }
    public final ShotIntensity getShotIntensity() { return shotIntensity; }
}
