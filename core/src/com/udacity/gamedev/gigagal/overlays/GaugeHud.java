package com.udacity.gamedev.gigagal.overlays;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.Level;
import com.udacity.gamedev.gigagal.entities.GigaGal;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;

// immutable
public final class GaugeHud {

    // fields
    private final ExtendViewport viewport;
    private final BitmapFont font;
    private final Level level;
    private final GigaGal gigaGal;
    private Rectangle backdrop;
    private Rectangle health;
    private Rectangle turbo;
    private Rectangle ammo;
    private ShapeRenderer renderer;

    // default ctor
    public GaugeHud(Level level) {
        this.level = level;
        this.gigaGal = level.getGigaGal();
        this.viewport = new ExtendViewport(Constants.HUD_VIEWPORT_SIZE, Constants.HUD_VIEWPORT_SIZE);
        font = new BitmapFont();
        font.getData().setScale(1);
        backdrop = new Rectangle();
        health = new Rectangle();
        turbo = new Rectangle();
        ammo = new Rectangle();
        renderer = new ShapeRenderer();
    }

    public void render(SpriteBatch batch, ShapeRenderer renderer) {

        viewport.apply();
        renderer.setProjectionMatrix(viewport.getCamera().combined);
        renderer.begin();


        // backdrop
        renderer.setColor(Color.BLACK);
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.rect(viewport.getScreenX(), viewport.getWorldHeight() - Constants.HUD_MARGIN, viewport.getWorldWidth(), viewport.getScreenHeight() / 25);

        // health

        if (gigaGal.getHealth() < 20) {
            renderer.setColor(Color.RED);
        } else if (gigaGal.getHealth() < 40) {
            renderer.setColor(Color.CORAL);
        } else {
            renderer.setColor(Color.ROYAL);
        }
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.rect(viewport.getScreenX(), viewport.getWorldHeight() - Constants.HUD_MARGIN, ((float) gigaGal.getHealth() / 100) * viewport.getWorldWidth() / 3, viewport.getScreenHeight() / 25);

        // turbo
        renderer.setColor(Color.FOREST);
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.rect(viewport.getWorldWidth() / 3, viewport.getWorldHeight() - Constants.HUD_MARGIN, ((float) gigaGal.getTurbo() / 100) * viewport.getWorldWidth() / 3, viewport.getScreenHeight() / 25);


        // ammo
        if (gigaGal.getShotIntensity() == Enums.ShotIntensity.CHARGED) {
            renderer.setColor(Color.GOLD);
        } else {
            renderer.setColor(Color.GOLDENROD);
        }
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.rect(viewport.getWorldWidth() / 3 * 2, viewport.getWorldHeight() - Constants.HUD_MARGIN, ((float) gigaGal.getAmmo() / 100) * viewport.getWorldWidth() / 3, viewport.getScreenHeight() / 25);
        renderer.end();


        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        batch.end();
    }

    public final Viewport getViewport() { return viewport; }
}
