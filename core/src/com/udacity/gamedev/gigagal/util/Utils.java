package com.udacity.gamedev.gigagal.util;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.udacity.gamedev.gigagal.entities.DestructibleHazard;
import com.udacity.gamedev.gigagal.entities.Multidirectional;
import com.udacity.gamedev.gigagal.entities.MultidirectionalX;
import com.udacity.gamedev.gigagal.entities.MultidirectionalY;
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
        if (direction == Enums.Direction.LEFT) {
            return Enums.Direction.RIGHT;
        } else if (direction == Enums.Direction.RIGHT)  {
            return Enums.Direction.LEFT;
        } else if (direction == Enums.Direction.UP) {
            return Enums.Direction.DOWN;
        } else if (direction == Enums.Direction.DOWN) {
            return Enums.Direction.UP;
        }
        return null;
    }

    public static final boolean changeDirectional(Multidirectional multidirectional, Enums.Direction setTo, Enums.Orientation orientation) {
        if (orientation == Enums.Orientation.X) {
            MultidirectionalX multidirectionalX = (MultidirectionalX) multidirectional;
            if (multidirectionalX.getDirectionX() != setTo) {
                multidirectionalX.setDirectionX(setTo);
                return true;
            } else {
                return false;
            }
        } else if (orientation == Enums.Orientation.Y) {
            MultidirectionalY multidirectionalY = (MultidirectionalY) multidirectional;
            if (multidirectionalY.getDirectionY() != setTo) {
                multidirectionalY.setDirectionY(setTo);
                return true;
            } else {
                return false;
            }  
        }
        return false;
    }

    public static final boolean movingOppositeDirection (float velocity, Enums.Direction facing, Enums.Orientation orientation) {
        if (orientation == Enums.Orientation.X) {
            return (facing == Enums.Direction.RIGHT && velocity < 0) || (facing == Enums.Direction.LEFT && velocity > 0);
        } else if (orientation == Enums.Orientation.Y) {
            return (facing == Enums.Direction.UP && velocity < 0) || (facing == Enums.Direction.DOWN && velocity > 0);
        }
        return false;
    }

    public static final boolean overlapsBetweenTwoSides(float lowerBound, float upperBound, float position, float halfSpan) {
        return (((position - halfSpan) <= upperBound) && ((position + halfSpan) >= lowerBound));
    }

    public static final boolean centeredBetweenTwoSides(float lowerBound, float upperBound, float position, float halfSpan) {
        return ((position + halfSpan) <= lowerBound && (position - halfSpan) >= upperBound)
                || (Math.abs(position - ((upperBound + lowerBound) / 2)) < 5);
    }
    
    public static final boolean overlapsBetweenFourSides(float left, float right, float bottom, float top, float x, float y, float halfWidth, float halfHeight) {
        return (overlapsBetweenTwoSides(left, right, x, halfWidth) && overlapsBetweenTwoSides(bottom, top, y, halfHeight));
    }

    public static final boolean centeredBetweenFourSides(float left, float right, float bottom, float top, float x, float y, float halfWidth, float halfHeight) {
        return (centeredBetweenTwoSides(left, right, x, halfWidth) && centeredBetweenTwoSides(bottom, top, y, halfHeight));
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
