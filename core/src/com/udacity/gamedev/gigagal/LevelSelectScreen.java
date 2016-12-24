package com.udacity.gamedev.gigagal;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.udacity.gamedev.gigagal.overlays.CursorOverlay;
import com.udacity.gamedev.gigagal.overlays.InputControls;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

// immutable
public final class LevelSelectScreen extends ScreenAdapter {

    // fields
    public static final String TAG = LevelSelectScreen.class.getName();
    private GigaGalGame game;
    private SpriteBatch batch;
    private int levelNumber;
    private Level level;
    private Array<String> completedLevels;
    private ExtendViewport viewport;
    private BitmapFont font;
    private List<String> levelNames;
    private float margin;
    private ListIterator<String> iterator;
    private String levelName;
    private CursorOverlay cursor;
    private Array<Float> namePositions;
    private String selectedLevel;
    private int index;
    private GameplayScreen gameplayScreen;
    private InputControls inputControls;

    // default ctor
    public LevelSelectScreen(GigaGalGame game) {
        this.viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        font = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE));
        font.getData().setScale(0.5f);
        levelNames = new ArrayList<String>();
        levelNames.addAll(Arrays.asList(Constants.LEVELS));
        iterator = levelNames.listIterator();
        levelName = iterator.next();
        margin = 0;
        index = 0;
        namePositions = new Array<Float>();
        this.game = game;
        gameplayScreen = game.getGameplayScreen();
    }

    @Override
    public void show() {
        // : When you're done testing, use onMobile() turn off the controls when not on a mobile device
        // onMobile();
        cursor = new CursorOverlay(145, 55);
        inputControls = new InputControls();
        cursor.setInputControls(inputControls);
        levelNumber = 0;
        batch = new SpriteBatch();
        completedLevels = new Array<String>();
        Gdx.input.setInputProcessor(inputControls);
    }

    private boolean onMobile() {
        return Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        cursor.getViewport().update(width, height, true);
        inputControls.getViewport().update(width, height, true);
        inputControls.recalculateButtonPositions();
    }

    @Override
    public void dispose() {
        Assets.getInstance().dispose();
    }

    public void update() {
    }

    @Override
    public void render(float delta) {

        viewport.apply();
        batch.begin();

        Gdx.gl.glClearColor(
                Constants.BACKGROUND_COLOR.r,
                Constants.BACKGROUND_COLOR.g,
                Constants.BACKGROUND_COLOR.b,
                Constants.BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        while (iterator.hasNext()) {
            iterator.next();
        }

        float verticalPosition = viewport.getWorldHeight() / 2.5f;
        namePositions.add(verticalPosition);
        while (iterator.hasPrevious()) {
            levelName = iterator.previous();
            if (cursor.getPosition() >= namePositions.get(index) - 15 && cursor.getPosition() < namePositions.get(index)) {
                selectedLevel = levelName;
            }
            levelName = levelName.replace("levels/", "");
            levelName = levelName.replace(".dt", "");
            font.draw(batch, levelName, viewport.getWorldWidth() / 2.5f, namePositions.get(index));
            verticalPosition += 15;
            namePositions.add(verticalPosition);
            index++;
        }

        index = 0;
        margin = 0;
        batch.end();

        inputControls.update();
        inputControls.render(batch);
        batch.begin();

        cursor.setInputControls(inputControls);
        cursor.render(batch);
        cursor.update();
        batch.end();

        if (inputControls.shootButtonJustPressed) {
            gameplayScreen.setGame(game);
            gameplayScreen.setLevelName(selectedLevel);
            game.setScreen(gameplayScreen);
        }
    }

    public final String getSelectedLevel() { return selectedLevel; }
}