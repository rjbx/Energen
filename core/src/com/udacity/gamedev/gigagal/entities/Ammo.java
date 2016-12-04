package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.udacity.gamedev.gigagal.Level;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Enums.ShotIntensity;
import com.udacity.gamedev.gigagal.util.Enums.Direction;
import com.udacity.gamedev.gigagal.util.Utils;

// immutable
public final class Ammo implements Physical {

    // fields
    public final static String TAG = Ammo.class.getName();
    private final Level level;
    private final ShotIntensity shotIntensity;
    private final Direction direction;
    private final Vector2 position;
    private boolean active;

    // ctor
    public Ammo(Level level, Vector2 position, Direction direction, ShotIntensity shotIntensity, Enums.Weapon weapon) {
        this.level = level;
        this.position = position;
        this.direction = direction;
        this.shotIntensity = shotIntensity;
        active = true;
    }

    public void update(float delta) {
        switch (direction) {
            case LEFT:
                position.x -= delta * Constants.BULLET_MOVE_SPEED;
                break;
            case RIGHT:
                position.x += delta * Constants.BULLET_MOVE_SPEED;
                break;
        }

        for (Destructible destructible : level.getDestructibles()) {
            if (position.dst(destructible.getPosition()) < destructible.getShotRadius()) {
                level.spawnExplosion(position);
                active = false;
                switch (shotIntensity) {
                    case NORMAL:
                        if (destructible instanceof Zoomba) {
                            destructible.setHealth(destructible.getHealth() - Constants.ZOOMBA_MAX_HEALTH / 3);
                        }
                        break;
                    case CHARGED:
                        if (destructible instanceof Zoomba) {
                            destructible.setHealth(destructible.getHealth() - Constants.ZOOMBA_MAX_HEALTH);
                        }
                        break;
                }
                level.setScore(level.getScore() + destructible.getHitScore());
            }
        }

        final float halfWorldWidth = level.getViewport().getWorldWidth() / 2;
        final float cameraX = level.getViewport().getCamera().position.x;

        if (position.x < cameraX - halfWorldWidth || position.x > cameraX + halfWorldWidth) {
            active = false;
        }
    }

    public void render(SpriteBatch batch) {
        TextureRegion region = null;
        Vector2 bulletCenter = new Vector2();
        switch (shotIntensity) {
            case NORMAL:
                region = Assets.getInstance().getAmmoAssets().nativeShot;
                bulletCenter.set(Constants.BULLET_CENTER);
                break;
            case CHARGED:
                region = Assets.getInstance().getAmmoAssets().nativeBlast;
                bulletCenter.set(Constants.CHARGE_BULLET_CENTER);
                break;
        }
        Utils.drawTextureRegion(batch, region, position, bulletCenter);
    }

    public final boolean isActive() { return active; }
    public final Vector2 getPosition() { return position; }
    public final float getWidth() { return Constants.POWERUP_CENTER.x * 2; }
    public final float getHeight() { return Constants.POWERUP_CENTER.y * 2; }
    public final float getLeft() { return position.x - Constants.POWERUP_CENTER.x; }
    public final float getRight() { return position.x + Constants.POWERUP_CENTER.x; }
    public final float getTop() { return position.y + Constants.POWERUP_CENTER.y; }
    public final float getBottom() { return position.y - Constants.POWERUP_CENTER.y; }
    public final ShotIntensity getShotIntensity() { return shotIntensity; }
}
