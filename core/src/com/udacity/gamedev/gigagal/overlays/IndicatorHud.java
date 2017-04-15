package com.udacity.gamedev.gigagal.overlays;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.udacity.gamedev.gigagal.app.Level;
import com.udacity.gamedev.gigagal.entities.Ammo;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Helpers;

public class IndicatorHud {

    // fields
    public final static String TAG = IndicatorHud.class.getName();
    public static final IndicatorHud INSTANCE = new IndicatorHud();

    // ctor
    private IndicatorHud() {}

    public static IndicatorHud getInstance() { return INSTANCE; }

    public void create() {}

    public void render(SpriteBatch batch, BitmapFont font, ExtendViewport viewport, Level level) {

        float drawPositionX = viewport.getWorldWidth() / 2;
        final float drawPositionY = viewport.getWorldHeight() - Constants.HUD_MARGIN - 7;
        Vector2 drawPosition = new Vector2(drawPositionX, drawPositionY);
        if (!level.getGigaGal().getClingStatus() && level.getGigaGal().getAction() != Enums.Action.CLINGING)  {
            if (!level.getGigaGal().getJumpStatus() && !level.getGigaGal().getClimbStatus() && level.getGigaGal().getHoverStatus()) {
                Helpers.drawTextureRegion(
                        batch,
                        viewport,
                        Assets.getInstance().getHudAssets().hover,
                        drawPosition,
                        Constants.ICON_CENTER
                );
            }
        } else {
            Helpers.drawTextureRegion(
                    batch,
                    viewport,
                    Assets.getInstance().getHudAssets().cling,
                    drawPosition,
                    Constants.ICON_CENTER
            );
        }

        if (level.getGigaGal().getDashStatus()) {
            Helpers.drawTextureRegion(
                    batch,
                    viewport,
                    Assets.getInstance().getHudAssets().dash,
                    drawPosition,
                    Constants.ICON_CENTER
            );
        }

        if (level.getGigaGal().getClimbStatus()) {
            Helpers.drawTextureRegion(
                    batch,
                    viewport,
                    Assets.getInstance().getHudAssets().climb,
                    drawPosition,
                    Constants.ICON_CENTER
            );
        }

        final TextureRegion lifeIcon = Assets.getInstance().getHudAssets().life;
        for (int i = 1; i <= level.getGigaGal().getLives(); i++) {
            drawPosition = new Vector2(
                    i * (Constants.HUD_MARGIN / 2 + lifeIcon.getRegionWidth()) - 15,
                    viewport.getWorldHeight() - Constants.HUD_MARGIN - lifeIcon.getRegionHeight()
            );
            Helpers.drawTextureRegion(
                    batch,
                    viewport,
                    lifeIcon,
                    drawPosition
            );
        }

        drawPosition = new Vector2(viewport.getWorldWidth() - Constants.HUD_MARGIN, viewport.getWorldHeight() - Constants.HUD_MARGIN - 7);
        Enums.WeaponType weapon = level.getGigaGal().getWeapon();
        Enums.AmmoIntensity intensity = level.getGigaGal().getAmmoIntensity();
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
        Helpers.drawTextureRegion(batch, viewport, ammo.getTexture(), drawPosition, offset, Constants.AMMO_ICON_SCALE);

        final String scoreString = level.getScore() + "";
        final String timerString = Helpers.secondsToString(level.getTime());
        Helpers.drawBitmapFont(batch, viewport, font, scoreString, viewport.getWorldWidth() / 2 - Constants.HUD_MARGIN - 0.5f, Constants.HUD_MARGIN / 2, Align.center);
        Helpers.drawBitmapFont(batch, viewport, font, timerString, viewport.getWorldWidth() / 2 - Constants.HUD_MARGIN * 2 - 0.5f, Constants.HUD_MARGIN * 1.5f, Align.center);
    }
}
