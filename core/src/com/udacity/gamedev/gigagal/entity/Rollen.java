package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.app.LevelUpdater;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Helpers;
import com.badlogic.gdx.graphics.g2d.Animation;

public class Rollen extends Roller {

    // fields
    public final static String TAG = Rollen.class.getName();

    private Enums.Direction xDirection;
    private float health;
    private Animation animation;

    // ctor
    public Rollen(LevelUpdater level, Vector2 position, Enums.Material type) {
        super(level, position, type);
        health = Constants.ROLLEN_MAX_HEALTH;
        switch (type) {
            case ORE:
                animation = Assets.getInstance().getRollenAssets().oreRollen;
                break;
            case PLASMA:
                animation = Assets.getInstance().getRollenAssets().plasmaRollen;
                break;
            case GAS:
                animation = Assets.getInstance().getRollenAssets().gasRollen;
                break;
            case LIQUID:
                animation = Assets.getInstance().getRollenAssets().liquidRollen;
                break;
            case SOLID:
                animation = Assets.getInstance().getRollenAssets().solidRollen;
                break;
            default:
                animation = Assets.getInstance().getRollenAssets().oreRollen;
        }
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        if (xDirection == Enums.Direction.RIGHT) {
            animation.setPlayMode(Animation.PlayMode.REVERSED);
        } else {
            animation.setPlayMode(Animation.PlayMode.NORMAL);
        }

        Helpers.drawTextureRegion(batch, viewport, animation.getKeyFrame(rollTimeSeconds, true), position, Constants.ROLLEN_CENTER, Constants.ROLLEN_TEXTURE_SCALE);
    }

    @Override public Vector2 getPosition() { return position; }
    @Override public final float getHealth() { return health; }
    @Override public final float getWidth() { return Constants.ROLLEN_CENTER.x * 2; }
    @Override public final float getHeight() { return Constants.ROLLEN_CENTER.y * 2; }
    @Override public final float getLeft() { return position.x - Constants.ROLLEN_CENTER.x; }
    @Override public final float getRight() { return position.x + Constants.ROLLEN_CENTER.x; }
    @Override public final float getTop() { return position.y + Constants.ROLLEN_CENTER.y; }
    @Override public final float getBottom() { return position.y - Constants.ROLLEN_CENTER.y; }
    @Override public final float getShotRadius() { return Constants.ROLLEN_SHOT_RADIUS; }
    @Override public final int getHitScore() { return Constants.ROLLEN_HIT_SCORE; }
    @Override public final int getKillScore() { return Constants.ROLLEN_KILL_SCORE; }
    @Override public final int getDamage() { return Constants.ROLLEN_STANDARD_DAMAGE; }
    @Override public final Vector2 getKnockback() { return Constants.ROLLEN_KNOCKBACK; }
    @Override public final void setHealth( float health ) { this.health = health; }
    @Override public Enums.Direction getDirectionX() { return xDirection; }
    @Override public void setDirectionX(Enums.Direction direction) { xDirection = direction; }
    public final long getStartTime() { return startTime; }
}