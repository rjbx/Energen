package com.github.rjbx.energen.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.rjbx.energen.util.AssetManager;
import com.github.rjbx.energen.util.Constants;
import com.github.rjbx.energen.util.Enums;
import com.github.rjbx.energen.util.Helpers;

// mutable
public class Barrier extends Ground implements Rappelable, Hurdleable, Strikeable, Convertible {

    // fields
    public final static String TAG = Barrier.class.getName();

    private final Enums.Energy type;
    protected Vector2 position; // class-level instantiation
    private float width;
    private float height;
    protected boolean dense;
    private boolean converted;
    private boolean convertible;
    private NinePatch ninePatch;

    //default ctor
    public Barrier() {
        this.width = 0;
        this.height = 0;
        this.position = new Vector2();
        this.type = Enums.Energy.NATIVE;
        this.dense = true;
        convertible = false;
        converted = false;
        ninePatch = new NinePatch(AssetManager.getInstance().getGroundAssets().getNinePatch(this));
        setColor();
    }

    // ctor
    public Barrier(float xPos, float yPos, float width, float height, Enums.Energy type, boolean dense, boolean convertible) {
        this.width = width;
        this.height = height;
        this.position = new Vector2(xPos + (width / 2), yPos + (height / 2));
        this.type = type;
        this.dense = dense;
        this.convertible = convertible;
        converted = false;
        ninePatch = new NinePatch(AssetManager.getInstance().getGroundAssets().getNinePatch(this));
        setColor();
    }

    public Barrier safeClone() {
        Barrier clone = new Barrier(position.x, position.y, width, height, type, dense, convertible);
        clone.setClonedHashCode(hashCode());
        return clone;
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
    @Override public boolean isDense() { return dense && (height > Constants.MAX_LEDGE_HEIGHT); }
    @Override public void convert() { if (convertible) dense = !dense; converted = true; }
    @Override public boolean isConverted() { return converted; }
    public Enums.Energy getType() { return type; }
    public Color getColor() { return ninePatch.getColor(); }
    private void setColor() {
        if (isDense()) {
            ninePatch.setColor(type.theme().color().mul(.75f, .75f, .75f, 1));
        } else {
            ninePatch.setColor(new Color(type.theme().color()).mul(.7f));
        }
    }

    @Override
    public int getPriority() {
        return isDense() ? Constants.PRIORITY_MAX : Constants.PRIORITY_LOW;
    }
}
