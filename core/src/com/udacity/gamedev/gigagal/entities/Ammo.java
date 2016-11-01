package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.udacity.gamedev.gigagal.Level;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums.AmmoType;
import com.udacity.gamedev.gigagal.util.Enums.DirectionalState;
import com.udacity.gamedev.gigagal.util.Utils;

// immutable
public final class Ammo {

    private final Level level;
    public boolean active;
    private Vector2 position;
    private final AmmoType ammoType;
    private final DirectionalState directionalState;

    public Ammo(Level level, Vector2 position, DirectionalState directionalState, AmmoType ammoType) {
        this.level = level;
        this.position = position;
        this.directionalState = directionalState;
        this.ammoType = ammoType;
        active = true;
    }

    public void update(float delta) {
        switch (directionalState) {
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
                region = Assets.instance.bulletAssets.bullet;
                bulletCenter.set(Constants.BULLET_CENTER);
                break;
            case CHARGE:
                region = Assets.instance.bulletAssets.chargeBullet;
                bulletCenter.set(Constants.CHARGE_BULLET_CENTER);
                break;
        }
        Utils.drawTextureRegion(batch, region, position, bulletCenter);
    }
}
