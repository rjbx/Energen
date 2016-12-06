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
public final class Ammo implements Physical {

    // fields
    public final static String TAG = Ammo.class.getName();
    private final Level level;
    private final ShotIntensity shotIntensity;
    private final Weapon weapon;
    private final Direction direction;
    private final Vector2 position;
    private boolean active;

    // ctor
    public Ammo(Level level, Vector2 position, Direction direction, ShotIntensity shotIntensity, Weapon weapon) {
        this.level = level;
        this.position = position;
        this.direction = direction;
        this.shotIntensity = shotIntensity;
        this.weapon = weapon;
        active = true;
    }

    public void update(float delta) {
        switch (direction) {
            case LEFT:
                position.x -= delta * Constants.BULLET_MOVE_SPEED;
                break;
            case RIGHT:
                position.x += delta * Constants.BULLET_MOVE_SPEED;
                break;
        }

        for (Destructible destructible : level.getDestructibles()) {
            if (position.dst(destructible.getPosition()) < destructible.getShotRadius()) {
                level.spawnExplosion(position);
                active = false;
                switch (weapon) {
                    case NATIVE:
                        if (destructible instanceof Zoomba) {
                            Utils.applyDamage(destructible, shotIntensity, Constants.AMMO_STANDARD_DAMAGE);
                        }
                        break;
                    case FIRE:
                        if (destructible instanceof Zoomba) {
                            Utils.applyDamage(destructible, shotIntensity, Constants.AMMO_STANDARD_DAMAGE);
                            Utils.specializeDamage(destructible, SharpZoomba.class, GushingZoomba.class, shotIntensity, Constants.AMMO_SPECIALIZED_DAMAGE, Constants.AMMO_STANDARD_DAMAGE);
                        }
                        break;
                    case WATER:
                        if (destructible instanceof Zoomba) {
                            Utils.applyDamage(destructible, shotIntensity, Constants.AMMO_STANDARD_DAMAGE);
                            Utils.specializeDamage(destructible, SharpZoomba.class, GushingZoomba.class, shotIntensity, Constants.AMMO_SPECIALIZED_DAMAGE, Constants.AMMO_STANDARD_DAMAGE);
                        }
                        break;
                    case ELECTRIC:
                        if (destructible instanceof Zoomba) {
                            Utils.applyDamage(destructible, shotIntensity, Constants.AMMO_STANDARD_DAMAGE);
                            Utils.specializeDamage(destructible, GushingZoomba.class, WhirlingZoomba.class, shotIntensity, Constants.AMMO_SPECIALIZED_DAMAGE, Constants.AMMO_STANDARD_DAMAGE);
                        }
                        break;
                    case RUBBER:
                        if (destructible instanceof Zoomba) {
                            Utils.applyDamage(destructible, shotIntensity, Constants.AMMO_STANDARD_DAMAGE);
                            Utils.specializeDamage(destructible, ChargedZoomba.class, SharpZoomba.class, shotIntensity, Constants.AMMO_SPECIALIZED_DAMAGE, Constants.AMMO_STANDARD_DAMAGE);
                        }
                        break;
                    case METAL:
                        if (destructible instanceof Zoomba) {
                            Utils.applyDamage(destructible, shotIntensity, Constants.AMMO_STANDARD_DAMAGE);
                            Utils.specializeDamage(destructible, WhirlingZoomba.class, FireyZoomba.class, shotIntensity, Constants.AMMO_SPECIALIZED_DAMAGE, Constants.AMMO_STANDARD_DAMAGE);
                        }
                        break;
                    case PSYCHIC:
                        if (destructible instanceof Zoomba) {
                            Utils.applyDamage(destructible, shotIntensity, Constants.AMMO_SPECIALIZED_DAMAGE);
                        }
                        break;
                }
                level.setScore(level.getScore() + destructible.getHitScore());
            }
        }

        final float halfWorldWidth = level.getViewport().getWorldWidth() / 2;
        final float cameraX = level.getViewport().getCamera().position.x;

        if (position.x < cameraX - halfWorldWidth || position.x > cameraX + halfWorldWidth) {
            active = false;
        }
    }

    public void render(SpriteBatch batch) {
        TextureRegion region = null;
        Vector2 ammoCenter = new Vector2();
        switch (weapon) {
            case NATIVE:
                if (shotIntensity == ShotIntensity.CHARGED) {
                    region = Assets.getInstance().getAmmoAssets().nativeBlast;
                } else {
                    region = Assets.getInstance().getAmmoAssets().nativeShot;
                }
                break;
            case FIRE:
                if (shotIntensity == ShotIntensity.CHARGED) {
                    region = Assets.getInstance().getAmmoAssets().fireBlast;
                } else {
                    region = Assets.getInstance().getAmmoAssets().fireShot;
                }
                break;
            case WATER:
                if (shotIntensity == ShotIntensity.CHARGED) {
                    region = Assets.getInstance().getAmmoAssets().waterBlast;
                } else {
                    region = Assets.getInstance().getAmmoAssets().waterShot;
                }
                break;
            case ELECTRIC:
                if (shotIntensity == ShotIntensity.CHARGED) {
                    region = Assets.getInstance().getAmmoAssets().electricBlast;
                } else {
                    region = Assets.getInstance().getAmmoAssets().electricShot;
                }
                break;
            case RUBBER:
                if (shotIntensity == ShotIntensity.CHARGED) {
                    region = Assets.getInstance().getAmmoAssets().rubberBlast;
                } else {
                    region = Assets.getInstance().getAmmoAssets().rubberShot;
                }
                break;
            case METAL:
                if (shotIntensity == ShotIntensity.CHARGED) {
                    region = Assets.getInstance().getAmmoAssets().metalBlast;
                } else {
                    region = Assets.getInstance().getAmmoAssets().metalShot;
                }
                break;
            case PSYCHIC:
                if (shotIntensity == ShotIntensity.CHARGED) {
                    region = Assets.getInstance().getAmmoAssets().psychicBlast;
                } else {
                    region = Assets.getInstance().getAmmoAssets().psychicShot;
                }
                break;
        }
        if (shotIntensity == ShotIntensity.CHARGED) {
            ammoCenter.set(Constants.BLAST_CENTER);
        } else {
            ammoCenter.set(Constants.SHOT_CENTER);
        }
        Utils.drawTextureRegion(batch, region, position, ammoCenter);
    }

    public final boolean isActive() { return active; }
    public final Vector2 getPosition() { return position; }
    public final float getWidth() { return Constants.POWERUP_CENTER.x * 2; }
    public final float getHeight() { return Constants.POWERUP_CENTER.y * 2; }
    public final float getLeft() { return position.x - Constants.POWERUP_CENTER.x; }
    public final float getRight() { return position.x + Constants.POWERUP_CENTER.x; }
    public final float getTop() { return position.y + Constants.POWERUP_CENTER.y; }
    public final float getBottom() { return position.y - Constants.POWERUP_CENTER.y; }
    public final ShotIntensity getShotIntensity() { return shotIntensity; }
}
