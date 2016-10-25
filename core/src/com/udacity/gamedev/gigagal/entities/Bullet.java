package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.udacity.gamedev.gigagal.Level;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Enums.Direction;
import com.udacity.gamedev.gigagal.util.Utils;

public class Bullet {

    private final Direction direction;
    private final Level level;
    public boolean active;
    private Vector2 position;
    private final Enums.BulletType bulletType;

    public Bullet(Level level, Vector2 position, Direction direction, Enums.BulletType bulletType) {
        this.level = level;
        this.position = position;
        this.direction = direction;
        this.bulletType = bulletType;
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
                switch (bulletType) {
                    case REGULAR:
                        zoomba.health -= 1;
                        break;
                    case CHARGE:
                        zoomba.health -= 5;
                        break;
                }
                level.score += Constants.ZOOMBA_HIT_SCORE;
            }
        }

        final float worldWidth = level.getViewport().getWorldWidth();
        final float cameraX = level.getViewport().getCamera().position.x;

        if (position.x < cameraX - worldWidth / 2 || position.x > cameraX + worldWidth / 2) {
            active = false;
        }
    }

    public void render(SpriteBatch batch) {
        TextureRegion region = null;
        Vector2 bulletCenter = new Vector2();
        switch (bulletType) {
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
