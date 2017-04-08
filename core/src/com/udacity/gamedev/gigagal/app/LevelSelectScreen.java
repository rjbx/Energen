package com.udacity.gamedev.gigagal.app;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import java.util.List;
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
    private MessageOverlay messageOverlay;
    private Array<Float> namePositions;
    private Array<Enums.LevelName> completedLevels;
    private List<Enums.LevelName> levelTypes;
    private ListIterator<Enums.LevelName> iterator;
    private Enums.LevelName levelName;
    private Enums.LevelName selectedLevel;
    private int index;
    private GameplayScreen gameplayScreen;
    private boolean optionsVisible;
    private boolean messageVisible;

    // default ctor
    public LevelSelectScreen(com.udacity.gamedev.gigagal.app.GigaGalGame game) {
        this.game = game;
        prefs = game.getPreferences();
        cursorOverlay = new CursorOverlay(145, 40, Enums.Orientation.Y);
        this.viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        font = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE));
        font.getData().setScale(0.5f);
        levelTypes = new ArrayList<Enums.LevelName>(Arrays.asList(Enums.LevelName.values()));
        iterator = levelTypes.listIterator();
        levelName = iterator.next();
        index = 0;
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
        optionsOverlay = new OptionsOverlay(this);
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
        cursorOverlay.getViewport().update(width, height, true);
        controlsOverlay.getViewport().update(width, height, true);
        controlsOverlay.recalculateButtonPositions();
        optionsOverlay.getViewport().update(width, height, true);
        optionsOverlay.getCursor().getViewport().update(width, height, true);
        messageOverlay.getViewport().update(width, height, true);
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
            batch.begin();
            cursorOverlay.render(batch);
            cursorOverlay.update();

            while (iterator.hasNext()) {
                iterator.next();
            }

            float yPosition = viewport.getWorldHeight() / 2.5f;
            namePositions.add(yPosition);
            while (iterator.hasPrevious()) {
                levelName = iterator.previous();
                if (cursorOverlay.getPosition() >= namePositions.get(index) - 15 && cursorOverlay.getPosition() < namePositions.get(index)) {
                    selectedLevel = levelName;
                }
                font.draw(batch, levelName.toString(), viewport.getWorldWidth() / 2.5f, namePositions.get(index));
                yPosition += 15;
                namePositions.add(yPosition);
                index++;
            }
            font.draw(batch, "OPTIONS", viewport.getWorldWidth() / 2.5f, viewport.getWorldHeight() / 2.5f - 15);

            index = 0;

            batch.end();

            if (inputControls.shootButtonJustPressed) {
                if (cursorOverlay.getPosition() == viewport.getWorldHeight() / 2.5f - 24) {
                    optionsVisible = true;
                } else {
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
                }
            }
        } else {
            optionsOverlay.render();
            if (inputControls.shootButtonJustPressed) {
                if (optionsOverlay.getCursor().getPosition() > optionsOverlay.getViewport().getWorldHeight() / 2.5f + 8) {
                    optionsVisible = false;
                } else if (optionsOverlay.getCursor().getPosition() > optionsOverlay.getViewport().getWorldHeight() / 2.5f - 7) {
                    controlsOverlay.onMobile = Utils.toggleBoolean(controlsOverlay.onMobile);
                    prefs.putBoolean("Mobile", controlsOverlay.onMobile);
                } else if (optionsOverlay.getCursor().getPosition() > optionsOverlay.getViewport().getWorldHeight() / 2.5f - 22) {
                    game.dispose();
                    game.create();
                }
            } else if (inputControls.pauseButtonJustPressed) {
                optionsVisible = false;
            }
        }
        if (messageVisible) {
            messageOverlay.render();
        }
        inputControls.update();
        controlsOverlay.render();
    }

    @Override
    public void dispose() {
        iterator.remove();
        completedLevels.clear();
        levelTypes.clear();
        inputControls.clear();
        optionsOverlay.dispose();
        messageOverlay.dispose();
        font.dispose();
        batch.dispose();
        iterator = null;
        completedLevels = null;
        levelTypes = null;
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