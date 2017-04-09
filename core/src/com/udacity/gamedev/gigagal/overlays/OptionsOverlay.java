package com.udacity.gamedev.gigagal.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.app.GameplayScreen;
import com.udacity.gamedev.gigagal.app.LevelSelectScreen;
import com.udacity.gamedev.gigagal.app.StartScreen;
import com.udacity.gamedev.gigagal.entities.GigaGal;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Utils;

// immutable
public final class OptionsOverlay {

    // fields
    public final static String TAG = OptionsOverlay.class.getName();
    private final SpriteBatch batch; // class-level instantiation
    private final ExtendViewport viewport; // class-level instantiation
    private final BitmapFont font; // class-level instantiation
    private final ScreenAdapter screenAdapter;
    private CursorOverlay cursor; // class-level instantiation
    private GameplayScreen gameplayScreen;
    private GigaGal gigaGal;
    private boolean paused;
    private boolean debugMode;

    // default ctor
    public OptionsOverlay(ScreenAdapter screenAdapter) {
        this.screenAdapter = screenAdapter;
        this.viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        this.batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE));
        font.getData().setScale(0.4f);
        debugMode = false;
        if (screenAdapter instanceof GameplayScreen) {
            cursor = new CursorOverlay(73, 28, Enums.Orientation.Y);
            gameplayScreen = (GameplayScreen) screenAdapter;
            gigaGal = gameplayScreen.getLevel().getGigaGal();
        } else {
            cursor = new CursorOverlay(73, 43, Enums.Orientation.Y);
        }
    }

    public void render(SpriteBatch batch, BitmapFont font, ExtendViewport viewport, CursorOverlay cursor) {

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        if (!debugMode) {
            cursor.render(batch, viewport);
            cursor.update();
        }

        if (screenAdapter instanceof GameplayScreen) {
            if (gameplayScreen.getChaseCam().getFollowing()) {
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
                font.draw(batch, weapons, viewport.getWorldWidth() - Constants.HUD_MARGIN, viewport.getWorldHeight() * .8f, 0, weapons.length(), 10, Align.right, false);
                font.draw(batch, "GAME TOTAL\n" + "Time: " + Utils.stopWatchToString(gameplayScreen.getTotalTime()) + "\n" + "Score: " + gameplayScreen.getTotalScore(), viewport.getWorldWidth() / 2, viewport.getWorldHeight() * .8f, 0, Align.center, false);
                font.draw(batch, "BACK", viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2.5f + 18, 0, Align.center, false);
                font.draw(batch, "DEBUG CAM", viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2.5f + 3, 0, Align.center, false);
                font.draw(batch, "TOUCH PAD", viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2.5f - 12, 0, Align.center, false);
                font.draw(batch, "QUIT GAME", viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2.5f - 27, 0, Align.center, false);
            } else {
                font.draw(batch, "DEBUG MODE\n" + "PRESS SHOOT BUTTON TO EXIT", viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2.5f + 15, 0, Align.center, false);
            }
        } else if (screenAdapter instanceof LevelSelectScreen) {
            font.draw(batch, "BACK", viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2.5f + 18, 0, Align.center, false);
            font.draw(batch, "TOUCH PAD", viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2.5f + 3, 0, Align.center, false);
            font.draw(batch, "QUIT GAME", viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2.5f - 12, 0, Align.center, false);
        } else if (screenAdapter instanceof StartScreen) {
            font.draw(batch, "NORMAL", viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2.5f + 18, 0, Align.center, false);
            font.draw(batch, "HARD", viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2.5f + 3, 0, Align.center, false);
            font.draw(batch, "VERY HARD", viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2.5f - 12, 0, Align.center, false);
        }
        batch.end();
    }

    public void dispose() { font.dispose(); batch.dispose(); }
    public final Viewport getViewport() { return viewport; }
    public final CursorOverlay getCursor() { return cursor; }
    public void setDebugMode(boolean mode) { debugMode = mode; }
}
