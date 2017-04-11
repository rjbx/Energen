package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Helpers;

// mutable
public class Impact implements Entity {

    //fields
    public static final String TAG = Impact.class.getName();
    private final Vector2 position;
    private final Enums.WeaponType type;
    private final long startTime;

    // ctor
    public Impact(Vector2 position, Enums.WeaponType type) {
        this.position = position;
        this.type = type;
        startTime = TimeUtils.nanoTime();
    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion region;
        switch (type) {
            case PLASMA:
                region = Assets.getInstance().getImpactAssets().impactPlasma.getKeyFrame(Helpers.secondsSince(startTime));
                break;
            case GAS:
                region = Assets.getInstance().getImpactAssets().impact.getKeyFrame(Helpers.secondsSince(startTime));
                break;
            case LIQUID:
                region = Assets.getInstance().getImpactAssets().impactLiquid.getKeyFrame(Helpers.secondsSince(startTime));
                break;
            case SOLID:
                region = Assets.getInstance().getImpactAssets().impactSolid.getKeyFrame(Helpers.secondsSince(startTime));
                break;
            case ANTIMATTER:
                region = Assets.getInstance().getImpactAssets().impactPsychic.getKeyFrame(Helpers.secondsSince(startTime));
                break;
            case HYBRID:
                region = Assets.getInstance().getImpactAssets().impactHybrid.getKeyFrame(Helpers.secondsSince(startTime));
                break;
            case NATIVE:
                region = Assets.getInstance().getImpactAssets().impactNative.getKeyFrame(Helpers.secondsSince(startTime));
                break;
            default:
                region = Assets.getInstance().getImpactAssets().impactNative.getKeyFrame(Helpers.secondsSince(startTime));
        }
        if (!isFinished()) {
            Helpers.drawTextureRegion(
                    batch,
                    region,
                    position.x - Constants.EXPLOSION_CENTER.x,
                    position.y - Constants.EXPLOSION_CENTER.y
            );
        }
    }

    @Override public Vector2 getPosition() { return position; }
    @Override public final float getHeight() { return Constants.EXPLOSION_CENTER.y * 2; }
    @Override public final float getWidth() { return Constants.EXPLOSION_CENTER.x * 2; }
    @Override public final float getLeft() { return position.x - Constants.EXPLOSION_CENTER.x; }
    @Override public final float getRight() { return position.x + Constants.EXPLOSION_CENTER.x; }
    @Override public final float getTop() { return position.y + Constants.EXPLOSION_CENTER.y; }
    @Override public final float getBottom() { return position.y - Constants.EXPLOSION_CENTER.y; }
    public boolean isFinished() { return Assets.getInstance().getImpactAssets().impact.isAnimationFinished(Helpers.secondsSince(startTime)); }
}
