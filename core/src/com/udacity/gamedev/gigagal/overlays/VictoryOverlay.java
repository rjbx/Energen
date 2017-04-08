package com.udacity.gamedev.gigagal.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.app.GameplayScreen;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Utils;

// immutable
public final class VictoryOverlay {

    // fields
    public final static String TAG = VictoryOverlay.class.getName();
    private final GameplayScreen gameplayScreen;
    private final SpriteBatch batch; // class-level instantiation
    private final ExtendViewport viewport; // class-level instantiation
    private final BitmapFont font; // class-level instantiation

    // default ctor
    public VictoryOverlay(GameplayScreen gameplayScreen) {
        this.gameplayScreen = gameplayScreen;
        this.batch = new SpriteBatch();
        this.viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        font = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE));
        font.getData().setScale(.4f);
    }

    public void render() {
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        final String levelTime = Utils.stopWatchToString(gameplayScreen.getLevel().getLevelTime());
        final String totalTime = Utils.stopWatchToString(gameplayScreen.getTotalTime());

        font.draw(batch, Constants.VICTORY_MESSAGE, viewport.getWorldWidth() / 2, viewport.getWorldHeight() * .9f, 0, Align.center, false);
        font.draw(batch, "GAME TOTAL\n" + "Time: " + Utils.stopWatchToString(gameplayScreen.getTotalTime()) + "\n" + "Score: " + gameplayScreen.getTotalScore(), viewport.getWorldWidth() / 2, viewport.getWorldHeight() * .7f, 0, Align.center, false);
        font.draw(batch, "LEVEL TOTAL\n" + "Time: " + levelTime + "\n" + "Score: " + gameplayScreen.getLevel().getLevelScore() + "\n", viewport.getWorldWidth() / 2, viewport.getWorldHeight() * .3f, 0, Align.center, false);

        batch.end();
    }

    public void dispose() { font.dispose(); batch.dispose(); }

    public final Viewport getViewport() { return viewport; }
}
