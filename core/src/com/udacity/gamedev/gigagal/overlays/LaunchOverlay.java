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
    public final static String TAG = LaunchOverlay.class.getName();
    private final SpriteBatch batch; // class-level instantiation
    private final ExtendViewport viewport; // class-level instantiation
    private final Vector2 logoOffset; // class-level instantiation
    private final BitmapFont font; // class-level instantiation

    // ctor
    public LaunchOverlay() {
        this.batch = new SpriteBatch();
        this.viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        logoOffset = new Vector2(Constants.LOGO_CENTER.x * .375f, Constants.LOGO_CENTER.y * .375f);
        font = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE));
        font.getData().setScale(0.5f);
    }

    public void render() {

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        final Vector2 logoPosition = new Vector2(viewport.getWorldWidth() / 2, viewport.getWorldHeight() * .625f);
        font.draw(batch, Constants.LAUNCH_MESSAGE, viewport.getWorldWidth() / 2, Constants.HUD_MARGIN, 0, Align.center, false);
        Utils.drawTextureRegion(batch, Assets.getInstance().getOverlayAssets().logo, logoPosition, logoOffset, .375f);
        batch.end();
    }

    public void dispose() { font.dispose(); batch.dispose(); }

    public Viewport getViewport() { return viewport; }
}
