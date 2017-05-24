package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.app.LevelUpdater;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Helpers;

public class Cannon implements Nonstatic, Ground {

    // fields
    private Vector2 position;
    private Enums.Orientation orientation;
    private Enums.ShotIntensity intensity;
    private TextureRegion region;
    private Vector2 center;
    private float offset;
    private long startTime;

    // ctor
    public Cannon(Vector2 position, Enums.Orientation orientation, Enums.ShotIntensity intensity) {

        this.position = position;
        this.orientation = orientation;
        this.intensity = intensity;
        startTime = 0;
        offset = 0;
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
    
    public void update() {
        if (this.getOffset() == 0) {
            offset += 0.25f;
            this.setStartTime(TimeUtils.nanoTime() + ((long) (this.getOffset() / MathUtils.nanoToSec)));
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

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        Helpers.drawTextureRegion(batch, viewport, region, position, center);
    }

    @Override public final Vector2 getPosition() { return position; }
    @Override public final float getWidth() { return center.x * 2; }
    @Override public final float getHeight() { return center.y * 2; }
    @Override public final float getLeft() { return position.x - center.x; }
    @Override public final float getRight() { return position.x + center.x; }
    @Override public final float getTop() { return position.y + center.y; }
    @Override public final float getBottom() { return position.y - center.y; }
    @Override public final boolean isLedge() { return false; }
    @Override public Cannon clone() { return new Cannon(position, orientation, intensity); }
    public final Enums.Orientation getOrientation() { return orientation; }
    public final Enums.ShotIntensity getIntensity() { return intensity; }
    public final float getOffset() { return offset; }
    public final void setOffset(float offset) { this.offset = offset; }
    public final long getStartTime() { return startTime; }
    public final void setStartTime(long startTime) { this.startTime = startTime; }
}
