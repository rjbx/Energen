package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.util.AssetManager;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Helpers;

// mutable
public class Barrier extends Ground implements Rappelable, Hurdleable, Strikeable, Convertible {

    // fields
    public final static String TAG = Barrier.class.getName();

    private final Enums.Material type;
    protected Vector2 position; // class-level instantiation
    private float width;
    private float height;
    protected boolean dense;
    private boolean converted;
    private NinePatch ninePatch;

    //default ctor
    public Barrier() {
        this.width = 0;
        this.height = 0;
        this.position = new Vector2();
        this.type = Enums.Material.NATIVE;
        this.dense = true;
        converted = false;
        ninePatch = new NinePatch(AssetManager.getInstance().getGroundAssets().getNinePatch(this));
        setColor();
    }

    // ctor
    public Barrier(float xPos, float yPos, float width, float height, Enums.Material type, boolean dense) {
        this.width = width;
        this.height = height;
        this.position = new Vector2(xPos + (width / 2), yPos + (height / 2));
        this.type = type;
        this.dense = dense;
        converted = false;
        ninePatch = new NinePatch(AssetManager.getInstance().getGroundAssets().getNinePatch(this));
        setColor();
    }

    @Override
    public void update(float delta) {
        if (converted) {
            setColor();
            converted = false;
        }
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        Helpers.drawNinePatch(batch, viewport, ninePatch, getLeft(), getBottom(), width, height);
    }

    // Getters
    @Override public float getTop() { return position.y + height / 2; }
    @Override public float getBottom() { return position.y - height / 2; }
    @Override public float getLeft() { return position.x - width / 2; }
    @Override public float getRight() { return position.x + width / 2; }
    @Override public float getWidth() { return width; }
    @Override public float getHeight() { return height; }
    @Override public Vector2 getPosition() { return position; }
    public void setDensity(boolean state) { dense = state; }
    @Override public boolean isDense() { return dense && getHeight() > Constants.MAX_LEDGE_HEIGHT; }
    @Override public void convert() { dense = !dense; converted = true; }
    @Override public boolean isConverted() { return converted; }
    public Enums.Material getType() { return type; }
    public Color getColor() { return ninePatch.getColor(); }
    private void setColor() {
        if (isDense()) {
            ninePatch.setColor(type.theme().color().mul(.5f, .5f, .5f, 1));
        } else {
            ninePatch.setColor(new Color(type.theme().color()).mul(.7f));
        }
    }
}
