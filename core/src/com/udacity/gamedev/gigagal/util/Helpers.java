package com.udacity.gamedev.gigagal.util;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.app.SaveData;
import com.udacity.gamedev.gigagal.entity.Ammo;
import com.udacity.gamedev.gigagal.entity.Destructible;
import com.udacity.gamedev.gigagal.entity.MultidirectionalX;
import com.udacity.gamedev.gigagal.entity.MultidirectionalY;
import com.udacity.gamedev.gigagal.entity.Multidirectional;
import com.udacity.gamedev.gigagal.entity.Orben;
import com.udacity.gamedev.gigagal.entity.Physical;

import org.apache.commons.lang3.time.DurationFormatUtils;
import java.util.List;

import static com.udacity.gamedev.gigagal.util.Enums.Orientation.X;
import static com.udacity.gamedev.gigagal.util.Enums.Orientation.Y;

// immutable non-instantiable static
public final class Helpers {

    // cannot be subclassed
    private Helpers() {}

    public static final void drawTextureRegion(SpriteBatch batch, Viewport viewport, TextureRegion region, Vector2 position) {
        drawTextureRegion(batch, viewport, region, position.x, position.y, region.getRegionWidth(), region.getRegionHeight(), 1, 1, 0);
    }

    public static final void drawTextureRegion(SpriteBatch batch, Viewport viewport, TextureRegion region, Vector2 position, Vector2 offset) {
        drawTextureRegion(batch, viewport, region, position.x - offset.x, position.y - offset.y, region.getRegionWidth(), region.getRegionHeight(), 1, 1, 0);
    }

    public static final void drawTextureRegion(SpriteBatch batch, Viewport viewport, TextureRegion region, Vector2 position, Vector2 offset, Vector2 scale) {
        drawTextureRegion(batch, viewport, region, position.x - offset.x, position.y - offset.y, region.getRegionWidth(), region.getRegionHeight(), scale.x, scale.y, 0);
    }

    public static final void drawTextureRegion(SpriteBatch batch, Viewport viewport, TextureRegion region, Vector2 position, Vector2 offset, float scale) {
        drawTextureRegion(batch, viewport, region, position.x - offset.x, position.y - offset.y, region.getRegionWidth(), region.getRegionHeight(), scale, scale, 0);
    }

    public static final void drawTextureRegion(SpriteBatch batch, Viewport viewport, TextureRegion region, Vector2 position, Vector2 offset, float scale, float rotation) {
        drawTextureRegion(batch, viewport, region, position.x - offset.x, position.y - offset.y, region.getRegionWidth(), region.getRegionHeight(), scale, scale, rotation);
    }

    public static final void drawTextureRegion(SpriteBatch batch, Viewport viewport, TextureRegion region, float x, float y) {
        drawTextureRegion(batch, viewport, region, x, y, region.getRegionWidth(), region.getRegionHeight(), 1, 1, 0);
    }

    public static final void drawTextureRegion(SpriteBatch batch, Viewport viewport, TextureRegion region, float x, float y, float offset) {
        drawTextureRegion(batch, viewport, region, x - offset, y - offset, region.getRegionWidth(), region.getRegionHeight(), 1, 1, 0);
    }

    public static final void drawTextureRegion(SpriteBatch batch, Viewport viewport, TextureRegion region, float x, float y, float offsetX, float offsetY) {
        drawTextureRegion(batch, viewport, region, x - offsetX, y - offsetY, region.getRegionWidth(), region.getRegionHeight(), 1, 1, 0);
    }

    public static final void drawTextureRegion(SpriteBatch batch, Viewport viewport, TextureRegion region, float x, float y, float offsetX, float offsetY, float scale) {
        drawTextureRegion(batch, viewport, region, x - offsetX, y - offsetY, region.getRegionWidth(), region.getRegionHeight(), scale, scale, 0);
    }

