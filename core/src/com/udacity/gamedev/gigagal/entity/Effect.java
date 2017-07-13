
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
public class Effect extends Entity {

    //fields
    public static final String TAG = com.udacity.gamedev.gigagal.entity.Effect.class.getName();
    private final Vector2 position;
    private final Enums.Material type;
    private final Animation animation;
    private final long startTime;

    // ctor
    public Effect(Vector2 position, Enums.Material type) {
        this.position = position;
        this.type = type;
        startTime = TimeUtils.nanoTime();

         animation = Assets.getInstance().getImpactAssets().impactGas;
//        switch (this.type) {
//            case PLASMA:
//                break;
//            case GAS:
//                animation = Assets.getInstance().getEffectAssets().effectGas;
//                break;
//            case LIQUID:
//                animation = Assets.getInstance().getEffectAssets().effectLiquid;
//                break;
//            case SOLID:
//                animation = Assets.getInstance().getEffectAssets().effectSolid;
//                break;
//            case ANTIMATTER:
//                animation = Assets.getInstance().getEffectAssets().effectPsychic;
//                break;
//            case HYBRID:
//                animation = Assets.getInstance().getEffectAssets().effectHybrid;
//                break;
//            case NATIVE:
//                animation = Assets.getInstance().getEffectAssets().effectNative;
//                break;
//            default:
//                animation = Assets.getInstance().getEffectAssets().effectNative;
//        }
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

