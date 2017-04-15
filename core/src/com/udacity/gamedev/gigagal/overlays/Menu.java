package com.udacity.gamedev.gigagal.overlays;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Helpers;

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
        float alignmentPosition = viewport.getCamera().position.x;
        if (alignment == Align.left) {
            alignmentPosition = viewport.getCamera().position.x - viewport.getWorldWidth() / 6;
        } else if (alignmentPosition == Align.right) {
            alignmentPosition = viewport.getCamera().position.x + viewport.getWorldWidth() / 6;
        }

        if (promptString != null) {
            Helpers.drawBitmapFont(batch, viewport, font, promptString, viewport.getCamera().position.x, viewport.getCamera().position.y, alignment);
        }

        if (!singleOption) {
            cursor.render(batch, viewport);
            cursor.update();
            if (cursor.getOrientation() == Enums.Orientation.X) {
                for (Object option : optionStrings) {
                    Helpers.drawBitmapFont(batch, viewport, font, (String) option, startingPosition, viewport.getCamera().position.y, alignment);
                    startingPosition += 100;
                }
            } else if (cursor.getOrientation() == Enums.Orientation.Y) {
                for (Object option : optionStrings) {
                    Helpers.drawBitmapFont(batch, viewport, font, (String) option, alignmentPosition, startingPosition + 10, alignment);
                    startingPosition -= 15;
                }
            }
        }
     //   cursor.resetPosition();
    }

    public void isSingleOption(boolean mode) { singleOption = mode; }
    public void setOptionStrings(List<String> optionStrings) { this.optionStrings = optionStrings.toArray();}
    public void setPromptString(String promptString) { this.promptString = promptString; }
    public void setAlignment(int alignment) { this.alignment = alignment; }
}
