package com.github.rjbx.energen.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.rjbx.energen.util.AssetManager;
import com.github.rjbx.energen.util.Constants;
import com.github.rjbx.energen.util.Enums;
import com.github.rjbx.energen.util.Helpers;

public class Waves extends Ground implements Indestructible, Pourous, Hazardous {

    // fields
    public final static String TAG = Waves.class.getName();

    private Vector2 position;
    private Vector2 scale;
    private Vector2 adjustedCenter;
    private long startTime;
    private Animation<TextureRegion> animation;

    // ctor
    public Waves(Vector2 position, Vector2 scale, Vector2 adjustedCenter) {
        this.position = position;
        this.scale = scale;
        this.adjustedCenter = adjustedCenter;
        this.startTime = TimeUtils.nanoTime();
        animation = AssetManager.getInstance().getGroundAssets().waves;
    }

    @Override
    public Waves safeClone() {
        Waves clone = new Waves(position, scale, adjustedCenter);
        clone.setClonedHashCode(hashCode());
        return clone;
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        Helpers.drawTextureRegion(batch, viewport, animation.getKeyFrame(Helpers.secondsSince(startTime), true), position, adjustedCenter, scale);
    }
    @Override public final Vector2 getPosition() { return position; }
    @Override public final float getHeight() { return Constants.WAVES_CENTER.y * 2 * scale.y; }
    @Override public final float getWidth() { return Constants.WAVES_CENTER.x * 2 * scale.x; }
    @Override public final float getLeft() { return position.x - Constants.WAVES_CENTER.x * scale.x; }
    @Override public final float getRight() { return position.x + Constants.WAVES_CENTER.x * scale.x; }
    @Override public final float getTop() { return position.y + Constants.WAVES_CENTER.y * scale.y; }
    @Override public final float getBottom() { return position.y - Constants.WAVES_CENTER.y * scale.y; }
    @Override public final Enums.Energy getType() { return Enums.Energy.LIQUID; }
    @Override public final Vector2 getKnockback() { return Constants.WAVES_KNOCKBACK; }
    @Override public final int getDamage() { return Constants.WAVES_DAMAGE; }
    @Override public final boolean isDense() { return false; }

    @Override
    public int getPriority() {
        return Constants.PRIORITY_HIGH;
    }
}
