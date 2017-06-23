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

public class Cannon extends Entity implements Nonstatic, Rappelable, Convertible, Ground {

    // fields
    public final static String TAG = Cannon.class.getName();

    private Enums.Orientation orientation;
    private Enums.ShotIntensity intensity;
    private TextureRegion region;
    private float offset;
    private long startTime;
    private boolean active;

    // ctor
    public Cannon(Vector2 position, Enums.Orientation orientation, Enums.ShotIntensity intensity, boolean active) {
        super(position);
        this.orientation = orientation;
        this.intensity = intensity;
        startTime = 0;
        offset = 0;
        this.active = active;
        switch (orientation) {
            case Y:
                region = Assets.getInstance().getGroundAssets().yCannon;
                super.setCenter(Constants.Y_CANNON_CENTER);
                break;
            case X:
                region = Assets.getInstance().getGroundAssets().xCannon;
                super.setCenter(Constants.X_CANNON_CENTER);
                break;
        }
    }

    public void update(float delta) {
        if (active) {
            if (this.getStartTime() == 0) {
                offset += 2;
                this.setStartTime(TimeUtils.nanoTime() + ((long) (this.getOffset() / MathUtils.nanoToSec)));
            }
            if ((Helpers.secondsSince(this.getStartTime()) > 2)) {
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

    @Override
    public boolean equals(Object object) {
        if (object instanceof Cannon) {
            Cannon cannon = (Cannon) object;
            return getTop() == cannon.getTop() && getBottom() == cannon.getBottom() && getLeft() == cannon.getLeft() && getRight() == cannon.getRight();
        }
        return false;
    }
    @Override public final boolean isDense() { return true; }
    @Override public void convert() { active = !active; }
    @Override public boolean isConverted() {  return active; }
    @Override public Cannon clone() { Cannon clone = new Cannon(getPosition(), orientation, intensity, active);  super.setHashCode(hashCode()); return clone; }
    public final Enums.Orientation getOrientation() { return orientation; }
    public final Enums.ShotIntensity getIntensity() { return intensity; }
    public final void setOffset(float offset) { this.offset = offset; }
    public final float getOffset() { return offset; }
    public final long getStartTime() { return startTime; }
    public final void setStartTime(long startTime) { this.startTime = startTime; }
}
