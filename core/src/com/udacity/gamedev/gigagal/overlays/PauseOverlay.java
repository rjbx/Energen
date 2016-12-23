package com.udacity.gamedev.gigagal.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.GameplayScreen;
import com.udacity.gamedev.gigagal.entities.GigaGal;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;

import java.util.ListIterator;

// immutable
public final class PauseOverlay {

    // fields
    public final static String TAG = VictoryOverlay.class.getName();
    private final Viewport viewport;
    private final BitmapFont font;
    private GameplayScreen gameplayScreen;
    private GigaGal gigaGal;
    private CursorOverlay cursor;
    private int index;

    // default ctor
    public PauseOverlay(GameplayScreen gameplayScreen) {
        this.gameplayScreen = gameplayScreen;
        this.viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        font = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE));
        font.getData().setScale(0.4f);
        cursor = new CursorOverlay(73, 25);
    }

    public void init() {
        gigaGal = this.gameplayScreen.getLevel().getGigaGal();
    }

    public void render(SpriteBatch batch) {

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
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

        font.draw(batch, stats, Constants.HUD_MARGIN, viewport.getWorldHeight() * .8f, 0, Align.left, false);
        font.draw(batch, weapons, viewport.getWorldWidth() - Constants.HUD_MARGIN, viewport.getWorldHeight() * .8f, 0, weapons.length(), 10, Align.right, false);
        font.draw(batch, "RESUME", viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2.5f + 15, 0, Align.center, false);
        font.draw(batch, "EXIT LEVEL", viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2.5f, 0, Align.center, false);
        font.draw(batch, "DEBUG CAM", viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2.5f - 15, 0, Align.center, false);
        font.draw(batch, "QUIT GAME", viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2.5f - 30, 0, Align.center, false);

        batch.end();
    }

    public final Viewport getViewport() { return viewport; }
    public final CursorOverlay getCursor() { return cursor; }
}
