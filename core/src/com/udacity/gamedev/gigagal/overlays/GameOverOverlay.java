package com.udacity.gamedev.gigagal.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.entities.Zoomba;
import com.udacity.gamedev.gigagal.entities.Platform;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Utils;

// immutable
public final class GameOverOverlay {

    // fields
    public final Viewport viewport;
    final BitmapFont font;
 //   Array<Zoomba> enemies;
 //   long startTime;

    // ctor
    public GameOverOverlay() {
        this.viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);

        font = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE));
        font.getData().setScale(1);
    }

    public void init() {
     /*   startTime = TimeUtils.nanoTime();

        enemies = new Array<Zoomba>(Constants.ZOOMBA_COUNT);

        for (int i = 0; i < Constants.ZOOMBA_COUNT; i++) {

            Platform fakePlatform = new Platform(
                    MathUtils.random(viewport.getWorldWidth()),
                    MathUtils.random(-Constants.ZOOMBA_CENTER.y/2, viewport.getWorldHeight()
                    ), 0, 0);

            Zoomba zoomba = new Zoomba(fakePlatform);

            enemies.add(zoomba);


        }
    */
    }

    public void render(SpriteBatch batch) {

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

   /*     float timeElapsed = Utils.secondsSince(startTime);
        int enemiesToShow = (int) (Constants.ZOOMBA_COUNT * (timeElapsed / Constants.LEVEL_END_DURATION));

        for (int i = 0; i < enemiesToShow; i++){
            Zoomba zoomba = enemies.get(i);
            zoomba.update(0);
            zoomba.render(batch);
        }

    */
        font.draw(batch, Constants.GAME_OVER_MESSAGE, viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2.5f, 0, Align.center, false);

        batch.end();

    }
}
