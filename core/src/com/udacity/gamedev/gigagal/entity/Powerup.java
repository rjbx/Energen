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

// immutable
public class Powerup implements Physical, Visible {

    // fields
    private final Vector2 position;
    private final Vector2 center;
    private final Enums.PowerupType type;
    private final Animation animation;
    private boolean state;
    private long startTime;

    // ctor
    public Powerup(Vector2 position, Enums.PowerupType type) {
        startTime = TimeUtils.nanoTime();
        this.position = position;
        this.type = type;
        state = true;
        switch (this.type) {
            case HEALTH:
                animation = Assets.getInstance().getPowerupAssets().healthPowerup;
                center = Constants.HEALTH_POWERUP_CENTER;
                break;
            case TURBO:
                animation = Assets.getInstance().getPowerupAssets().turboPowerup;
                center = Constants.TURBO_POWERUP_CENTER;
                break;
            case AMMO:
                animation = Assets.getInstance().getPowerupAssets().ammoPowerup;
                center = Constants.AMMO_POWERUP_CENTER;
                break;
            case LIFE:
                animation = Assets.getInstance().getPowerupAssets().lifePowerup;
                center = Constants.LIFE_POWERUP_CENTER;
                break;
            case CANNON:
                animation = Assets.getInstance().getPowerupAssets().cannonPowerup;
                center = Constants.CANNON_POWERUP_CENTER;
                break;
            default:
                animation = Assets.getInstance().getPowerupAssets().ammoPowerup;
                center = Constants.AMMO_POWERUP_CENTER;
        }
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        Helpers.drawTextureRegion(batch, viewport, animation.getKeyFrame(Helpers.secondsSince(startTime), true), position, center);
    }

    public void deactivate() { this.state = false; }
    public boolean isActive() { return this.state; }
    public Enums.PowerupType getType() { return this.type; }
    @Override public final Vector2 getPosition() { return position; }
    @Override public final float getWidth() { return center.x * 2; }
    @Override public final float getHeight() { return center.y * 2; }
    @Override public final float getLeft() { return position.x - center.x; }
    @Override public final float getRight() { return position.x + center.x; }
    @Override public final float getTop() { return position.y + center.y; }
    @Override public final float getBottom() { return position.y - center.y; }
}
