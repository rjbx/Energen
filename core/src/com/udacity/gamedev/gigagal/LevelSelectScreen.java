package com.udacity.gamedev.gigagal;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.udacity.gamedev.gigagal.overlays.LevelSelectCursor;
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
    private LevelSelectCursor overlay;
    private Array<Float> namePositions;
    private String selectedLevel;
    private int index;

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
    }

    @Override
    public void show() {
        // : When you're done testing, use onMobile() turn off the controls when not on a mobile device
        // onMobile();
        overlay = new LevelSelectCursor();
        levelNumber = 0;
        batch = new SpriteBatch();
        completedLevels = new Array<String>();
    }

    private boolean onMobile() {
        return Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        overlay.getViewport().update(width, height, true);
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


        overlay.render(batch);
        overlay.update();

        while (iterator.hasNext()) {
            iterator.next();
        }

        float verticalPosition = viewport.getWorldHeight() / 2.5f;
        namePositions.add(verticalPosition);
        while (iterator.hasPrevious()) {
            levelName = iterator.previous();
            if (overlay.getPosition() >= namePositions.get(index) - 15 && overlay.getPosition() < namePositions.get(index)) {
                selectedLevel = levelName;
            }
            levelName = levelName.replace("levels/", "");
            levelName = levelName.replace(".dt", "");
            font.draw(batch, levelName, viewport.getWorldWidth() / 7, namePositions.get(index));
            verticalPosition += 15;
            namePositions.add(verticalPosition);
            index++;
        }

        index = 0;
        margin = 0;
        batch.end();


        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.getGameplayScreen().setGame(game);
            game.getGameplayScreen().setLevelName(selectedLevel);
            game.setScreen(game.getGameplayScreen());
        }
    }

    public final String getSelectedLevel() { return selectedLevel; }
}