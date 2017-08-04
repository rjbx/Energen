package com.udacity.gamedev.gigagal.overlay;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.udacity.gamedev.gigagal.app.LevelUpdater;
import com.udacity.gamedev.gigagal.entity.Ammo;
import com.udacity.gamedev.gigagal.entity.GigaGal;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Helpers;

public class IndicatorHud {

    // fields
    public final static String TAG = IndicatorHud.class.getName();
    public static final IndicatorHud INSTANCE = new IndicatorHud();
    public ExtendViewport viewport;

    // ctor
    private IndicatorHud() {}

    public static IndicatorHud getInstance() { return INSTANCE; }

    public void create() {
    }

    public void render(SpriteBatch batch, ExtendViewport viewport, BitmapFont font, LevelUpdater level) {
        float yIcon = viewport.getCamera().position.y + viewport.getWorldHeight() / 2.5f;

        float xAction = viewport.getCamera().position.x + 5;
        if ( GigaGal.getInstance().getClimbStatus()) {
            Helpers.drawTextureRegion(
                    batch,
                    viewport,
                    Assets.getInstance().getHudAssets().climb,
                    xAction,
                    yIcon,
                    Constants.ICON_CENTER.x,
                    Constants.ICON_CENTER.y,
                    Constants.ACTION_ICON_SCALE
            );
        } else if (GigaGal.getInstance().getRappelStatus() || GigaGal.getInstance().getAction() == Enums.Action.RAPPELLING)  {
            Helpers.drawTextureRegion(
                    batch,
                    viewport,
                    Assets.getInstance().getHudAssets().rappel,
                    xAction,
                    yIcon,
                    Constants.ICON_CENTER.x,
                    Constants.ICON_CENTER.y,
                    Constants.ACTION_ICON_SCALE
            );
        }  else if (!GigaGal.getInstance().getJumpStatus() && GigaGal.getInstance().getHoverStatus()) {
            Helpers.drawTextureRegion(
                    batch,
                    viewport,
                    Assets.getInstance().getHudAssets().hover,
                    xAction,
                    yIcon,
                    Constants.ICON_CENTER.x,
                    Constants.ICON_CENTER.y,
                    Constants.ACTION_ICON_SCALE
            );
        } else if (GigaGal.getInstance().getDashStatus()) {
            Helpers.drawTextureRegion(
                    batch,
                    viewport,
                    Assets.getInstance().getHudAssets().dash,
                    xAction,
                    yIcon,
                    Constants.ICON_CENTER.x,
                    Constants.ICON_CENTER.y,
                    Constants.ACTION_ICON_SCALE
            );
        } else if (GigaGal.getInstance().getMoveStatus()) {
            Helpers.drawTextureRegion(
                    batch,
                    viewport,
                    Assets.getInstance().getHudAssets().move,
                    xAction,
                    yIcon,
                    Constants.ICON_CENTER.x,
                    Constants.ICON_CENTER.y,
                    Constants.ACTION_ICON_SCALE
            );
        } else if (GigaGal.getInstance().getSwipeStatus()) {

        }

        final TextureRegion lifeIcon = Assets.getInstance().getHudAssets().life;
        float xLife = viewport.getCamera().position.x - viewport.getWorldWidth() / 2.1f;
        for (int i = 1; i <= GigaGal.getInstance().getLives(); i++) {
            Helpers.drawTextureRegion(
                    batch,
                    viewport,
                    lifeIcon,
                    xLife,
                    yIcon,
                    Constants.SHOT_CENTER.x,
                    Constants.SHOT_CENTER.y,
                    Constants.LIFE_ICON_SCALE
            );
            xLife += 20;
        }

        Enums.Material weapon = GigaGal.getInstance().getWeapon();
        Enums.ShotIntensity intensity = GigaGal.getInstance().getShotIntensity();
        Ammo ammo = new Ammo(level, new Vector2(0,0), Enums.Direction.RIGHT, Enums.Orientation.X, intensity, weapon, LevelUpdater.getInstance().getGigaGal());
        ammo.update(1);
        Vector2 offset = new Vector2();
        switch (intensity) {
            case NORMAL:
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

        if (ammo.getTexture() != null) {

            Helpers.drawTextureRegion(
                    batch,
                    viewport, ammo.getTexture(),
                    viewport.getCamera().position.x + viewport.getWorldWidth() / 2.5f,
                    yIcon,
                    offset.x,
                    offset.y,
                    Constants.AMMO_ICON_SCALE
            );
        }

        final String scoreString = LevelUpdater.getInstance().getScore() + "";
        final String timerString = Helpers.secondsToString(TimeUtils.nanosToMillis(level.getTime()));
        Helpers.drawBitmapFont(batch, viewport, font, scoreString, viewport.getCamera().position.x, viewport.getCamera().position.y - viewport.getWorldHeight() / 2.2f, Align.center);
        Helpers.drawBitmapFont(batch, viewport, font, timerString, viewport.getCamera().position.x, viewport.getCamera().position.y - viewport.getWorldHeight() / 2.8f, Align.center);
    }
}
