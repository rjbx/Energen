package com.udacity.gamedev.gigagal.app;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.udacity.gamedev.gigagal.overlays.ControlsOverlay;
import com.udacity.gamedev.gigagal.overlays.CursorOverlay;
import com.udacity.gamedev.gigagal.overlays.OptionsOverlay;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

// immutable
public final class LevelSelectScreen extends ScreenAdapter {

    // fields
    public static final String TAG = LevelSelectScreen.class.getName();
    private com.udacity.gamedev.gigagal.app.GigaGalGame game;
    private SpriteBatch batch;
    private Preferences prefs;
    private int levelNumber;
    private Array<String> completedLevels;
    private ExtendViewport viewport;
    private BitmapFont font;
    private List<Enums.WeaponType> levelTypes;
    private float margin;
    private ListIterator<Enums.WeaponType> iterator;
    private String levelName;
    private CursorOverlay cursor;
    private OptionsOverlay optionsOverlay;
    private Array<Float> namePositions;
    private String selectedLevel;
    private int index;
    private GameplayScreen gameplayScreen;
    private com.udacity.gamedev.gigagal.app.InputControls inputControls;
    private ControlsOverlay controlsOverlay;
    private boolean optionsVisible;

    // default ctor
    public LevelSelectScreen(com.udacity.gamedev.gigagal.app.GigaGalGame game) {
        this.game = game;
        gameplayScreen = game.getGameplayScreen();
        prefs = game.getPreferences();
        cursor = new CursorOverlay(145, 40, Enums.Orientation.Y);
        this.viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        font = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE));
        font.getData().setScale(0.5f);
        levelTypes = new ArrayList<Enums.WeaponType>();
        levelTypes.addAll(Arrays.asList(Enums.WeaponType.values()));
        iterator = levelTypes.listIterator();
        levelName = iterator.next().name();
        index = 0;
        namePositions = new Array<Float>();
    }

    @Override
    public void show() {
        // : When you're done testing, use onMobile() turn off the controls when not on a mobile device
        // onMobile();
        levelNumber = 0;
        optionsVisible = false;
        batch = new SpriteBatch();
        completedLevels = new Array<String>();
        optionsOverlay = new OptionsOverlay(this);
        optionsOverlay.init();
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
        cursor.getViewport().update(width, height, true);
        controlsOverlay.getViewport().update(width, height, true);
        controlsOverlay.recalculateButtonPositions();
        optionsOverlay.getViewport().update(width, height, true);
        optionsOverlay.getCursor().getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        Assets.getInstance().dispose();
    }

    public void update() {}

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
            cursor.render(batch);
            cursor.update();

            while (iterator.hasNext()) {
                iterator.next();
            }

            float yPosition = viewport.getWorldHeight() / 2.5f;
            namePositions.add(yPosition);
            while (iterator.hasPrevious()) {
                levelName = iterator.previous().name();
                if (cursor.getPosition() >= namePositions.get(index) - 15 && cursor.getPosition() < namePositions.get(index)) {
                    selectedLevel = levelName;
                }
                font.draw(batch, levelName, viewport.getWorldWidth() / 2.5f, namePositions.get(index));
                yPosition += 15;
                namePositions.add(yPosition);
                index++;
            }
            font.draw(batch, "OPTIONS", viewport.getWorldWidth() / 2.5f, viewport.getWorldHeight() / 2.5f - 15);

            index = 0;
            margin = 0;

            if (inputControls.shootButtonJustPressed) {
                if (cursor.getPosition() == viewport.getWorldHeight() / 2.5f - 24) {
                    optionsVisible = true;
                } else {
                    gameplayScreen.setGame(game);
                    gameplayScreen.setLevelName(selectedLevel);
                    game.setScreen(gameplayScreen);
                }
            }
            batch.end();
        } else {
            optionsOverlay.render(batch);
            if (inputControls.shootButtonJustPressed) {
                if (optionsOverlay.getCursor().getPosition() > optionsOverlay.getViewport().getWorldHeight() / 2.5f + 8) {
                    optionsVisible = false;
                } else if (optionsOverlay.getCursor().getPosition() > optionsOverlay.getViewport().getWorldHeight() / 2.5f - 7) {
                    controlsOverlay.onMobile = Utils.toggleBoolean(controlsOverlay.onMobile);
                    prefs.putBoolean("Mobile", controlsOverlay.onMobile);
                } else if (optionsOverlay.getCursor().getPosition() > optionsOverlay.getViewport().getWorldHeight() / 2.5f - 22) {
                    game.create();
                }
            } else if (inputControls.pauseButtonJustPressed) {
                optionsVisible = false;
            }
        }
        inputControls.update();
        controlsOverlay.render(batch);
    }
}