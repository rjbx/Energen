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

// immutable
public class Powerup extends Entity implements Replenishing {

    // fields
    public final static String TAG = Powerup.class.getName();

    private final Vector2 position;
    private final Vector2 center;
    private final Enums.PowerupType type;
    private final Animation<TextureRegion> animation;
    private long startTime;
    private Enums.GemType gemType;
    private boolean active;

    // ctor
    public Powerup(Vector2 position, Enums.PowerupType type) {
        startTime = TimeUtils.nanoTime();
        this.position = position;
        this.type = type;
        active = true;
        switch (this.type) {
            case HEALTH:
                animation = AssetManager.getInstance().getPowerupAssets().healthPowerup;
                center = Constants.HEALTH_POWERUP_CENTER;
                break;
            case TURBO:
                animation = AssetManager.getInstance().getPowerupAssets().turboPowerup;
                center = Constants.TURBO_POWERUP_CENTER;
                break;
            case AMMO:
                animation = AssetManager.getInstance().getPowerupAssets().ammoPowerup;
                center = Constants.AMMO_POWERUP_CENTER;
                break;
            case LIFE:
                animation = AssetManager.getInstance().getPowerupAssets().lifePowerup;
                center = Constants.LIFE_POWERUP_CENTER;
                break;
            case CANNON:
                animation = AssetManager.getInstance().getPowerupAssets().cannonPowerup;
                center = Constants.CANNON_POWERUP_CENTER;
                break;
            case SUPER:
                animation = AssetManager.getInstance().getPowerupAssets().superPowerup;
                center = Constants.SUPER_POWERUP_CENTER;
                break;
            default:
                animation = AssetManager.getInstance().getPowerupAssets().ammoPowerup;
                center = Constants.AMMO_POWERUP_CENTER;
        }
    }

    public Powerup safeClone() {
        Powerup clone = new Powerup(position, type);
        clone.setClonedHashCode(hashCode());
        return clone;
    }

    // overload ctor
    public Powerup(Vector2 position, Enums.GemType gemType) {
        active = true;
        startTime = TimeUtils.nanoTime();
        type = Enums.PowerupType.GEM;
        this.gemType = gemType;
        this.position = position;
        animation = AssetManager.getInstance().getPowerupAssets().getGemTexture(gemType);
        center = Constants.GEM_CENTER;
    }

    public void update(float delta) {}

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        Helpers.drawTextureRegion(batch, viewport, animation.getKeyFrame(Helpers.secondsSince(startTime), true), position, center);
    }
//
//    public Powerup safeClone() {
//        Powerup clone = new Powerup(this.position, this.type);
//        clone.setClonedHashCode(clone.hashCode());
//        return clone;
//    }

    @Override public Enums.PowerupType getType() { return this.type; }
    @Override public final Vector2 getPosition() { return position; }
    @Override public final float getWidth() { return center.x * 2; }
    @Override public final float getHeight() { return center.y * 2; }
    @Override public final float getLeft() { return position.x - center.x; }
    @Override public final float getRight() { return position.x + center.x; }
    @Override public final float getTop() { return position.y + center.y; }
    @Override public final float getBottom() { return position.y - center.y; }
    public boolean isActive() { return active; }
    public void deactivate() { active = false; }
    public final Enums.GemType getGemType() { return gemType; }

    @Override
    public int getPriority() {
        return Constants.PRIORITY_MEDIUM;
    }
}
