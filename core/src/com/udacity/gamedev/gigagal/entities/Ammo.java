package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.udacity.gamedev.gigagal.Level;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums.AmmoType;
import com.udacity.gamedev.gigagal.util.Enums.Direction;
import com.udacity.gamedev.gigagal.util.Utils;

// immutable
public final class Ammo {

    // fields
    public final static String TAG = Ammo.class.getName();
    private final Level level;
    private final AmmoType ammoType;
    private final Direction direction;
    private boolean active;
    private Vector2 position;

    // ctor
    public Ammo(Level level, Vector2 position, Direction direction, AmmoType ammoType) {
        this.level = level;
        this.position = position;
        this.direction = direction;
        this.ammoType = ammoType;
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

        for (Zoomba zoomba : level.getEnemies()) {
            if (position.dst(zoomba.position) < Constants.ZOOMBA_SHOT_RADIUS) {
                level.spawnExplosion(position);
                active = false;
                switch (ammoType) {
                    case REGULAR:
                        zoomba.health -= Constants.ZOOMBA_HEALTH / 5;
                        break;
                    case CHARGE:
                        zoomba.health -= Constants.ZOOMBA_HEALTH;
                        break;
                }
                level.score += Constants.ZOOMBA_HIT_SCORE;
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
        switch (ammoType) {
            case REGULAR:
                region = Assets.getInstance().getBulletAssets().bullet;
                bulletCenter.set(Constants.BULLET_CENTER);
                break;
            case CHARGE:
                region = Assets.getInstance().getBulletAssets().chargeBullet;
                bulletCenter.set(Constants.CHARGE_BULLET_CENTER);
                break;
        }
        Utils.drawTextureRegion(batch, region, position, bulletCenter);
    }

    public final boolean isActive() { return active; }
}
