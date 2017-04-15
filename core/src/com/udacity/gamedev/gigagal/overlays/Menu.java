package com.udacity.gamedev.gigagal.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.screens.LevelScreen;
import com.udacity.gamedev.gigagal.entities.GigaGal;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import java.util.List;

// immutable
public final class Menu {

    // fields
    public final static String TAG = Menu.class.getName();
    public static final Menu INSTANCE = new Menu();
    private Object[] optionStrings;
    private String promptString;
    private int alignment;
    private boolean singleOption;

    // default ctor
    private Menu() {}

    public static Menu getInstance() { return INSTANCE; }

    public void create() {
        singleOption = false;
    }

    public void render(SpriteBatch batch, BitmapFont font, ExtendViewport viewport, Cursor cursor) {

        float startingPosition = cursor.getStartingPosition();
        float alignmentPosition = viewport.getWorldWidth() / 2;
        if (alignment == Align.left) {
            alignmentPosition = viewport.getWorldWidth() / 3;
        } else if (alignmentPosition == Align.right) {
            alignmentPosition = viewport.getWorldWidth() / 3 * 2;
        }

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        if (promptString != null) {
            font.draw(batch, promptString, viewport.getWorldWidth() / 2, viewport.getWorldHeight() * .75f, 0, alignment, false);
        }

        if (!singleOption) {
            cursor.render(batch, viewport);
            cursor.update();
        }

        if (cursor.getOrientation() == Enums.Orientation.X) {
            for (Object option : optionStrings) {
                font.draw(batch, (String) option, startingPosition, viewport.getWorldHeight() / 2.5f, 0, alignment, false);
                startingPosition += 100;
            }
        } else if (cursor.getOrientation() == Enums.Orientation.Y) {
            for (Object option : optionStrings) {
                font.draw(batch, (String) option, alignmentPosition, startingPosition + 10, 0, alignment, false);
                startingPosition -= 15;
            }
        }

        batch.end();
     //   cursor.resetPosition();
    }

    public void isSingleOption(boolean mode) { singleOption = mode; }
    public void setOptionStrings(List<String> optionStrings) { this.optionStrings = optionStrings.toArray();}
    public void setPromptString(String promptString) { this.promptString = promptString; }
    public void setAlignment(int alignment) { this.alignment = alignment; }
}
