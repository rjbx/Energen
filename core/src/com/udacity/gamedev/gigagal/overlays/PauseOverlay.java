package com.udacity.gamedev.gigagal.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.app.GameplayScreen;
import com.udacity.gamedev.gigagal.entities.GigaGal;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Utils;

// immutable
public final class PauseOverlay {

    // fields
    public final static String TAG = VictoryOverlay.class.getName();
    private final Viewport viewport;
    private final BitmapFont font;
    private final BitmapFont inactiveFont;
    private GameplayScreen gameplayScreen;
    private GigaGal gigaGal;
    private boolean canToggle;
    private CursorOverlay cursor;

    // default ctor
    public PauseOverlay(GameplayScreen gameplayScreen) {
        this.gameplayScreen = gameplayScreen;
        this.viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        font = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE));
        font.getData().setScale(0.4f);
        inactiveFont = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE));
        inactiveFont.getData().setScale(0.4f);
        inactiveFont.setColor(Color.LIGHT_GRAY);
        cursor = new CursorOverlay(73, 25);
    }

    public void init() {
        gigaGal = this.gameplayScreen.getLevel().getGigaGal();
        canToggle = gigaGal.getAerialState() == Enums.AerialState.GROUNDED;
    }

    public void render(SpriteBatch batch) {
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        if (gameplayScreen.getChaseCam().getFollowing()) {
            cursor.render(batch);
            cursor.update();
            String stats =
                    Constants.HUD_AMMO_LABEL + gigaGal.getAmmo() + "\n" +
                            Constants.HUD_HEALTH_LABEL + gigaGal.getHealth() + "\n" +
                            "Turbo: " + gigaGal.getTurbo();

            String weapons = gigaGal.getWeapon() + "";

            for (Enums.WeaponType weapon : gigaGal.getWeaponList()) {
                if (weapon != gigaGal.getWeapon()) {
                    weapons += "\n" + weapon.toString();
                }
            }

            font.draw(batch, stats, viewport.getScreenX() + 5, viewport.getWorldHeight() * .8f, 0, Align.left, false);
            font.draw(batch, "GAME TOTAL\n" + "Time: " + Utils.stopWatchToString(gameplayScreen.getTotalTime()) + "\n" + "Score: " + gameplayScreen.getTotalScore(), viewport.getWorldWidth() / 2, viewport.getWorldHeight() * .8f, 0, Align.center, false);
            font.draw(batch, "RESUME", viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2.5f + 18, 0, Align.center, false);
            font.draw(batch, "EXIT LEVEL", viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2.5f + 3, 0, Align.center, false);
            font.draw(batch, "OPTIONS", viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2.5f - 12, 0, Align.center, false);
            if (canToggle) {
                font.draw(batch, weapons, viewport.getWorldWidth() - Constants.HUD_MARGIN, viewport.getWorldHeight() * .8f, 0, weapons.length(), 10, Align.right, false);
            } else {
                inactiveFont.draw(batch, weapons, viewport.getWorldWidth() - Constants.HUD_MARGIN, viewport.getWorldHeight() * .8f, 0, weapons.length(), 10, Align.right, false);
            }
        } else {
            font.draw(batch, "DEBUG MODE\n" + "PRESS SHOOT BUTTON TO EXIT", viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2.5f + 15, 0, Align.center, false);
        }
        batch.end();
    }

    public final Viewport getViewport() { return viewport; }
    public final CursorOverlay getCursor() { return cursor; }
}
