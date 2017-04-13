package com.udacity.gamedev.gigagal.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.util.Constants;

public class Message {

    // fields
    public final static String TAG = Message.class.getName();
    private final SpriteBatch batch; // class-level instantiation
    private final ExtendViewport viewport; // class-level instantiation
    private final BitmapFont font; // class-level instantiation
    private String message;

    public Message() {
        this.batch = new SpriteBatch();
        this.viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        font = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE));
        font.getData().setScale(0.25f);
        this.message = "";
    }

    public void render(SpriteBatch batch, BitmapFont font, ExtendViewport viewport, Vector2 position) {
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        font.draw(batch, message, position.x, position.y, 0, Align.center, false);
        batch.end();
    }

    public void dispose() { font.dispose(); batch.dispose(); }

    public Viewport getViewport() { return viewport; }

    // Setters
    public void setMessage(String messageString) { this.message = messageString; }
}
