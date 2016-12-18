package com.udacity.gamedev.gigagal.overlays;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.Level;
import com.udacity.gamedev.gigagal.entities.GigaGal;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Utils;

public class ContextIndicatorHud {

    // fields
    private final Viewport viewport;
    private final BitmapFont font;
    private final Level level;
    private final GigaGal gigaGal;


    public ContextIndicatorHud(Level level) {
        this.level = level;
        this.gigaGal = level.getGigaGal();
        this.viewport = new ExtendViewport(
                Constants.ONSCREEN_CONTROLS_VIEWPORT_SIZE,
                Constants.ONSCREEN_CONTROLS_VIEWPORT_SIZE);
        font = new BitmapFont();
        font.getData().setScale(1);
    }

    public void render(SpriteBatch batch) {
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        float drawPositionX = viewport.getWorldWidth() / 2;
        final float drawPositionY = viewport.getWorldHeight() - Constants.HUD_MARGIN;
        Vector2 drawPosition = new Vector2(drawPositionX, drawPositionY);
        if (!gigaGal.getRicochetStatus())  {
            if (!gigaGal.getJumpStatus() && gigaGal.getHoverStatus()) {
                Utils.drawTextureRegion(
                        batch,
                        Assets.getInstance().getHudAssets().hover,
                        drawPosition,
                        Constants.BUTTON_CENTER
                );
            }
        } else {
            Utils.drawTextureRegion(
                    batch,
                    Assets.getInstance().getHudAssets().ricochet,
                    drawPosition,
                    Constants.BUTTON_CENTER
            );
        }

        drawPosition.set(drawPositionX - 50, drawPositionY);
/*        if (!gigaGal.isCharged()) {
            Utils.drawTextureRegion(
                    batch,
                    Assets.getInstance().getHudAssets().shoot,
                    drawPosition,
                    Constants.BUTTON_CENTER
            );
        } else {
            Utils.drawTextureRegion(
                    batch,
                    Assets.getInstance().getHudAssets().blast,
                    drawPosition,
                    Constants.BUTTON_CENTER
            );
        }*/
        batch.end();
    }

    public final Viewport getViewport() { return viewport; }

}
