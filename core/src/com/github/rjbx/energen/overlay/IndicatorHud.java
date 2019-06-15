package com.github.rjbx.energen.overlay;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.github.rjbx.energen.app.LevelAssets;
import com.github.rjbx.energen.entity.Projectile;
import com.github.rjbx.energen.entity.Avatar;
import com.github.rjbx.energen.util.AssetManager;
import com.github.rjbx.energen.util.Constants;
import com.github.rjbx.energen.util.Enums;
import com.github.rjbx.energen.util.Helpers;

public class IndicatorHud {

    // fields
    public final static String TAG = IndicatorHud.class.getName();
    private static final IndicatorHud INSTANCE = new IndicatorHud();
    private Avatar avatar;
    private AssetManager assetManager;

    // ctor
    private IndicatorHud() {}

    public static IndicatorHud getInstance() { return INSTANCE; }

    public void create() {
        avatar = Avatar.getInstance();
        assetManager = AssetManager.getInstance();
    }

    public void render(SpriteBatch batch, ExtendViewport viewport, BitmapFont font) {
        float yIcon = viewport.getCamera().position.y + viewport.getWorldHeight() / 2.25f;
        float xAction = viewport.getCamera().position.x + 5;
        if (avatar.getMoveStatus()) {
            Helpers.drawTextureRegion(
                    batch,
                    viewport,
                    assetManager.getHudAssets().move,
                    xAction,
                    yIcon,
                    Constants.ABILITY_ICON_CENTER.x,
                    Constants.ABILITY_ICON_CENTER.y,
                    Constants.ACTION_ICON_SCALE
            );
        } else if (avatar.getClingStatus() && avatar.getClimbStatus()) {
            Helpers.drawTextureRegion(
                    batch,
                    viewport,
                    assetManager.getHudAssets().climb,
                    xAction,
                    yIcon,
                    Constants.ABILITY_ICON_CENTER.x,
                    Constants.ABILITY_ICON_CENTER.y,
                    Constants.ACTION_ICON_SCALE
            );
        } else if (avatar.getRappelStatus())  {
            Helpers.drawTextureRegion(
                    batch,
                    viewport,
                    assetManager.getHudAssets().rappel,
                    xAction,
                    yIcon,
                    Constants.ABILITY_ICON_CENTER.x,
                    Constants.ABILITY_ICON_CENTER.y,
                    Constants.ACTION_ICON_SCALE
            );
        }  else if (!avatar.getJumpStatus() && avatar.getAction() != Enums.Action.HOVERING && avatar.getHoverStatus()) {
            Helpers.drawTextureRegion(
                    batch,
                    viewport,
                    assetManager.getHudAssets().hover,
                    xAction,
                    yIcon,
                    Constants.ABILITY_ICON_CENTER.x,
                    Constants.ABILITY_ICON_CENTER.y,
                    Constants.ACTION_ICON_SCALE
            );
        } else if (avatar.getDashStatus()) {
            Helpers.drawTextureRegion(
                    batch,
                    viewport,
                    assetManager.getHudAssets().dash,
                    xAction,
                    yIcon,
                    Constants.ABILITY_ICON_CENTER.x,
                    Constants.ABILITY_ICON_CENTER.y,
                    Constants.ACTION_ICON_SCALE
            );
        }

        final TextureRegion lifeIcon = assetManager.getHudAssets().life;
        float xLife = viewport.getCamera().position.x - viewport.getWorldWidth() / 2.15f;
        for (int i = 1; i <= avatar.getLives(); i++) {
            Helpers.drawTextureRegion(
                    batch,
                    viewport,
                    lifeIcon,
                    xLife,
                    yIcon,
                    Constants.LIFE_ICON_CENTER.x,
                    Constants.LIFE_ICON_CENTER.y,
                    Constants.LIFE_ICON_SCALE
            );
            xLife += 19;
        }

        Enums.Energy energy = avatar.getEnergy();
        Enums.ShotIntensity intensity = avatar.getShotIntensity();
        Projectile projectile = new Projectile(new Vector2(0,0), Enums.Direction.RIGHT, Enums.Orientation.X, intensity, energy, LevelAssets.getClonedAvatar());
        projectile.update(1);
        Vector2 offset = new Vector2();
        switch (intensity) {
            case NORMAL:
                offset.set(Constants.SHOT_CENTER);
                offset.scl(Constants.AMMO_ICON_SCALE);
                break;
            case BLAST:
                offset.set(Constants.BLAST_CENTER.x, Constants.BLAST_CENTER.y * 1.5f);
                offset.scl(Constants.AMMO_ICON_SCALE);
                break;
            default:
                offset.set(Constants.SHOT_CENTER);
                offset.scl(Constants.AMMO_ICON_SCALE);
        }

        if (projectile.getTexture() != null) {

            Helpers.drawTextureRegion(
                    batch,
                    viewport,
                    projectile.getTexture(),
                    viewport.getCamera().position.x + viewport.getWorldWidth() / 2.75f,
                    yIcon,
                    offset.x,
                    offset.y,
                    Constants.AMMO_ICON_SCALE
            );
        }

        final String scoreString = LevelAssets.getScore() + "";
        final String timerString = Helpers.millisToString(TimeUtils.nanosToMillis(LevelAssets.getTime()));
        Helpers.drawBitmapFont(batch, viewport, font, scoreString, viewport.getCamera().position.x, viewport.getCamera().position.y - viewport.getWorldHeight() / 2.2f, Align.center);
        Helpers.drawBitmapFont(batch, viewport, font, timerString, viewport.getCamera().position.x, viewport.getCamera().position.y - viewport.getWorldHeight() / 2.8f, Align.center);
    }
}
