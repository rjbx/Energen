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
public class Impact extends Entity {

    //fields
    public static final String TAG = Impact.class.getName();
    private final Vector2 position;
    private final Enums.Energy type;
    private final Animation<TextureRegion> animation;
    private final long startTime;

    // ctor
    public Impact(Vector2 position, Enums.Energy type) {
        this.position = position;
        this.type = type;
        startTime = TimeUtils.nanoTime();
        switch (this.type) {
            case PLASMA:
                animation = AssetManager.getInstance().getImpactAssets().impactPlasma;
                break;
            case GAS:
                animation = AssetManager.getInstance().getImpactAssets().impactGas;
                break;
            case LIQUID:
                animation = AssetManager.getInstance().getImpactAssets().impactLiquid;
                break;
            case SOLID:
                animation = AssetManager.getInstance().getImpactAssets().impactSolid;
                break;
            case ANTIMATTER:
                animation = AssetManager.getInstance().getImpactAssets().impactPsychic;
                break;
            case HYBRID:
                animation = AssetManager.getInstance().getImpactAssets().impactHybrid;
                break;
            case NATIVE:
                animation = AssetManager.getInstance().getImpactAssets().impactNative;
                break;
            default:
                animation = AssetManager.getInstance().getImpactAssets().impactNative;
        }
    }

    public Impact safeClone() {
        Impact clone = new Impact(position, type);
        clone.setClonedHashCode(hashCode());
        return clone;
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

    @Override
    public int getPriority() {
        return Constants.PRIORITY_OVERRIDE;
    }
}
