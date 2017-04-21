package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Helpers;

public class Lava implements IndestructibleHazard {

    // fields
    private Vector2 position;
    private Vector2 scale;
    private Vector2 adjustedCenter;
    private long startTime;
    private Animation animation;

    // ctor
    public Lava(Vector2 position, Vector2 scale, Vector2 adjustedCenter) {
        this.position = position;
        this.scale = scale;
        this.adjustedCenter = adjustedCenter;
        this.startTime = TimeUtils.nanoTime();
        animation = Assets.getInstance().getGroundAssets().lava;
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        Helpers.drawTextureRegion(batch, viewport, animation.getKeyFrame(Helpers.secondsSince(startTime), true), position, adjustedCenter, scale);
    }

    @Override public final Vector2 getPosition() { return position; }
    @Override public final float getHeight() { return Constants.LAVA_CENTER.x * 2 * scale.x; }
    @Override public final float getWidth() { return Constants.LAVA_CENTER.y * 2 * scale.y; }
    @Override public final float getLeft() { return position.x - Constants.LAVA_CENTER.x * scale.x; }
    @Override public final float getRight() { return position.x + Constants.LAVA_CENTER.x * scale.x; }
    @Override public final float getTop() { return position.y + Constants.LAVA_CENTER.y * scale.y; }
    @Override public final float getBottom() { return position.y - Constants.LAVA_CENTER.y * scale.y; }
    @Override public final Enums.WeaponType getType() { return Enums.WeaponType.LIQUID; }
    @Override public final Vector2 getKnockback() { return Constants.LAVA_KNOCKBACK; }
    @Override public final int getDamage() { return Constants.LAVA_DAMAGE; }
}
