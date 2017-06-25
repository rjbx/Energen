package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.app.LevelUpdater;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.ChaseCam;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Helpers;

public class Cannon extends Ground implements Nonstatic, Rappelable, Convertible {

    // fields
    public final static String TAG = Cannon.class.getName();
    private Vector2 position;
    private Vector2 center;
    private Enums.Orientation orientation;
    private Enums.ShotIntensity intensity;
    private TextureRegion region;
    private long startTime;
    private boolean active;

    // ctor
    public Cannon(Vector2 position, Enums.Orientation orientation, Enums.ShotIntensity intensity, boolean active) {
        this.position = position;
        this.orientation = orientation;
        this.intensity = intensity;
        startTime = 0;
        this.active = active;
        switch (orientation) {
            case Y:
                region = Assets.getInstance().getGroundAssets().yCannon;
                center = Constants.Y_CANNON_CENTER;
                break;
            case X:
                region = Assets.getInstance().getGroundAssets().xCannon;
                center = Constants.X_CANNON_CENTER;
                break;
        }
    }

    public void update(float delta) {
        if (active) {
            if (this.getStartTime() == 0) {
                this.setStartTime(TimeUtils.nanoTime());
            }
            if ((Helpers.secondsSince(this.getStartTime()) > 1.5f)) {
                this.setStartTime(TimeUtils.nanoTime());
                Enums.Orientation orientation = this.getOrientation();
                if (orientation == Enums.Orientation.X) {
                    Vector2 ammoPositionLeft = new Vector2(this.getPosition().x - (this.getWidth() / 2), this.getPosition().y);
                    Vector2 ammoPositionRight = new Vector2(this.getPosition().x + (this.getWidth() / 2), this.getPosition().y);
                    if (GigaGal.getInstance().getPosition().x < (ammoPositionLeft.x - (this.getWidth() / 2))) {
                        LevelUpdater.getInstance().spawnAmmo(ammoPositionLeft, Enums.Direction.LEFT, orientation, this.getIntensity(), LevelUpdater.getInstance().getType(), false);
                    } else if (GigaGal.getInstance().getPosition().x > (ammoPositionRight.x + (this.getWidth() / 2))) {
                        LevelUpdater.getInstance().spawnAmmo(ammoPositionRight, Enums.Direction.RIGHT, orientation, this.getIntensity(), LevelUpdater.getInstance().getType(), false);
                    }
                } else if (this.getOrientation() == Enums.Orientation.Y) {
                    Vector2 ammoPositionTop = new Vector2(this.getPosition().x, this.getPosition().y + (this.getHeight() / 2));
                    Vector2 ammoPositionBottom = new Vector2(this.getPosition().x, this.getPosition().y - (this.getHeight() / 2));
                    if (GigaGal.getInstance().getPosition().y < (ammoPositionBottom.y - (this.getHeight() / 2))) {
                        LevelUpdater.getInstance().spawnAmmo(ammoPositionBottom, Enums.Direction.DOWN, orientation, this.getIntensity(), LevelUpdater.getInstance().getType(), false);
                    } else if (GigaGal.getInstance().getPosition().y > (ammoPositionTop.y + (this.getHeight() / 2))) {
                        LevelUpdater.getInstance().spawnAmmo(ammoPositionTop, Enums.Direction.UP, orientation, this.getIntensity(), LevelUpdater.getInstance().getType(), false);
                    }
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        Helpers.drawTextureRegion(batch, viewport, region, getPosition(), getCenter());
    }

    public final Vector2 getPosition() { return position; }
    public final Vector2 getCenter() { return center; }
    public final float getWidth() { return center.x * 2; }
    public final float getHeight() { return center.y * 2; }
    public final float getLeft() { return position.x - center.x; }
    public final float getRight() { return position.x + center.x; }
    public final float getTop() { return position.y + center.y; }
    public final float getBottom() { return position.y - center.y; }
    @Override public final boolean isDense() { return true; }
    @Override public void convert() { active = !active; }
    @Override public boolean isConverted() {  return active; }
    public final Enums.Orientation getOrientation() { return orientation; }
    public final Enums.ShotIntensity getIntensity() { return intensity; }
    public final long getStartTime() { return startTime; }
    public final void setStartTime(long startTime) { this.startTime = startTime; }
}
