package com.udacity.gamedev.gigagal.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.app.GameplayScreen;
import com.udacity.gamedev.gigagal.app.LevelSelectScreen;
import com.udacity.gamedev.gigagal.app.StartScreen;
import com.udacity.gamedev.gigagal.entities.GigaGal;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Utils;

import java.util.ArrayList;
import java.util.List;

// immutable
public final class OptionsOverlay {

    // fields
    public final static String TAG = OptionsOverlay.class.getName();
    private final SpriteBatch batch; // class-level instantiation
    private final ExtendViewport viewport; // class-level instantiation
    private final BitmapFont font; // class-level instantiation
    private final ScreenAdapter screenAdapter;
    private CursorOverlay cursor; // class-level instantiation
    private Object[] optionStrings;
    private GameplayScreen gameplayScreen;
    private GigaGal gigaGal;
    private String promptString;
    private boolean paused;
    private boolean singleOption;

    // default ctor
    public OptionsOverlay(ScreenAdapter screenAdapter) {
        this.screenAdapter = screenAdapter;
        this.viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        this.batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE));
        font.getData().setScale(0.4f);
        singleOption = false;
    }

    public void render(SpriteBatch batch, BitmapFont font, ExtendViewport viewport, CursorOverlay cursor) {

        float startingPosition = cursor.getStartingPosition();

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        if (promptString != null) {
            font.draw(batch, promptString, viewport.getWorldWidth() / 2, viewport.getWorldHeight() * .75f, 0, Align.center, false);
        }

        if (!singleOption) {
            cursor.render(batch, viewport);
            cursor.update();
        }

        if (cursor.getOrientation() == Enums.Orientation.X) {
            for (Object option : optionStrings) {
                font.draw(batch, (String) option, startingPosition, viewport.getWorldHeight() / 2.5f, 0, Align.center, false);
                startingPosition += 100;
            }
        } else if (cursor.getOrientation() == Enums.Orientation.Y) {
            for (Object option : optionStrings) {
                font.draw(batch, (String) option, viewport.getWorldWidth() / 2, startingPosition + 10, 0, Align.center, false);
                startingPosition -= 15;
            }
        }

        batch.end();
     //   cursor.resetPosition();
    }

    public void dispose() { font.dispose(); batch.dispose(); }
    public final Viewport getViewport() { return viewport; }
    public final CursorOverlay getCursor() { return cursor; }
    public void isSingleOption(boolean mode) { singleOption = mode; }
    public void setOptionStrings(List<String> optionStrings) { this.optionStrings = optionStrings.toArray();}
    public void setPromptString(String promptString) { this.promptString = promptString; }
}
