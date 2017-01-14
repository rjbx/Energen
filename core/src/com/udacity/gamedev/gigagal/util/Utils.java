package com.udacity.gamedev.gigagal.util;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.udacity.gamedev.gigagal.entities.DestructibleHazard;
import com.udacity.gamedev.gigagal.entities.Directional;
import com.udacity.gamedev.gigagal.entities.GigaGal;
import com.udacity.gamedev.gigagal.entities.Entity;
import com.udacity.gamedev.gigagal.entities.Orben;

import org.apache.commons.lang3.time.StopWatch;

// immutable static
public class Utils {

    // non-instantiable
    private Utils() {}

    public static final void drawTextureRegion(SpriteBatch batch, TextureRegion region, float x, float y) {
        drawTextureRegion(batch, region, x, y, 1);
    }

    public static final void drawTextureRegion(SpriteBatch batch, TextureRegion region, Vector2 position) {
        drawTextureRegion(batch, region, position.x, position.y, 1);
    }

    public static final void drawTextureRegion(SpriteBatch batch, TextureRegion region, Vector2 position, Vector2 offset) {
        drawTextureRegion(batch, region, position.x - offset.x, position.y - offset.y, 1);
    }

    public static final void drawTextureRegion(SpriteBatch batch, TextureRegion region, Vector2 position, Vector2 offset, float scale) {
        drawTextureRegion(batch, region, position.x - offset.x, position.y - offset.y, scale);
    }

    public static final void drawTextureRegion(SpriteBatch batch, TextureRegion region, float x, float y, float scale) {
        batch.draw(
                region.getTexture(),
                x,
                y,
                0,
                0,
                region.getRegionWidth(),
                region.getRegionHeight(),
                scale,
                scale,
                0,
                region.getRegionX(),
                region.getRegionY(),
                region.getRegionWidth(),
                region.getRegionHeight(),
                false,
                false);
    }

    public static final float secondsSincePause(long pauseTime) {
        return secondsSince(pauseTime);
    }

    public static final float secondsSince(long timeNanos) {
        return MathUtils.nanoToSec * (TimeUtils.nanoTime() - timeNanos);
    }

    public static final float absoluteToDirectionalValue(float delta, Enums.Direction direction, Enums.Orientation orientation) {
        if (orientation == Enums.Orientation.LATERAL) {
            if (direction == Enums.Direction.RIGHT) {
                return delta;
            } else if (direction == Enums.Direction.LEFT) {
                return -delta;
            }
        } else if (orientation == Enums.Orientation.VERTICAL) {
            if (direction == Enums.Direction.UP) {
                return delta;
            } else if (direction == Enums.Direction.DOWN) {
                return -delta;
            }
        }
        return 0;
    }

    public static final boolean setFacing(Directional directional, Enums.Direction setTo) {
        if (directional.getFacing() != setTo){
            directional.setFacing(setTo);
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

    public static boolean movingOppositeDirectionFacing (float delta, Enums.Direction facing) {
        return (facing == Enums.Direction.RIGHT && delta < 0) || (facing == Enums.Direction.LEFT && delta > 0);
    }

    public static final boolean overlapsXVals(Entity entity, float delta, float halfWidth) {
        return ((delta - halfWidth) <= entity.getRight() && (delta + halfWidth) >= entity.getLeft());
    }

    public static final boolean overlapsXVals(float leftSide, float rightSide, float delta, float halfWidth) {
        return (((delta - halfWidth) <= rightSide) && ((delta + halfWidth) >= leftSide));
    }

    public static final boolean centeredOverXVals(Entity entity, float delta, float halfWidth) {
        return ((delta + halfWidth) <= entity.getRight() && (delta - halfWidth) >= entity.getLeft())
                || (Math.abs(delta - entity.getPosition().x) < 5);
    }

    public static final boolean centeredOverXVals(float leftSide, float rightSide, float delta, float halfWidth) {
        return ((delta + halfWidth) <= rightSide && (delta - halfWidth) >= leftSide)
                || (Math.abs(delta - ((leftSide + rightSide) / 2)) < 5);
    }

    public static final boolean equilateralWithinBounds(Entity entity, Vector2 position, float radius) {
        return ((position.x - radius) <= entity.getRight() && (position.x + radius) >= entity.getLeft())
                && ((position.y - radius) <= entity.getTop() && (position.y + radius) >= entity.getBottom());
    }

    public static final boolean equilateralWithinBounds(Entity entity, float x, float y, float radius) {
        return ((x - radius) <= entity.getRight() && (x + radius) >= entity.getLeft())
                && ((y - radius) <= entity.getTop() && (y + radius) >= entity.getBottom());
    }

    public static final int useAmmo(Enums.AmmoIntensity intensity) {
        if (intensity == Enums.AmmoIntensity.BLAST) {
            return 3;
        } else if (intensity == Enums.AmmoIntensity.SHOT) {
            return 1;
        }
        return 0;
    }

    public static final void applyDamage(DestructibleHazard destructible, Enums.AmmoIntensity ammoIntensity, int damage) {
        if (!(destructible instanceof Orben && !(((Orben) destructible).isActive()))) {
            if (ammoIntensity == Enums.AmmoIntensity.BLAST) {
                destructible.setHealth(destructible.getHealth() - damage);
            } else {
                destructible.setHealth((int) (destructible.getHealth() - (damage * .67f)));
            }
        }
    }

    public static Enums.TypeEffectiveness getAmmoEffectiveness(Enums.WeaponType enemyType, Enums.WeaponType ammoType) {
        if (enemyType == ammoType) {
            return Enums.TypeEffectiveness.WEAK;
        }
        if ((enemyType == Enums.WeaponType.METAL && ammoType == Enums.WeaponType.FIRE)
        || (enemyType == Enums.WeaponType.RUBBER && ammoType == Enums.WeaponType.METAL)
        || (enemyType == Enums.WeaponType.ELECTRIC && ammoType == Enums.WeaponType.RUBBER)
        || (enemyType == Enums.WeaponType.WATER && ammoType == Enums.WeaponType.ELECTRIC)
        || (enemyType == Enums.WeaponType.FIRE && ammoType == Enums.WeaponType.WATER)
        || (ammoType == Enums.WeaponType.PSYCHIC)) {
            return Enums.TypeEffectiveness.STRONG;
        }
        return Enums.TypeEffectiveness.NORMAL;
    }

    public static final String stopWatchToString(StopWatch time) {
        String timeString = time + "";
        return (timeString).substring(0, timeString.length() - 4);
    }
}
