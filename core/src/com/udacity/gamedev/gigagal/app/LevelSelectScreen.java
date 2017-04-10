package com.udacity.gamedev.gigagal.app;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.udacity.gamedev.gigagal.overlays.ControlsOverlay;
import com.udacity.gamedev.gigagal.overlays.CursorOverlay;
import com.udacity.gamedev.gigagal.overlays.MessageOverlay;
import com.udacity.gamedev.gigagal.overlays.OptionsOverlay;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Utils;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;

// immutable
public final class LevelSelectScreen extends ScreenAdapter {

    // fields
    public static final String TAG = LevelSelectScreen.class.getName();
    private static InputControls inputControls;
    private static ControlsOverlay controlsOverlay;
    private com.udacity.gamedev.gigagal.app.GigaGalGame game;
    private Preferences prefs;
    private ExtendViewport viewport;
    private SpriteBatch batch;
    private BitmapFont font;
    private CursorOverlay cursorOverlay;
    private OptionsOverlay optionsOverlay;
    private OptionsOverlay selectionOverlay;
    private MessageOverlay messageOverlay;
    private Array<Float> namePositions;
    private ArrayList<String> selectionStrings;
    private Array<Enums.LevelName> completedLevels;
    private ListIterator<String> iterator;
    private Enums.LevelName levelName;
    private Enums.LevelName selectedLevel;
    private GameplayScreen gameplayScreen;
    private boolean optionsVisible;
    private boolean messageVisible;

    // default ctor
    public LevelSelectScreen(com.udacity.gamedev.gigagal.app.GigaGalGame game) {
        this.game = game;
        prefs = game.getPreferences();
        this.viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        font = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE));
        font.getData().setScale(0.5f);
        namePositions = new Array<Float>();
    }

    @Override
    public void show() {
        // : When you're done testing, use onMobile() turn off the controls when not on a mobile device
        // onMobile();
        optionsVisible = false;
        messageVisible = false;
        batch = new SpriteBatch();
        completedLevels = new Array<Enums.LevelName>();
        cursorOverlay = new CursorOverlay(145, 25, Enums.Orientation.Y);
        optionsOverlay = new OptionsOverlay(this);
        selectionOverlay = new OptionsOverlay(this);
        selectionStrings = new ArrayList();
        for (Enums.LevelName level : Enums.LevelName.values()) {
            selectionStrings.add(level.name());
        }
        selectionStrings.add("OPTIONS");
        iterator = selectionStrings.listIterator();
        cursorOverlay.setIterator(selectionStrings);
        iterator.next();
        selectionOverlay.setOptionStrings(selectionStrings);
        selectionOverlay.setAlignment(Align.left);
        messageOverlay = new MessageOverlay("");
        inputControls = com.udacity.gamedev.gigagal.app.InputControls.getInstance();
        controlsOverlay = ControlsOverlay.getInstance();
        Gdx.input.setInputProcessor(inputControls);
    }

    private boolean onMobile() {
        return Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
//        cursorOverlay.getViewport().update(width, height, true);
//        controlsOverlay.getViewport().update(width, height, true);
//        controlsOverlay.recalculateButtonPositions();
//        optionsOverlay.getViewport().update(width, height, true);
//        optionsOverlay.getCursor().getViewport().update(width, height, true);
//        messageOverlay.getViewport().update(width, height, true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(
                Constants.BACKGROUND_COLOR.r,
                Constants.BACKGROUND_COLOR.g,
                Constants.BACKGROUND_COLOR.b,
                Constants.BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!optionsVisible) {
            viewport.apply();

            float yPosition = viewport.getWorldHeight() / 2.5f;
            namePositions.add(yPosition);
            yPosition += 15;
            namePositions.add(yPosition);
            selectionOverlay.render(batch, font, viewport, cursorOverlay);

            if (inputControls.shootButtonJustPressed) {
                if (cursorOverlay.getPosition() <= 145 && cursorOverlay.getPosition() >= 40) {
                    selectedLevel = Enums.LevelName.valueOf(cursorOverlay.getIterator().previous());
                    gameplayScreen = new GameplayScreen(game, selectedLevel);
                    try {
                        gameplayScreen.readLevelFile();
                        game.setScreen(gameplayScreen);
                        this.dispose();
                        return;
                    } catch (IOException ex) {
                        Gdx.app.log(TAG, Constants.LEVEL_READ_MESSAGE);
                        messageOverlay.setMessage(Constants.LEVEL_READ_MESSAGE);
                        messageVisible = true;
                    } catch (ParseException ex) {
                        Gdx.app.log(TAG, Constants.LEVEL_READ_MESSAGE);
                        messageOverlay.setMessage(Constants.LEVEL_READ_MESSAGE);
                        messageVisible = true;
                    } catch (GdxRuntimeException ex) {
                        Gdx.app.log(TAG, Constants.LEVEL_READ_MESSAGE);
                        messageOverlay.setMessage(Constants.LEVEL_READ_MESSAGE);
                        messageVisible = true;
                    }
                } else {
                    optionsVisible = true;
                    String[] optionStrings = {"BACK", "TOUCH PAD", "QUIT GAME"};
                    optionsOverlay.setOptionStrings(Arrays.asList(optionStrings));
                    cursorOverlay.setIterator(null);
                    cursorOverlay.setRange(106, 76);
                    cursorOverlay.resetPosition();
                    cursorOverlay.update();
                }
            }
        } else {
            optionsOverlay.render(batch, font, viewport, cursorOverlay);
            if (inputControls.shootButtonJustPressed) {
                if (cursorOverlay.getPosition() == 106) {
                    cursorOverlay.setRange(145, 25);
                    cursorOverlay.resetPosition();
                    cursorOverlay.update();
                    optionsVisible = false;
                } else if (cursorOverlay.getPosition() == 91) {
                    controlsOverlay.onMobile = Utils.toggleBoolean(controlsOverlay.onMobile);
                    prefs.putBoolean("Mobile", controlsOverlay.onMobile);
                } else if (cursorOverlay.getPosition() == 76) {
                    game.dispose();
                    game.create();
                }
            } else if (inputControls.pauseButtonJustPressed) {
                optionsVisible = false;
            }
        }
        if (messageVisible) {
            messageOverlay.render(batch, font, viewport);
        }
        inputControls.update();
        controlsOverlay.render(batch, viewport);
    }

    @Override
    public void dispose() {
        completedLevels.clear();
        inputControls.clear();
        optionsOverlay.dispose();
        messageOverlay.dispose();
        font.dispose();
        batch.dispose();
        iterator = null;
        completedLevels = null;
        inputControls = null;
        optionsOverlay = null;
        messageOverlay = null;
        font = null;
        batch = null;
        this.hide();
        super.dispose();
        System.gc();
    }
}