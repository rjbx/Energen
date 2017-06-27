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

// mutable
public class Impact extends com.udacity.gamedev.gigagal.app.Entity {

    //fields
    public static final String TAG = Impact.class.getName();
    private final Vector2 position;
    private final Enums.Material type;
    private final Animation animation;
    private final long startTime;

    // ctor
    public Impact(Vector2 position, Enums.Material type) {
        this.position = position;
        this.type = type;
        startTime = TimeUtils.nanoTime();
        switch (this.type) {
            case PLASMA:
                animation = Assets.getInstance().getImpactAssets().impactPlasma;
                break;
            case GAS:
                animation = Assets.getInstance().getImpactAssets().impactGas;
                break;
            case LIQUID:
                animation = Assets.getInstance().getImpactAssets().impactLiquid;
                break;
            case SOLID:
                animation = Assets.getInstance().getImpactAssets().impactSolid;
                break;
            case ANTIMATTER:
                animation = Assets.getInstance().getImpactAssets().impactPsychic;
                break;
            case HYBRID:
                animation = Assets.getInstance().getImpactAssets().impactHybrid;
                break;
            case NATIVE:
                animation = Assets.getInstance().getImpactAssets().impactNative;
                break;
            default:
                animation = Assets.getInstance().getImpactAssets().impactNative;
        }
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        if (!isFinished()) {
            Helpers.drawTextureRegion(
                    batch,
                    viewport,
                    animation.getKeyFrame(Helpers.secondsSince(startTime)),
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
    public boolean isFinished() { return Constants.IMPACT_DURATION < Helpers.secondsSince(startTime); }
}
