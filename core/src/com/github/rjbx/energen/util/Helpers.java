package com.github.rjbx.energen.util;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.rjbx.energen.app.SaveData;
import com.github.rjbx.energen.entity.Aerial;
import com.github.rjbx.energen.entity.Blade;
import com.github.rjbx.energen.entity.Projectile;
import com.github.rjbx.energen.entity.Avatar;
import com.github.rjbx.energen.entity.Destructible;
import com.github.rjbx.energen.entity.Hazard;
import com.github.rjbx.energen.entity.Vehicular;
import com.github.rjbx.energen.entity.Orben;
import com.github.rjbx.energen.entity.Physical;
import com.github.rjbx.energen.entity.Roving;

import java.util.List;

import static com.github.rjbx.energen.util.Enums.Orientation.X;
import static com.github.rjbx.energen.util.Enums.Orientation.Y;
import static com.github.rjbx.energen.util.Enums.Orientation.Z;

// immutable non-instantiable static
public final class Helpers {

    // cannot be subclassed
    private Helpers() {}

    public static final void drawTextureRegion(SpriteBatch batch, Viewport viewport, TextureRegion region, Vector2 position) {
        drawTextureRegion(batch, viewport, region, position.x, position.y, region.getRegionWidth(), region.getRegionHeight(), 1, 1, 0, false, false);
    }

    public static final void drawTextureRegion(SpriteBatch batch, Viewport viewport, TextureRegion region, Vector2 position, Vector2 offset) {
        drawTextureRegion(batch, viewport, region, position.x - offset.x, position.y - offset.y, region.getRegionWidth(), region.getRegionHeight(), 1, 1, 0, false, false);
    }

    public static final void drawTextureRegion(SpriteBatch batch, Viewport viewport, TextureRegion region, Vector2 position, Vector2 offset, Vector2 scale) {
        drawTextureRegion(batch, viewport, region, position.x - offset.x, position.y - offset.y, region.getRegionWidth(), region.getRegionHeight(), scale.x, scale.y, 0, false, false);
    }

    public static final void drawTextureRegion(SpriteBatch batch, Viewport viewport, TextureRegion region, Vector2 position, Vector2 offset, float scale) {
        drawTextureRegion(batch, viewport, region, position.x - offset.x, position.y - offset.y, region.getRegionWidth(), region.getRegionHeight(), scale, scale, 0, false, false);
    }

    public static final void drawTextureRegion(SpriteBatch batch, Viewport viewport, TextureRegion region, Vector2 position, Vector2 offset, float scale, float rotation) {
        drawTextureRegion(batch, viewport, region, position.x - offset.x, position.y - offset.y, region.getRegionWidth(), region.getRegionHeight(), scale, scale, rotation, false, false);
    }

    public static final void drawTextureRegion(SpriteBatch batch, Viewport viewport, TextureRegion region, Vector2 position, Vector2 offset, float scale, float rotation, boolean flipX, boolean flipY) {
        drawTextureRegion(batch, viewport, region, position.x - offset.x, position.y - offset.y, region.getRegionWidth(), region.getRegionHeight(), scale, scale, rotation, flipX, flipY);
    }

    public static final void drawTextureRegion(SpriteBatch batch, Viewport viewport, TextureRegion region, float x, float y) {
        drawTextureRegion(batch, viewport, region, x, y, region.getRegionWidth(), region.getRegionHeight(), 1, 1, 0, false, false);
    }

    public static final void drawTextureRegion(SpriteBatch batch, Viewport viewport, TextureRegion region, float x, float y, float offset) {
        drawTextureRegion(batch, viewport, region, x - offset, y - offset, region.getRegionWidth(), region.getRegionHeight(), 1, 1, 0, false, false);
    }

    public static final void drawTextureRegion(SpriteBatch batch, Viewport viewport, TextureRegion region, float x, float y, float offsetX, float offsetY) {
        drawTextureRegion(batch, viewport, region, x - offsetX, y - offsetY, region.getRegionWidth(), region.getRegionHeight(), 1, 1, 0, false, false);
    }

    public static final void drawTextureRegion(SpriteBatch batch, Viewport viewport, TextureRegion region, float x, float y, float offsetX, float offsetY, float scale) {
        drawTextureRegion(batch, viewport, region, x - offsetX, y - offsetY, region.getRegionWidth(), region.getRegionHeight(), scale, scale, 0, false, false);
    }

    public static final void drawTextureRegion(SpriteBatch batch, Viewport viewport, TextureRegion region, float x, float y, float offsetX, float offsetY, float scale, float rotation) {
        drawTextureRegion(batch, viewport, region, x - offsetX, y - offsetY, region.getRegionWidth(), region.getRegionHeight(), scale, scale, rotation, false, false);
    }

    public static final void drawTextureRegion(SpriteBatch batch, Viewport viewport, TextureRegion region, float x, float y, float width, float height, float scaleX, float scaleY, float rotation, boolean flipX, boolean flipY) {
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
                flipX,
                flipY);
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

    public static final int booleanToDirectionalValue(boolean state) { return state? -1 : 1; }

    public static final float secondsSincePause(long pauseTime) { return secondsSince(pauseTime); }

    public static final float secondsSince(long timeNanos) { return MathUtils.nanoToSec * (TimeUtils.nanoTime() - timeNanos); }

    public static final float secondsSince(float timeNanos) { return MathUtils.nanoToSec * (TimeUtils.nanoTime() - timeNanos); }

    public static String millisToString(long milliseconds) {

        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        minutes = minutes % 60;
        seconds = seconds % 60;

        String hoursStr = String.valueOf(hours);
        String minutesStr = String.valueOf(minutes);
        String secondsStr = String.valueOf(seconds);

        return ("00" + hoursStr).substring(hoursStr.length()) + ":" +
                ("00" + minutesStr).substring(minutesStr.length()) + ":" +
                ("00" + secondsStr).substring(secondsStr.length());
    }