    public static final void drawTextureRegion(SpriteBatch batch, Viewport viewport, TextureRegion region, float x, float y, float offsetX, float offsetY, float scale, float rotation) {
        drawTextureRegion(batch, viewport, region, x - offsetX, y - offsetY, region.getRegionWidth(), region.getRegionHeight(), scale, scale, rotation);
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

    public static final float secondsSince(float timeNanos) { return MathUtils.nanoToSec * (TimeUtils.nanoTime() - timeNanos); }

    public static String secondsToString(long seconds) {
        String timeStr = DurationFormatUtils.formatDurationHMS(seconds);
        return timeStr.substring(0, timeStr.length() - 4);
    }

    public static final float absoluteToDirectionalValue(float delta, Enums.Direction direction, Enums.Orientation orientation) {
        if (orientation == X) {
            if (direction == Enums.Direction.RIGHT) {
                return delta;
            } else if (direction == Enums.Direction.LEFT) {
                return -delta;
            }
        } else if (orientation == Y) {
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

    public static final Enums.Orientation getOppositeOrientation(Enums.Orientation orientation) {
        if (orientation == X) {
            return Y;
        } else if (orientation == Y) {
            return X;
        }
        return null;
    }

    public static final boolean changeDirection(Multidirectional multidirectional, Enums.Direction setTo, Enums.Orientation orientation) {
        if (orientation == X) {
            MultidirectionalX multidirectionalX = (MultidirectionalX) multidirectional;
            if (multidirectionalX.getDirectionX() != setTo) {
                multidirectionalX.setDirectionX(setTo);
                return true;
            }
            return false;
        } else if (orientation == Y) {
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
        if (orientation == X) {
            return (xDirection == Enums.Direction.RIGHT && velocity < 0) || (xDirection == Enums.Direction.LEFT && velocity > 0);
        } else if (orientation == Y) {
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

    public static final boolean overlapsPhysicalObject(Physical object1, Physical object2) {
        return (object1.getLeft() <= object2.getRight() && object1.getRight() >= object2.getLeft() && object1.getBottom() <= object2.getTop() && object1.getTop() >= object2.getBottom());
    }

    public static final int useAmmo(Enums.ShotIntensity intensity) {
        if (intensity == Enums.ShotIntensity.BLAST) {
            return 3;
        } else if (intensity == Enums.ShotIntensity.NORMAL) {
            return 1;
        }
        return 0;
    }

    public static final void applyDamage(Destructible destructible, Ammo ammo) {
        Enums.ReactionIntensity effectiveness = Helpers.getAmmoEffectiveness(destructible.getType(), ammo.getType());
        float damage;
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
        if (!ammo.isFromGigagal()) {
            damage -= Constants.AMMO_WEAK_DAMAGE;
            damage /= 2;
        } else {
            ammo.setHitScore(ammo.getHitScore() + destructible.getHitScore());
        }
        damage = damage / Constants.DIFFICULTY_MULTIPLIER[SaveData.getDifficulty()];
        if (!(destructible instanceof Orben && !(((Orben) destructible).isActive()))) {
            if (ammo.getShotIntensity() == Enums.ShotIntensity.BLAST) {
                destructible.setHealth(destructible.getHealth() - damage);
            } else {
                destructible.setHealth((destructible.getHealth() - (damage * .67f)));
            }
        }
    }

    public static Enums.ReactionIntensity getAmmoEffectiveness(Enums.Material enemyType, Enums.Material ammoType) {
        if (enemyType == ammoType) {
            return Enums.ReactionIntensity.WEAK;
        }
        if (
           (enemyType == Enums.Material.SOLID && ammoType == Enums.Material.LIQUID)
        || (enemyType == Enums.Material.ORE && ammoType == Enums.Material.SOLID)
        || (enemyType == Enums.Material.PLASMA && ammoType == Enums.Material.ORE)
        || (enemyType == Enums.Material.LIQUID && ammoType == Enums.Material.GAS)
        || (enemyType == Enums.Material.GAS && ammoType == Enums.Material.PLASMA)
        || (ammoType == Enums.Material.ANTIMATTER)
        || (ammoType == Enums.Material.HYBRID)) {
            return Enums.ReactionIntensity.STRONG;
        }
        return Enums.ReactionIntensity.NORMAL;
    }

    public static long numStrToSum(List<String> numStrList) {
        long sum = 0;
        for (String numStr : numStrList) {
            sum += Long.parseLong(numStr);
        }
        return sum;
    }
}