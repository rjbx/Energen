package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Utils;

// mutable
public class Impact implements Entity {

    //fields
    public static final String TAG = Impact.class.getName();
    private final Vector2 position;
    private final Enums.WeaponType type;
    private final long startTime;
    private float offset = 0;

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
                region = Assets.getInstance().getImpactAssets().impactPlasma.getKeyFrame(Utils.secondsSince(startTime) + offset);
                break;
            case GAS:
                region = Assets.getInstance().getImpactAssets().impactGas.getKeyFrame(Utils.secondsSince(startTime) + offset);
                break;
            case LIQUID:
                region = Assets.getInstance().getImpactAssets().impactLiquid.getKeyFrame(Utils.secondsSince(startTime) + offset);
                break;
            case SOLID:
                region = Assets.getInstance().getImpactAssets().impactSolid.getKeyFrame(Utils.secondsSince(startTime) + offset);
                break;
            case NATIVE:
                region = Assets.getInstance().getImpactAssets().impactNative.getKeyFrame(Utils.secondsSince(startTime) + offset);
                break;
            default:
                region = Assets.getInstance().getImpactAssets().impactNative.getKeyFrame(Utils.secondsSince(startTime) + offset);
        }
        if (!isFinished() && !yetToStart()) {
            Utils.drawTextureRegion(
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
    public float getOffset() { return offset; }
    public void setOffset(float offset) { this.offset = offset; }
    public boolean yetToStart(){ return Utils.secondsSince(startTime) - offset < 0; }
    public boolean isFinished() {
        float elapsedTime = Utils.secondsSince(startTime) - offset;
        return Assets.getInstance().getImpactAssets().impactGas.isAnimationFinished(elapsedTime);
    }
}
