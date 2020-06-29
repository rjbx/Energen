package com.github.rjbx.energen.overlay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.github.rjbx.energen.util.AssetManager;
import com.github.rjbx.energen.util.Enums;
import com.github.rjbx.energen.util.Helpers;

import java.util.Arrays;
import java.util.List;

// immutable
public final class Menu {

    // fields
    public final static String TAG = Menu.class.getName();
    private static final Menu INSTANCE = new Menu();
    private Object[] optionStrings;
    private List<String> optionStringsList;
    private String leftPrompt;
    private String centerPrompt;
    private String rightPrompt;
    private int textAlignment;
    private boolean singleOption;

    // default ctor
    private Menu() {}

    public static Menu getInstance() { return INSTANCE; }

    public void create() {
        singleOption = false;
    }

    public void render(SpriteBatch batch, BitmapFont font, ExtendViewport viewport, Cursor cursor, Object... inactive) {

        float startingPosition = cursor.getStartingPosition();
        float alignmentPosition = viewport.getCamera().position.x;
        if (textAlignment == Align.left) {
            alignmentPosition = viewport.getCamera().position.x - viewport.getWorldWidth() / 6;
        } else if (alignmentPosition == Align.right) {
            alignmentPosition = viewport.getCamera().position.x + viewport.getWorldWidth() / 6;
        }

        Helpers.drawBitmapFont(batch, viewport, font, leftPrompt,  viewport.getCamera().position.x - viewport.getWorldWidth() / 2.25f, viewport.getCamera().position.y + viewport.getWorldHeight() / 3, Align.left);
        Helpers.drawBitmapFont(batch, viewport, font, centerPrompt,  viewport.getCamera().position.x, viewport.getCamera().position.y + viewport.getWorldHeight() / 3, Align.center);
        Helpers.drawBitmapFont(batch, viewport, font, rightPrompt,  viewport.getCamera().position.x + viewport.getWorldWidth() / 2.25f, viewport.getCamera().position.y + viewport.getWorldHeight() / 3, Align.right);

        if (!singleOption) {
            cursor.render(batch, viewport);
            cursor.update();
            if (cursor.getOrientation() == Enums.Orientation.X) {
                for (Object option : optionStrings) {
                    if (inactive != null && inactive.length > 0) {
                        for (Object inactiveOption : inactive)
                            if (inactiveOption.equals(option)) font.setColor(Color.GRAY);
                            else font.setColor(Color.WHITE);
                    }
                    Helpers.drawBitmapFont(batch, viewport, font, (String) option, startingPosition, viewport.getCamera().position.y + 10, textAlignment);
                    startingPosition += 100;
                }
            } else if (cursor.getOrientation() == Enums.Orientation.Y) {
                for (Object option : optionStrings) {
                    if (inactive != null && inactive.length > 0) {
                        for (Object inactiveOption : inactive)
                            if (inactiveOption.equals(option)) {
                                font.setColor(Color.GRAY);
                                break;
                            } else font.setColor(Color.WHITE);
                    }
                    Helpers.drawBitmapFont(batch, viewport, font, (String) option, alignmentPosition, startingPosition + 10, textAlignment);
                    startingPosition -= 15;
                }
            }
        } else {
            Helpers.drawBitmapFont(batch, viewport, font, (String) optionStrings[0], alignmentPosition, startingPosition + 10, textAlignment);
        }
     //   cursor.resetPosition();
    }

    public void isSingleOption(boolean mode) { singleOption = mode; }
    public void setOptionStrings(List<String> optionStringsList) {
        this.optionStringsList = optionStringsList;
        this.optionStrings = optionStringsList.toArray();
        Cursor.getInstance().setIterator(this.optionStringsList.listIterator());
    }
    public void setOptionString(int index, String optionString) {
        if (index >= 0 && index < optionStringsList.size()) {
            this.optionStringsList.set(index, optionString);
            this.optionStrings = optionStringsList.toArray();
            Cursor.getInstance().setIterator(this.optionStringsList.listIterator());
        } else {
            throw new IndexOutOfBoundsException("Option string cannot be changed because selected index is not within bounds of option string list");
        }
    }
    public void TextAlignment(int alignment) { this.textAlignment = alignment; }
    public void setPromptString(int screenAlignment, String promptString) {
        switch (screenAlignment) {
            case Align.left:
                this.leftPrompt = promptString;
                break;
            case Align.center:
                this.centerPrompt = promptString;
                break;
            case Align.right:
                this.rightPrompt = promptString;
        }
    }
    public void clearStrings() {
        leftPrompt = "";
        centerPrompt = "";
        rightPrompt = "";
        optionStrings = null;
    }
}
