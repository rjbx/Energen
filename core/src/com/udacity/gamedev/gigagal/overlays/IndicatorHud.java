package com.udacity.gamedev.gigagal.overlays;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.app.Level;
import com.udacity.gamedev.gigagal.entities.Ammo;
import com.udacity.gamedev.gigagal.entities.GigaGal;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Helpers;

public class IndicatorHud {

    // fields
    public final static String TAG = IndicatorHud.class.getName();
    private final SpriteBatch batch; // class-level instantiation
    private final ExtendViewport viewport; // class-level instantiation
    private final BitmapFont font; // class-level instantiation
    private final Level level;
    private final GigaGal gigaGal;

    // ctor
    public IndicatorHud(Level level) {
        this.level = level;
        this.gigaGal = level.getGigaGal();
        this.batch = new SpriteBatch();
        this.viewport = new ExtendViewport(
                Constants.CONTROLS_OVERLAY_VIEWPORT_SIZE,
                Constants.CONTROLS_OVERLAY_VIEWPORT_SIZE);
        font = new BitmapFont();
        font.getData().setScale(.75f);
    }

    public void render(SpriteBatch batch, BitmapFont font, ExtendViewport viewport) {
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        float drawPositionX = viewport.getWorldWidth() / 2;
        final float drawPositionY = viewport.getWorldHeight() - Constants.HUD_MARGIN - 7;
        Vector2 drawPosition = new Vector2(drawPositionX, drawPositionY);
        if (!gigaGal.getClingStatus() && gigaGal.getAction() != Enums.Action.CLINGING)  {
            if (!gigaGal.getJumpStatus() && !gigaGal.getClimbStatus() && gigaGal.getHoverStatus()) {
                Helpers.drawTextureRegion(
                        batch,
                        Assets.getInstance().getHudAssets().hover,
                        drawPosition,
                        Constants.ICON_CENTER
                );
            }
        } else {
            Helpers.drawTextureRegion(
                    batch,
                    Assets.getInstance().getHudAssets().cling,
                    drawPosition,
                    Constants.ICON_CENTER
            );
        }

        if (gigaGal.getDashStatus()) {
            Helpers.drawTextureRegion(
                    batch,
                    Assets.getInstance().getHudAssets().dash,
                    drawPosition,
                    Constants.ICON_CENTER
            );
        }

        if (gigaGal.getClimbStatus()) {
            Helpers.drawTextureRegion(
                    batch,
                    Assets.getInstance().getHudAssets().climb,
                    drawPosition,
                    Constants.ICON_CENTER
            );
        }

        final TextureRegion lifeIcon = Assets.getInstance().getHudAssets().life;
        for (int i = 1; i <= gigaGal.getLives(); i++) {
            drawPosition = new Vector2(
                    i * (Constants.HUD_MARGIN / 2 + lifeIcon.getRegionWidth()) - 15,
                    viewport.getWorldHeight() - Constants.HUD_MARGIN - lifeIcon.getRegionHeight()
            );
            Helpers.drawTextureRegion(
                    batch,
                    lifeIcon,
                    drawPosition
            );
        }

        drawPosition = new Vector2(viewport.getWorldWidth() - Constants.HUD_MARGIN, viewport.getWorldHeight() - Constants.HUD_MARGIN - 7);
        Enums.WeaponType weapon = gigaGal.getWeapon();
        Enums.AmmoIntensity intensity = gigaGal.getAmmoIntensity();
        Ammo ammo = new Ammo(level, new Vector2(0,0), Enums.Direction.RIGHT, Enums.Orientation.X, intensity, weapon, false);
        ammo.update(1);
        Vector2 offset = new Vector2();
        switch (intensity) {
            case SHOT:
                offset.set(Constants.SHOT_CENTER);
                offset.scl(Constants.AMMO_ICON_SCALE);
                break;
            case BLAST:
                offset.set(Constants.BLAST_CENTER);
                offset.scl(Constants.AMMO_ICON_SCALE);
                break;
            default:
                offset.set(Constants.SHOT_CENTER);
                offset.scl(Constants.AMMO_ICON_SCALE);
        }
        Helpers.drawTextureRegion(batch, ammo.getTexture(), drawPosition, offset, Constants.AMMO_ICON_SCALE);

        final String scoreString = level.getLevelScore() + "";
        final String timerString = Helpers.stopWatchToString(level.getLevelTime());
        font.draw(batch, scoreString, viewport.getWorldWidth() / 2 - Constants.HUD_MARGIN - 0.5f, Constants.HUD_MARGIN / 2, Constants.HUD_MARGIN * 2, 1, true);
        font.draw(batch, timerString, viewport.getWorldWidth() / 2 - Constants.HUD_MARGIN * 2 - 0.5f, Constants.HUD_MARGIN * 1.5f, Constants.HUD_MARGIN * 4, 1, true);

        batch.end();
    }

    public void dispose() { font.dispose(); batch.dispose(); }

    public final Viewport getViewport() { return viewport; }
}
