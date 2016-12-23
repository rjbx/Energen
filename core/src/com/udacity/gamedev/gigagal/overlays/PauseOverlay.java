package com.udacity.gamedev.gigagal.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.GameplayScreen;
import com.udacity.gamedev.gigagal.entities.GigaGal;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Utils;

// immutable
public final class PauseOverlay {

    // fields
    public final static String TAG = VictoryOverlay.class.getName();
    private final Viewport viewport;
    private final BitmapFont font;
    private GameplayScreen gameplayScreen;
    private String levelTime;
    private String totalTime;
    private GigaGal gigaGal;
    private CursorOverlay cursor;

    // default ctor
    public PauseOverlay(GameplayScreen gameplayScreen) {
        this.gameplayScreen = gameplayScreen;
        this.viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        font = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE));
        font.getData().setScale(.25f);
        cursor = new CursorOverlay();
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

        levelTime = Utils.stopWatchToString(gameplayScreen.getLevel().getLevelTime());
        totalTime = Utils.stopWatchToString(gameplayScreen.getTotalTime());

        String stats =
                Constants.HUD_AMMO_LABEL + gigaGal.getAmmo() + "\n" +
                        Constants.HUD_HEALTH_LABEL + gigaGal.getHealth() + "\n" +
                        "Turbo: " + gigaGal.getTurbo() + "\n" +
                        Constants.HUD_WEAPON_LABEL + gigaGal.getWeapon() +
                        gigaGal.getWeaponList().toString();
            font.draw(batch, stats, viewport.getWorldWidth() / 4, viewport.getWorldHeight() / 2);
            font.draw(batch, "QUIT", (viewport.getWorldWidth() / 4), viewport.getWorldHeight() / 4);

        batch.end();
    }

    public final Viewport getViewport() { return viewport; }
    public final CursorOverlay getCursor() { return cursor; }
}
