package com.udacity.gamedev.gigagal.util;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.udacity.gamedev.gigagal.entities.Destructible;
import com.udacity.gamedev.gigagal.entities.GigaGal;
import com.udacity.gamedev.gigagal.entities.Physical;

// immutable static
public class Utils {

    // non-instantiable
    private Utils() {}

    public static final void drawTextureRegion(SpriteBatch batch, TextureRegion region, Vector2 position) {
        drawTextureRegion(batch, region, position.x, position.y);
    }

    public static final void drawTextureRegion(SpriteBatch batch, TextureRegion region, Vector2 position, Vector2 offset) {
        drawTextureRegion(batch, region, position.x - offset.x, position.y - offset.y);
    }

    public static final void drawTextureRegion(SpriteBatch batch, TextureRegion region, float x, float y) {
        batch.draw(
                region.getTexture(),
                x,
                y,
                0,
                0,
                region.getRegionWidth(),
                region.getRegionHeight(),
                1,
                1,
                0,
                region.getRegionX(),
                region.getRegionY(),
                region.getRegionWidth(),
                region.getRegionHeight(),
                false,
                false);
    }

    public static final float secondsSince(long timeNanos) {
        return MathUtils.nanoToSec * (TimeUtils.nanoTime() - timeNanos);
    }

    public static final float absValToLateralMovement(float delta, Enums.Direction direction) {
        if (direction == Enums.Direction.RIGHT) {
            return delta;
        } else if (direction == Enums.Direction.LEFT) {
            return -delta;
        }
        return 0;
    }

    public static final boolean setFacing(GigaGal gigaGal, Enums.Direction setTo) {
        if (gigaGal.getFacing() != setTo){
            gigaGal.setFacing(setTo);
            return true;
        } else {
            return false;
        }
    }

    public static final Enums.Direction getOppositeDirection(Enums.Direction direction) {
        if (direction == Enums.Direction.LEFT) {
            return Enums.Direction.RIGHT;
        } else if (direction == Enums.Direction.RIGHT)  {
            return Enums.Direction.LEFT;
        }
        return null;
    }

    public static final boolean betweenSides(Physical entity, float delta) {
        float frontHalf = Constants.GIGAGAL_STANCE_WIDTH / 2;
        if ((delta - frontHalf) <= entity.getRight()
        && (delta + frontHalf) >= entity.getLeft()) {
            return true;
        }
        return false;
    }

    public static final int useAmmo(Enums.ShotIntensity intensity) {
        if (intensity == Enums.ShotIntensity.CHARGED) {
            return 3;
        } else if (intensity == Enums.ShotIntensity.NORMAL) {
            return 1;
        }
        return 0;
    }

    public static final void applyDamage(Destructible destructible, Enums.ShotIntensity shotIntensity, int damage) {
        if (shotIntensity == Enums.ShotIntensity.CHARGED) {
            destructible.setHealth(destructible.getHealth() - damage);
        } else {
            destructible.setHealth((int) (destructible.getHealth() - (damage * .67f)));
        }
    }

    public static Enums.TypeEffectiveness getAmmoEffectiveness(Enums.WeaponType enemyType, Enums.WeaponType ammoType) {
        if (enemyType == ammoType) {
            return Enums.TypeEffectiveness.WEAK;
        } else if ((enemyType == Enums.WeaponType.METAL && ammoType == Enums.WeaponType.FIRE)
        || (enemyType == Enums.WeaponType.METAL && ammoType == Enums.WeaponType.RUBBER)
        || (enemyType == Enums.WeaponType.RUBBER&& ammoType == Enums.WeaponType.ELECTRIC)
        || (enemyType == Enums.WeaponType.ELECTRIC && ammoType == Enums.WeaponType.WATER)
        || (enemyType == Enums.WeaponType.WATER && ammoType == Enums.WeaponType.FIRE)
        || (ammoType == Enums.WeaponType.PSYCHIC)) {
            return Enums.TypeEffectiveness.STRONG;
        } else {
            return Enums.TypeEffectiveness.WEAK;
        }
    }
}
