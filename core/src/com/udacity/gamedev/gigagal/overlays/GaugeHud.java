package com.udacity.gamedev.gigagal.overlays;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.Level;
import com.udacity.gamedev.gigagal.entities.GigaGal;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Utils;

// immutable
public final class GaugeHud {

    // fields
    private final ExtendViewport viewport;
    private final GigaGal gigaGal;
    private float flickerFrequency;

    // default ctor
    public GaugeHud(Level level) {
        this.gigaGal = level.getGigaGal();
        this.viewport = new ExtendViewport(Constants.HUD_VIEWPORT_SIZE, Constants.HUD_VIEWPORT_SIZE);
        flickerFrequency = 1;
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

        if (gigaGal.getHealth() < Constants.MAX_HEALTH / 4) {
            renderer.setColor(Constants.HEALTH_CRITICAL_COLOR);
        } else if (gigaGal.getHealth() < Constants.MAX_HEALTH / 2) {
            renderer.setColor(Constants.HEALTH_LOW_COLOR);
        } else if (gigaGal.getHealth() < Constants.MAX_HEALTH) {
            renderer.setColor(Constants.HEALTH_NORMAL_COLOR);
        } else {
            renderer.setColor(Constants.HEALTH_MAX_COLOR);
        }
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.rect(viewport.getScreenX(), viewport.getWorldHeight() - Constants.HUD_MARGIN, ((float) gigaGal.getHealth() / Constants.MAX_HEALTH) * viewport.getWorldWidth() / 3, viewport.getScreenHeight() / 25);

        // turbo
        if (gigaGal.getTurbo() < Constants.MAX_TURBO) {
            renderer.setColor(Constants.TURBO_NORMAL_COLOR);
        } else {
            renderer.setColor(Constants.TURBO_MAX_COLOR);
        }
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.rect(viewport.getWorldWidth() / 3, viewport.getWorldHeight() - Constants.HUD_MARGIN, (gigaGal.getTurbo() / Constants.MAX_TURBO) * viewport.getWorldWidth() / 3, viewport.getScreenHeight() / 25);


        // ammo
        if (gigaGal.getChargeTimeSeconds() == 0) {
            flickerFrequency = 1;
        }

        if (gigaGal.getChargeStatus()
        && ((gigaGal.getChargeTimeSeconds() % flickerFrequency > flickerFrequency / 2) || flickerFrequency < .01f)) {
            renderer.setColor(Constants.AMMO_CHARGED_COLOR);
            flickerFrequency /= 2;
        } else {
            renderer.setColor(Constants.AMMO_NORMAL_COLOR);
        }

        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.rect(viewport.getWorldWidth() / 3 * 2, viewport.getWorldHeight() - Constants.HUD_MARGIN, ((float) gigaGal.getAmmo() / Constants.MAX_AMMO) * viewport.getWorldWidth() / 3, viewport.getScreenHeight() / 25);
        renderer.end();


        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        batch.end();
    }

    public final Viewport getViewport() { return viewport; }
}
