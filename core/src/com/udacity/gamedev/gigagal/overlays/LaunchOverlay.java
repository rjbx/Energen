package com.udacity.gamedev.gigagal.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Utils;

public final class LaunchOverlay {

    // fields
    public final Viewport viewport;
    final BitmapFont font;

    // ctor
    public LaunchOverlay() {
        this.viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        font = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE));
        font.getData().setScale(0.5f);
    }

    public void init() {
    }

    public void render(SpriteBatch batch) {

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        font.draw(batch, Constants.LAUNCH_MESSAGE, viewport.getWorldWidth() / 2, Constants.HUD_MARGIN, 0, Align.center, false);
        Utils.drawTextureRegion(batch, Assets.getInstance().getOverlayAssets().logo, new Vector2(viewport.getWorldWidth() / 4.5f, viewport.getWorldHeight() / 4), new Vector2(0,0), .5f);
        batch.end();
    }

    public Viewport getViewport() { return viewport; }
}