    public static final float speedToVelocity(float speed, Enums.Direction direction, Enums.Orientation orientation) {
        if (orientation == X) {
            if (direction == Enums.Direction.RIGHT) {
                return speed;
            } else if (direction == Enums.Direction.LEFT) {
                return -speed;
            }
        } else if (orientation == Y) {
            if (direction == Enums.Direction.UP) {
                return speed;
            } else if (direction == Enums.Direction.DOWN) {
                return -speed;
            }
        }
        return 0;
    }

    public static final float vectorToAxisValue(Vector2 vector, Enums.Orientation orientation) {
        if (orientation == X) {
            return vector.x;
        } else if (orientation == Y) {
            return vector.y;
        }
        return 0;
    }

    public static final Enums.Direction velocityToDirection(Vector2 velocity, Enums.Orientation orientation) {
        if (orientation == X) {
            if (velocity.x < 0) {
                return Enums.Direction.LEFT;
            } else if (velocity.x > 0) {
                return Enums.Direction.RIGHT;
            } else {
                return null;
            }
        } else if (orientation == Y) {
            if (velocity.y < 0) {
                return Enums.Direction.DOWN;
            } else if (velocity.y > 0) {
                return Enums.Direction.UP;
            } else {
                return null;
            }
        }
        return null;
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

    public static final Enums.Orientation directionToOrientation(Enums.Direction direction) {
        if (direction == Enums.Direction.LEFT || direction == Enums.Direction.RIGHT) {
            return X;
        } else if (direction == Enums.Direction.DOWN || direction == Enums.Direction.UP) {
            return Y;
        }
        return Z;
    }
    
    public static final Enums.Direction inputToDirection() {
        if (InputControls.getInstance().leftButtonPressed || InputControls.getInstance().leftButtonJustPressed) {
            return Enums.Direction.LEFT;
        } else if (InputControls.getInstance().rightButtonPressed || InputControls.getInstance().rightButtonJustPressed) {
            return Enums.Direction.RIGHT;
        } else if (InputControls.getInstance().downButtonPressed || InputControls.getInstance().downButtonJustPressed) {
            return Enums.Direction.DOWN;
        } else if (InputControls.getInstance().upButtonPressed || InputControls.getInstance().upButtonJustPressed) {
            return Enums.Direction.UP;
        }
        return null;
    }

    public static final boolean changeDirection(Vehicular moving, Enums.Direction setTo, Enums.Orientation orientation) {
        if (orientation == X) {
            if (moving instanceof Roving && ((Roving) moving).getDirectionX() != setTo) {
                ((Roving) moving).setDirectionX(setTo);
                return true;
            }
            return false;
        } else if (orientation == Y) {
            if (moving instanceof Aerial && ((Aerial) moving).getDirectionY() != setTo) {
                ((Aerial) moving).setDirectionY(setTo);
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
        return (!object1.equals(object2) && object1.getLeft() <= object2.getRight() && object1.getRight() >= object2.getLeft() && object1.getBottom() <= object2.getTop() + 2 && object1.getTop() >= object2.getBottom());
    }

    public static final boolean encompassesPhysicalObject(Physical object1, Physical object2) {
        return (!object1.equals(object2) && object1.getLeft() <= object2.getLeft() && object1.getRight() >= object2.getRight() && object1.getBottom() <= object2.getBottom() && object1.getTop() >= object2.getTop());
    }

    public static final int useAmmo(Enums.ShotIntensity intensity) {
        if (intensity == Enums.ShotIntensity.BLAST) {
            return 3;
        } else if (intensity == Enums.ShotIntensity.NORMAL) {
            return 1;
        }
        return 0;
    }

    public static final void applyDamage(Destructible destructible, Hazard hazard) {
        Enums.ReactionIntensity effectiveness = Helpers.getAmmoEffectiveness(destructible.getType(), hazard.getType());
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

        damage /= Constants.DIFFICULTY_MULTIPLIER[SaveData.getDifficulty()];
        if (hazard instanceof Blade) damage /= Constants.BLADE_DAMAGE_FACTOR;

        if (hazard instanceof Projectile) {
            Projectile projectile = (Projectile) hazard;
            if (!(projectile.getSource() instanceof Avatar)) {
                damage -= Constants.AMMO_WEAK_DAMAGE;
                damage /= 2;
            } else {
                projectile.setHitScore(projectile.getHitScore() + destructible.getHitScore());
            }
            if (!(destructible instanceof Orben && !(((Orben) destructible).isActive()))) {
                if (projectile.getShotIntensity() == Enums.ShotIntensity.NORMAL) {
                    damage *= .67f;
                }
            }
        }
        destructible.setHealth(destructible.getHealth() - damage);
    }

    public static Enums.ReactionIntensity getAmmoEffectiveness(Enums.Energy enemyType, Enums.Energy ammoType) {
        if (enemyType == ammoType) {
            return Enums.ReactionIntensity.WEAK;
        }
        if (
        (enemyType == Enums.Energy.PLASMA && ammoType == Enums.Energy.NATIVE)
        || (enemyType == Enums.Energy.GAS && ammoType == Enums.Energy.PLASMA)
        || (enemyType == Enums.Energy.LIQUID && ammoType == Enums.Energy.GAS)
        || (enemyType == Enums.Energy.SOLID && ammoType == Enums.Energy.LIQUID)
        || (enemyType == Enums.Energy.ORE && ammoType == Enums.Energy.SOLID)
        || (ammoType == Enums.Energy.ANTIMATTER)
        || (ammoType == Enums.Energy.HYBRID)) {
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