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

// mutable
public class Effect extends Entity {

    //fields
    public static final String TAG = Effect.class.getName();
    private final Vector2 position;
    private final Enums.Energy type;
    private final Animation<TextureRegion> animation;
    private final long startTime;

    // ctor
    public Effect(Vector2 position, Enums.Energy type) {
        this.position = position;
        this.type = type;
        startTime = TimeUtils.nanoTime();
        animation = AssetManager.getInstance().getImpactAssets().impactGas;
//        switch (this.type) {
//            case PLASMA:
//                break;
//            case GAS:
//                animation = AssetManager.getInstance().getEffectAssets().effectGas;
//                break;
//            case LIQUID:
//                animation = AssetManager.getInstance().getEffectAssets().effectLiquid;
//                break;
//            case SOLID:
//                animation = AssetManager.getInstance().getEffectAssets().effectSolid;
//                break;
//            case ANTIMATTER:
//                animation = AssetManager.getInstance().getEffectAssets().effectPsychic;
//                break;
//            case HYBRID:
//                animation = AssetManager.getInstance().getEffectAssets().effectHybrid;
//                break;
//            case NATIVE:
//                animation = AssetManager.getInstance().getEffectAssets().effectNative;
//                break;
//            default:
//                animation = AssetManager.getInstance().getEffectAssets().effectNative;
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

