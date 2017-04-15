package com.udacity.gamedev.gigagal.util;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.entities.DestructibleHazard;
import com.udacity.gamedev.gigagal.entities.MultidirectionalX;
import com.udacity.gamedev.gigagal.entities.MultidirectionalY;
import com.udacity.gamedev.gigagal.entities.Multidirectional;
import com.udacity.gamedev.gigagal.entities.Orben;

import org.apache.commons.lang3.time.DurationFormatUtils;

// immutable non-instantiable static
public final class Helpers {

    // cannot be subclassed
    private Helpers() {}

    public static final void drawTextureRegion(SpriteBatch batch, Viewport viewport, TextureRegion region, float x, float y) {
        drawTextureRegion(batch, viewport, region, x, y, region.getRegionWidth(), region.getRegionHeight(), 1, 1, 0);
    }

    public static final void drawTextureRegion(SpriteBatch batch, Viewport viewport, TextureRegion region, Vector2 position) {
        drawTextureRegion(batch, viewport, region, position.x, position.y, region.getRegionWidth(), region.getRegionHeight(), 1, 1, 0);
    }

    public static final void drawTextureRegion(SpriteBatch batch, Viewport viewport, TextureRegion region, Vector2 position, Vector2 offset) {
        drawTextureRegion(batch, viewport, region, position.x - offset.x, position.y - offset.y, region.getRegionWidth(), region.getRegionHeight(), 1, 1, 0);
    }

    public static final void drawTextureRegion(SpriteBatch batch, Viewport viewport, TextureRegion region, Vector2 position, Vector2 offset, float scale) {
        drawTextureRegion(batch, viewport, region, position.x - offset.x, position.y - offset.y, region.getRegionWidth(), region.getRegionHeight(), scale, scale, 0);
    }

    public static final void drawTextureRegion(SpriteBatch batch, Viewport viewport, TextureRegion region, Vector2 position, Vector2 offset, float scale, float rotation) {
        drawTextureRegion(batch, viewport, region, position.x - offset.x, position.y - offset.y, region.getRegionWidth(), region.getRegionHeight(), scale, scale, rotation);
    }

    public static final void drawTextureRegion(SpriteBatch batch, Viewport viewport, TextureRegion region, Vector2 position, Vector2 offset, Vector2 scale) {
        drawTextureRegion(batch, viewport, region, position.x - offset.x, position.y - offset.y, region.getRegionWidth(), region.getRegionHeight(), scale.x, scale.y, 0);
    }

    public static final void drawTextureRegion(SpriteBatch batch, Viewport viewport, TextureRegion region, float x, float y, float width, float height, float scaleX, float scaleY, float rotation) {
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
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
                rotation,
                region.getRegionX(),
                region.getRegionY(),
                region.getRegionWidth(),
                region.getRegionHeight(),
                false,
                false);
        batch.end();
    }

    public static final void drawBitmapFont(SpriteBatch batch, Viewport viewport, BitmapFont font, String text, float xPos, float yPos, int align) {
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        font.draw(batch, text, xPos, yPos, 0, align, false);
        batch.end();
    }

    public static final void drawNinePatch(SpriteBatch batch, Viewport viewport, NinePatch ninePatch, float left, float bottom, float width, float height) {
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        ninePatch.draw(batch, left, bottom, width, height);
        batch.end();
    }

    public static final float secondsSincePause(long pauseTime) { return secondsSince(pauseTime); }

    public static final float secondsSince(long timeNanos) { return MathUtils.nanoToSec * (TimeUtils.nanoTime() - timeNanos); }

    public static String secondsToString(long seconds) {
        String timeStr = DurationFormatUtils.formatDurationHMS(seconds);
        return timeStr.substring(0, timeStr.length() - 4);
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
        if (
           (enemyType == Enums.WeaponType.SOLID && ammoType == Enums.WeaponType.LIQUID)
        || (enemyType == Enums.WeaponType.ORE && ammoType == Enums.WeaponType.SOLID)
        || (enemyType == Enums.WeaponType.PLASMA && ammoType == Enums.WeaponType.ORE)
        || (enemyType == Enums.WeaponType.LIQUID && ammoType == Enums.WeaponType.GAS)
        || (enemyType == Enums.WeaponType.GAS && ammoType == Enums.WeaponType.PLASMA)
        || (ammoType == Enums.WeaponType.ANTIMATTER)
        || (ammoType == Enums.WeaponType.HYBRID)) {
            return Enums.TypeEffectiveness.STRONG;
        }
        return Enums.TypeEffectiveness.NORMAL;
    }


    public static boolean toggleBoolean(Boolean b) {
        if (b) {
            return false;
        } else {
            return true;
        }
    }
}