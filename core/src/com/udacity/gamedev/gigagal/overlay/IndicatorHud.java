package com.udacity.gamedev.gigagal.overlay;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.udacity.gamedev.gigagal.app.LevelUpdater;
import com.udacity.gamedev.gigagal.entity.Ammo;
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

    public void render(SpriteBatch batch, BitmapFont font, ExtendViewport viewport, LevelUpdater level) {

        Vector2 drawPosition = new Vector2(viewport.getCamera().position.x, viewport.getCamera().position.y + viewport.getWorldHeight() / 3);
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
            Helpers.drawTextureRegion(
                    batch,
                    viewport,
                    lifeIcon,
                    viewport.getCamera().position.x - viewport.getWorldWidth() / 2.5f,
                    viewport.getCamera().position.y + viewport.getWorldHeight() / 3,
                    Constants.SHOT_CENTER.x,
                    Constants.SHOT_CENTER.y
            );
        }

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

        Helpers.drawTextureRegion(
                batch,
                viewport, ammo.getTexture(),
                viewport.getCamera().position.x + viewport.getWorldWidth() / 2.5f,
                viewport.getCamera().position.y + viewport.getWorldHeight() / 3,
                offset.x,
                offset.y,
                Constants.AMMO_ICON_SCALE
        );

        final String scoreString = level.getScore() + "";
        final String timerString = Helpers.secondsToString(level.getTime());
        Helpers.drawBitmapFont(batch, viewport, font, scoreString, viewport.getCamera().position.x, viewport.getCamera().position.y - viewport.getWorldHeight() / 2.2f, Align.center);
        Helpers.drawBitmapFont(batch, viewport, font, timerString, viewport.getCamera().position.x, viewport.getCamera().position.y - viewport.getWorldHeight() / 2.8f, Align.center);
    }
}
