package com.udacity.gamedev.gigagal.overlays;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.GameplayScreen;
import com.udacity.gamedev.gigagal.entities.Explosion;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Utils;

// immutable
public final class VictoryOverlay {

    // fields
    public final static String TAG = VictoryOverlay.class.getName();
    private final Viewport viewport;
    private final BitmapFont font;
    private Array<Explosion> explosions;
    private GameplayScreen gameplayScreen;
    private String levelTime;
    private String totalTime;

    // default ctor
    public VictoryOverlay(GameplayScreen gameplayScreen) {
        this.viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        font = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE));
        font.getData().setScale(.25f);
        this.gameplayScreen = gameplayScreen;
    }

    public void init() {
        explosions = new Array<Explosion>(Constants.EXPLOSION_COUNT);
        for (int i = 0; i < Constants.EXPLOSION_COUNT; i++) {
            Explosion explosion = new Explosion(new Vector2(
                    MathUtils.random(viewport.getWorldWidth()),
                    MathUtils.random(viewport.getWorldHeight())
            ));
            explosion.setOffset(MathUtils.random(Constants.LEVEL_END_DURATION));

            explosions.add(explosion);
        }
    }

    public void render(SpriteBatch batch) {

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        for (Explosion explosion : explosions){
            explosion.render(batch);
        }

        levelTime = Utils.stopWatchToString(gameplayScreen.getLevel().getLevelTime());
        totalTime = Utils.stopWatchToString(gameplayScreen.getTotalTime());

        font.draw(batch, Constants.VICTORY_MESSAGE, viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2.5f, 0, Align.center, false);
        font.draw(batch,
                levelTime + "\n"
                + gameplayScreen.getTotalScore() + "\n"
                + totalTime + "\n"
                + gameplayScreen.getLevel().getLevelScore() + "\n",
                viewport.getWorldWidth() / 3, viewport.getWorldHeight() / 3, 0, Align.center, false
                );

        batch.end();
    }

    public final Viewport getViewport() { return viewport; }
}
