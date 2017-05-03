package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Helpers;

// immutable
public class Powerup implements Entity {
    // fields
    private final Vector2 position;
    private final Enums.PowerupType type;
    private final TextureRegion region;
    private boolean state;

    // ctor
    public Powerup(Vector2 position, Enums.PowerupType type) {
        this.position = position;
        this.type = type;
        state = true;
        switch (this.type) {
            case HEALTH:
                region = Assets.getInstance().getPowerupAssets().healthPowerup;
                break;
            case TURBO:
                region = Assets.getInstance().getPowerupAssets().turboPowerup;
                break;
            case AMMO:
                region = Assets.getInstance().getPowerupAssets().ammoPowerup;
                break;
            case LIFE:
                region = Assets.getInstance().getPowerupAssets().lifePowerup;
                break;
            case CANNON:
                region = Assets.getInstance().getPowerupAssets().cannonPowerup;
                break;
            default:
                region = Assets.getInstance().getPowerupAssets().ammoPowerup;
        }
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        Helpers.drawTextureRegion(batch, viewport, region, position, Constants.POWERUP_CENTER);
    }

    public void deactivate() { this.state = false; }
    public boolean isActive() { return this.state; }
    public Enums.PowerupType getType() { return this.type; }
    @Override public final Vector2 getPosition() { return position; }
    @Override public final float getWidth() { return Constants.POWERUP_CENTER.x * 2; }
    @Override public final float getHeight() { return Constants.POWERUP_CENTER.y * 2; }
    @Override public final float getLeft() { return position.x - Constants.POWERUP_CENTER.x; }
    @Override public final float getRight() { return position.x + Constants.POWERUP_CENTER.x; }
    @Override public final float getTop() { return position.y + Constants.POWERUP_CENTER.y; }
    @Override public final float getBottom() { return position.y - Constants.POWERUP_CENTER.y; }
}
