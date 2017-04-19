package com.udacity.gamedev.gigagal.app;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.udacity.gamedev.gigagal.overlay.TouchInterface;
import com.udacity.gamedev.gigagal.util.InputControls;
import com.udacity.gamedev.gigagal.overlay.Menu;
import com.udacity.gamedev.gigagal.overlay.Cursor;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Helpers;

import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// immutable package-private
final class OverworldScreen extends ScreenAdapter {

    // fields
    public static final String TAG = OverworldScreen.class.getName();
    private static final OverworldScreen INSTANCE = new OverworldScreen();
    private ExtendViewport viewport;
    private SpriteBatch batch;
    private BitmapFont font;
    private boolean viewingOptions;
    private boolean messageVisible;

    // cannot be subclassed
    private OverworldScreen() {}

    // static factory method
    protected static OverworldScreen getInstance() { return INSTANCE; }

    protected void create() {
        this.viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE));
        font.getData().setScale(0.5f);
        setMainMenu();
    }

    @Override
    public void show() {
        // : When you're done testing, use onMobile() turn off the controls when not on a mobile device
        // onMobile();
        viewingOptions = false;
        messageVisible = false;
        InputControls.getInstance();
        TouchInterface.getInstance();
        Gdx.input.setInputProcessor(InputControls.getInstance());
    }

    public static void setMainMenu() {
        List<String> selectionStrings = new ArrayList();
        for (Enums.LevelName level : Enums.LevelName.values()) {
            selectionStrings.add(level.name());
        }
        selectionStrings.add("OPTIONS");
        Cursor.getInstance().setIterator(selectionStrings);
        Cursor.getInstance().setRange(145, 25);
        Cursor.getInstance().setOrientation(Enums.Orientation.Y);
        Cursor.getInstance().resetPosition();
        Menu.getInstance().clearStrings();
        Menu.getInstance().setOptionStrings(selectionStrings);
        Menu.getInstance().TextAlignment(Align.left);
    }

    private static void setOptionsMenu() {
        Cursor.getInstance().setIterator(null);
        Cursor.getInstance().setRange(106, 76);
        Cursor.getInstance().setOrientation(Enums.Orientation.Y);
        Cursor.getInstance().resetPosition();
        String[] optionStrings = {"BACK", "TOUCH PAD", "QUIT GAME"};
        Menu.getInstance().setOptionStrings(Arrays.asList(optionStrings));
        Menu.getInstance().TextAlignment(Align.center);
    }

    private boolean onMobile() {
        return Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
//        cursor.getViewport().update(width, height, true);
//        touchInterface.getViewport().update(width, height, true);
        TouchInterface.getInstance().recalculateButtonPositions();
//        optionsOverlay.getViewport().update(width, height, true);
//        optionsOverlay.getCursor().getViewport().update(width, height, true);
//        errorMessage.getViewport().update(width, height, true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(
                Constants.BACKGROUND_COLOR.r,
                Constants.BACKGROUND_COLOR.g,
                Constants.BACKGROUND_COLOR.b,
                Constants.BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!viewingOptions) {

            Menu.getInstance().render(batch, font, viewport, Cursor.getInstance());

            if (InputControls.getInstance().shootButtonJustPressed) {
                if (Cursor.getInstance().getPosition() <= 145 && Cursor.getInstance().getPosition() >= 40) {
                    messageVisible = false;
                    try {
                        LevelLoader.load(Enums.LevelName.valueOf(Cursor.getInstance().getIterator().previous()));
                        ScreenManager.getInstance().setScreen(LevelScreen.getInstance());
                        this.dispose();
                        return;
                    } catch (IOException ex) {
                        Gdx.app.log(TAG, Constants.LEVEL_READ_MESSAGE);
                        Gdx.app.log(TAG, Constants.LEVEL_READ_MESSAGE, ex);
                        Cursor.getInstance().getIterator().next();
                        messageVisible = true;
                    } catch (ParseException ex) {
                        Gdx.app.log(TAG, Constants.LEVEL_READ_MESSAGE);
                        Gdx.app.log(TAG, Constants.LEVEL_READ_MESSAGE, ex);
                        Cursor.getInstance().getIterator().next();
                        messageVisible = true;
                    } catch (GdxRuntimeException ex) {
                        Gdx.app.log(TAG, Constants.LEVEL_READ_MESSAGE);
                        Gdx.app.log(TAG, Constants.LEVEL_READ_MESSAGE, ex);
                        Cursor.getInstance().getIterator().next();
                        messageVisible = true;
                    }
                } else {
                    viewingOptions = true;
                    setOptionsMenu();
                }
            }
        } else {
            Menu.getInstance().render(batch, font, viewport, Cursor.getInstance());
            if (InputControls.getInstance().shootButtonJustPressed) {
                if (Cursor.getInstance().getPosition() == 106) {
                    setMainMenu();
                    viewingOptions = false;
                } else if (Cursor.getInstance().getPosition() == 91) {
                    SaveData.toggleTouchscreen(!SaveData.hasTouchscreen());
                } else if (Cursor.getInstance().getPosition() == 76) {
                    ScreenManager.getInstance().dispose();
                    ScreenManager.getInstance().create();
                }
            } else if (InputControls.getInstance().pauseButtonJustPressed) {
                viewingOptions = false;
            }
        }
        if (messageVisible) {
            font.getData().setScale(0.25f);
            Helpers.drawBitmapFont(batch, viewport, font, Constants.LEVEL_READ_MESSAGE, viewport.getWorldWidth() / 2, Constants.HUD_MARGIN - 5, Align.center);
            font.getData().setScale(.5f);
        }
        InputControls.getInstance().update();
        TouchInterface.getInstance().render(batch);
    }

    @Override
    public void dispose() {
//        completedLevels.clearStrings();
//        inputControls.clearStrings();
//        errorMessage.dispose();
//        font.dispose();
//        batch.dispose();
//        completedLevels = null;
//        inputControls = null;
//        errorMessage = null;
//        font = null;
//        batch = null;
//        this.hide();
//        super.dispose();
//        System.gc();
    }
}