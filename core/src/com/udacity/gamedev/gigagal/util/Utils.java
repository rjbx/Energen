package com.udacity.gamedev.gigagal.util;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.udacity.gamedev.gigagal.entities.DestructibleHazard;
import com.udacity.gamedev.gigagal.entities.MultidirectionalX;
import com.udacity.gamedev.gigagal.entities.MultidirectionalY;
import com.udacity.gamedev.gigagal.entities.Multidirectional;
import com.udacity.gamedev.gigagal.entities.Orben;

// immutable static
public final class Utils {

    // non-instantiable; cannot be subclassed
    private Utils() {}

    public static final void drawTextureRegion(SpriteBatch batch, TextureRegion region, float x, float y) {
        drawTextureRegion(batch, region, x, y, region.getRegionWidth(), region.getRegionHeight(), 1, 1);
    }

    public static final void drawTextureRegion(SpriteBatch batch, TextureRegion region, Vector2 position) {
        drawTextureRegion(batch, region, position.x, position.y, region.getRegionWidth(), region.getRegionHeight(), 1, 1);
    }

    public static final void drawTextureRegion(SpriteBatch batch, TextureRegion region, Vector2 position, Vector2 offset) {
        drawTextureRegion(batch, region, position.x - offset.x, position.y - offset.y, region.getRegionWidth(), region.getRegionHeight(), 1, 1);
    }

    public static final void drawTextureRegion(SpriteBatch batch, TextureRegion region, Vector2 position, Vector2 offset, float scale) {
        drawTextureRegion(batch, region, position.x - offset.x, position.y - offset.y, region.getRegionWidth(), region.getRegionHeight(), scale, scale);
    }

    public static final void drawTextureRegion(SpriteBatch batch, TextureRegion region, Vector2 position, Vector2 offset, Vector2 scale) {
        drawTextureRegion(batch, region, position.x - offset.x, position.y - offset.y, region.getRegionWidth(), region.getRegionHeight(), scale.x, scale.y);
    }

    public static final void drawTextureRegion(SpriteBatch batch, TextureRegion region, float x, float y, float width, float height, float scaleX, float scaleY) {
        batch.draw(
                region.getTexture(),
                x,
                y,
                0,
                0,
                width,
                height,
                scaleX,
                scaleY,
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
        if (orientation == Enums.Orientation.X) {
            if (direction == Enums.Direction.RIGHT) {
                return delta;
            } else if (direction == Enums.Direction.LEFT) {
                return -delta;
            }
        } else if (orientation == Enums.Orientation.Y) {
            if (direction == Enums.Direction.UP) {
                return delta;
            } else if (direction == Enums.Direction.DOWN) {
                return -delta;
            }
        }
        return 0;
    }

    public static final Enums.Direction getOppositeDirection(Enums.Direction direction) {
        switch (direction) {
            case LEFT:
                return Enums.Direction.RIGHT;
            case RIGHT:
                return Enums.Direction.LEFT;
            case DOWN:
                return Enums.Direction.UP;
            case UP:
                return Enums.Direction.DOWN;
            default:
                return null;
        }
    }

    public static final boolean changeDirection(Multidirectional multidirectional, Enums.Direction setTo, Enums.Orientation orientation) {
        if (orientation == Enums.Orientation.X) {
            MultidirectionalX multidirectionalX = (MultidirectionalX) multidirectional;
            if (multidirectionalX.getDirectionX() != setTo) {
                multidirectionalX.setDirectionX(setTo);
                return true;
            }
            return false;
        } else if (orientation == Enums.Orientation.Y) {
            MultidirectionalY multidirectionalY = (MultidirectionalY) multidirectional;
            if (multidirectionalY.getDirectionY() != setTo) {
                multidirectionalY.setDirectionY(setTo);
                return true;
            }
            return false;
        }
        return false;
    }

    public static final boolean movingOppositeDirection (float velocity, Enums.Direction xDirection, Enums.Orientation orientation) {
        if (orientation == Enums.Orientation.X) {
            return (xDirection == Enums.Direction.RIGHT && velocity < 0) || (xDirection == Enums.Direction.LEFT && velocity > 0);
        } else if (orientation == Enums.Orientation.Y) {
            return (xDirection == Enums.Direction.UP && velocity < 0) || (xDirection == Enums.Direction.DOWN && velocity > 0);
        }
        return false;
    }

    public static final boolean betweenTwoValues(float position, float lowerBound, float upperBound) {
        if (lowerBound < upperBound) {
            return position >= lowerBound && position <= upperBound;
        }
        return false;
    }

    public static final boolean overlapsBetweenTwoSides(float position, float halfSpan, float lowerBound, float upperBound) {
        return betweenTwoValues(position, lowerBound - halfSpan, upperBound + halfSpan);
    }

    public static final boolean encompassedBetweenTwoSides(float position, float halfSpan, float lowerBound, float upperBound) {
        return betweenTwoValues(position, lowerBound + halfSpan, upperBound - halfSpan);
    }

    public static final boolean overlapsBetweenFourSides(Vector2 position, float halfWidth, float halfHeight, float left, float right, float bottom, float top) {
        return (overlapsBetweenTwoSides(position.x, halfWidth, left, right) && overlapsBetweenTwoSides(position.y, halfHeight, bottom, top));
    }

    public static final boolean betweenFourValues(Vector2 position, float left, float right, float bottom, float top) {
        return (betweenTwoValues(position.x, left, right) && betweenTwoValues(position.y, bottom, top));
    }

    public static final boolean encompassedBetweenFourSides(Vector2 position, float halfWidth, float halfHeight, float left, float right, float bottom, float top) {
        return (encompassedBetweenTwoSides(position.x, halfWidth, left, right) && encompassedBetweenTwoSides(position.y, halfHeight, bottom, top));
    }

    public static final int useAmmo(Enums.AmmoIntensity intensity) {
        if (intensity == Enums.AmmoIntensity.BLAST) {
            return 3;
        } else if (intensity == Enums.AmmoIntensity.SHOT) {
            return 1;
        }
        return 0;
    }

    public static final void applyDamage(DestructibleHazard destructible, Enums.AmmoIntensity ammoIntensity, float damage) {
        if (!(destructible instanceof Orben && !(((Orben) destructible).isActive()))) {
            if (ammoIntensity == Enums.AmmoIntensity.BLAST) {
                destructible.setHealth(destructible.getHealth() - damage);
            } else {
                destructible.setHealth((destructible.getHealth() - (damage * .67f)));
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

    public static final String stopWatchToString(Timer time) {
        String timeString = time + "";
        return (timeString).substring(0, timeString.length() - 4);
    }

    public static boolean toggleBoolean(Boolean b) {
        if (b) {
            return false;
        } else {
            return true;
        }
    }
}