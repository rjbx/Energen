package com.udacity.gamedev.gigagal.overlays;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.Level;
import com.udacity.gamedev.gigagal.entities.GigaGal;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Utils;

public class IndicatorHud {

    // fields
    private final Viewport viewport;
    private final BitmapFont font;
    private final Level level;
    private final GigaGal gigaGal;


    // ctor
    public IndicatorHud(Level level) {
        this.level = level;
        this.gigaGal = level.getGigaGal();
        this.viewport = new ExtendViewport(
                Constants.ONSCREEN_CONTROLS_VIEWPORT_SIZE,
                Constants.ONSCREEN_CONTROLS_VIEWPORT_SIZE);
        font = new BitmapFont();
        font.getData().setScale(.75f);
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

        if (gigaGal.getDashStatus()) {
            Utils.drawTextureRegion(
                    batch,
                    Assets.getInstance().getHudAssets().dash,
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

        final TextureRegion lifeIcon = Assets.getInstance().getHudAssets().life;
        for (int i = 1; i <= gigaGal.getLives(); i++) {
            drawPosition = new Vector2(
                    i * (Constants.HUD_MARGIN / 2 + lifeIcon.getRegionWidth()) - 15,
                    viewport.getWorldHeight() - Constants.HUD_MARGIN - lifeIcon.getRegionHeight()
            );
            Utils.drawTextureRegion(
                    batch,
                    lifeIcon,
                    drawPosition
            );
        }



        final String scoreString = level.getScore() + "";
        final String timerString = level.getTimer() + "";
                        /*
                Constants.HUD_AMMO_LABEL + gigaGal.getAmmo() + "\n" +
                Constants.HUD_HEALTH_LABEL + gigaGal.getHealth() + "\n" +
                "Turbo: " + gigaGal.getTurbo() + "\n" +
                Constants.HUD_WEAPON_LABEL + gigaGal.getWeapon() +
                gigaGal.getWeaponList().toString(); */
        font.draw(batch, scoreString, viewport.getWorldWidth() / 2 - Constants.HUD_MARGIN - 0.5f, Constants.HUD_MARGIN / 2, Constants.HUD_MARGIN * 2, 1, true);
        font.draw(batch, timerString, viewport.getWorldWidth() / 2 - Constants.HUD_MARGIN * 2 - 0.5f, Constants.HUD_MARGIN * 1.5f, Constants.HUD_MARGIN * 4, 1, true);
        batch.end();
    }

    public final Viewport getViewport() { return viewport; }
}
