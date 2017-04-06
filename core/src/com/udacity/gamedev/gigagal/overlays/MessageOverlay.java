package com.udacity.gamedev.gigagal.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;

public class MessageOverlay {

    private final Viewport viewport;
    private final BitmapFont font;
    private String messageString;

    public MessageOverlay(String messageString) {
        this.viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        font = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE));
        font.getData().setScale(0.25f);
        this.messageString = messageString;
    }

    public void render(SpriteBatch batch) {
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        font.draw(batch, messageString, viewport.getWorldWidth() / 2, Constants.HUD_MARGIN - 5, 0, Align.center, false);

        batch.end();
    }

    public Viewport getViewport() { return viewport; }

    // Setters
    public void setMessage(String messageString) { this.messageString = messageString; }
}
