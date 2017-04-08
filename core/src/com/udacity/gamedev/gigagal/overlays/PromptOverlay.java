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

public class PromptOverlay {

    // fields
    public final static String TAG = PromptOverlay.class.getName();
    private final SpriteBatch batch; // class-level instantiation
    private final ExtendViewport viewport; // class-level instantiation
    private final CursorOverlay cursor; // class-level instantiation
    private final BitmapFont font; // class-level instantiation
    private String promptString;
    private Array<String> choicesStrings;

    public PromptOverlay(String promptString, Array<String> choicesStrings) {
        this.batch = new SpriteBatch();
        this.viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        cursor = new CursorOverlay(50, 150, Enums.Orientation.X);
        font = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE));
        font.getData().setScale(0.4f);
        this.promptString = promptString;
        this.choicesStrings = choicesStrings;
    }

    public void render() {
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        cursor.render(batch);
        cursor.update();

        font.draw(batch, promptString, viewport.getWorldWidth() / 2, viewport.getWorldHeight() * .75f, 0, Align.center, false);

        switch (choicesStrings.size) {
            case 1:
                font.draw(batch, choicesStrings.get(0), viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 3, 0, Align.center, false);
                break;
            case 2:
                font.draw(batch, choicesStrings.get(0), viewport.getWorldWidth() / 4, viewport.getWorldHeight() / 3, 0, Align.center, false);
                font.draw(batch, choicesStrings.get(1), viewport.getWorldWidth() * .75f, viewport.getWorldHeight() / 3, 0, Align.center, false);
                break;
        }

        batch.end();
    }

    public void dispose() { choicesStrings.clear(); font.dispose(); batch.dispose(); }

    // Getters
    public Viewport getViewport() { return viewport; }
    public final CursorOverlay getCursor() { return cursor; }

    // Setters
    public void setPrompt(String promptString) { this.promptString = promptString; }
    public void setChoices(Array<String> choicesStrings) { this.choicesStrings = choicesStrings; }
}
