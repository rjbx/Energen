package com.udacity.gamedev.gigagal.overlay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.udacity.gamedev.gigagal.entity.Avatar;
import com.udacity.gamedev.gigagal.util.Constants;

// immutable
public final class GaugeHud {

    // fields
    public static final String TAG = GaugeHud.class.toString();
    private static final GaugeHud INSTANCE = new GaugeHud();
    private float flickerFrequency;

    // default ctor
    private GaugeHud() {
    }

    public static GaugeHud getInstance() { return INSTANCE; }

    public void create() {
        flickerFrequency = 0.5f;
    }

    public void render(ShapeRenderer renderer, ExtendViewport viewport, Avatar avatar) {

        viewport.apply();
        renderer.setProjectionMatrix(viewport.getCamera().combined);
        renderer.begin();

        // backdrop
        renderer.setColor(Color.BLACK);
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.rect(viewport.getCamera().position.x  - viewport.getWorldWidth() / 2, viewport.getCamera().position.y + viewport.getWorldHeight() / 2.25f, viewport.getWorldWidth(), viewport.getWorldHeight() / 15);

        // health
        if (avatar.getHealth() < Constants.MAX_HEALTH / 4) {
            renderer.setColor(Constants.HEALTH_CRITICAL_COLOR);
        } else if (avatar.getHealth() < Constants.MAX_HEALTH / 2) {
            renderer.setColor(Constants.HEALTH_LOW_COLOR);
        } else if (avatar.getHealth() < Constants.MAX_HEALTH) {
            renderer.setColor(avatar.getWeapon().theme().color().mul(.8f));
        } else {
            renderer.setColor(avatar.getWeapon().theme().color());
        }
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.rect(viewport.getCamera().position.x - viewport.getWorldWidth() / 2, viewport.getCamera().position.y + viewport.getWorldHeight() / 2.25f, ((float) avatar.getHealth() / Constants.MAX_HEALTH) * viewport.getWorldWidth() / 3, viewport.getWorldHeight() / 15);

        // turbo
        if (avatar.getTurbo() < Constants.MAX_TURBO) {
            renderer.setColor(Constants.TURBO_NORMAL_COLOR);
        } else {
            renderer.setColor(Constants.TURBO_MAX_COLOR);
        }
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.rect(viewport.getCamera().position.x  - viewport.getWorldWidth() / 6, viewport.getCamera().position.y + viewport.getWorldHeight() / 2.25f, (avatar.getTurbo() / Constants.MAX_TURBO) * viewport.getWorldWidth() / 3, viewport.getWorldHeight() / 15);

        // ammo
        if (avatar.getChargeTimeSeconds() >  Constants.BLAST_CHARGE_DURATION / 4) {
            renderer.setColor(Constants.AMMO_CHARGED_COLOR);
        } else {
            renderer.setColor(Constants.AMMO_NORMAL_COLOR);
        }

        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.rect(viewport.getCamera().position.x  + viewport.getWorldWidth() / 6, viewport.getCamera().position.y + viewport.getWorldHeight() / 2.25f, ((float) avatar.getAmmo() / Constants.MAX_AMMO) * viewport.getWorldWidth() / 3, viewport.getWorldHeight() / 15);
        renderer.end();
    }
}