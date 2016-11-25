package com.udacity.gamedev.gigagal.util;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
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

    public static final float getLateralVelocity(float delta, Enums.Direction facing) {
        if (facing == Enums.Direction.RIGHT) {
            return delta;
        } else {
            return -delta;
        }
    }

    public static final boolean setDirection(GigaGal gigaGal, Enums.Direction setTo) {
        if (gigaGal.getDirection() != setTo){
            gigaGal.setDirection(setTo);
            return true;
        } else {
            return false;
        }
    }

    public static final Enums.Direction getOppositeDirection(Enums.Direction direction) {
        if (direction == Enums.Direction.LEFT) {
            return Enums.Direction.RIGHT;
        } else {
            return Enums.Direction.LEFT;
        }
    }

    public static final boolean bisectsLaterally(Physical entity, float delta, float velocity) {
        float frontHalf = Constants.GIGAGAL_STANCE_WIDTH / 2;
        if (velocity <= 0) {
            if ((delta - frontHalf) <= entity.getRight()) {
                return true;
            }
        } else if (velocity >= 0) {
            if ((delta + frontHalf) >= entity.getLeft()) {
                return true;
            }
        }
        return false;
    }
}
