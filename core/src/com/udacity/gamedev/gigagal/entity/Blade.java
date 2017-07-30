package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums.*;
import com.udacity.gamedev.gigagal.util.Helpers;

// immutable
public final class Blade extends Hazard implements Indestructible {

    // fields
    public final static String TAG = Blade.class.getName();
    public static final Blade INSTANCE = new Blade();
    private Vector2 position;
    private Material weapon;
    private Rectangle bounds;
    private int damage;
    private float radius;
    private float scale;
    private boolean active;
    private int hitScore;
    private Vector2 knockback; // class-level instantiation
    private Animation animation;

    // non-instantiable
    private Blade() {}

    public static Blade getInstance() { return INSTANCE; }

    public void create() {
        position = GigaGal.getInstance().getPosition();
        bounds = new Rectangle(getLeft(), getBottom(), getWidth(), getHeight());
        this.weapon = GigaGal.getInstance().getWeapon();
        knockback = new Vector2();
        damage = 0;
        active = true;
        hitScore = 0;
        animation = null;
        scale = 1;
        damage = Constants.AMMO_STANDARD_DAMAGE;
        knockback = Constants.ZOOMBA_KNOCKBACK;
    }

    public void update(float delta) {
        setAttributes(GigaGal.getInstance().getWeapon());
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        if (GigaGal.getInstance().getBladeState() != BladeState.RETRACTED)
        if (GigaGal.getInstance().getBladeState() == BladeState.FLIP) {
            if (GigaGal.getInstance().getDirectionX() == Direction.RIGHT) {
                Helpers.drawTextureRegion(batch, viewport, Assets.getInstance().getBladeAssets().backflipRight.getKeyFrame(GigaGal.getInstance().getSwipeTimeSeconds()), position, Constants.BLADE_CENTER);
            } else {
                Helpers.drawTextureRegion(batch, viewport, Assets.getInstance().getBladeAssets().backflipLeft.getKeyFrame(GigaGal.getInstance().getSwipeTimeSeconds()), position, Constants.BLADE_CENTER);
            }
        } else if (GigaGal.getInstance().getBladeState() == BladeState.RUSH) {
            if (GigaGal.getInstance().getDirectionX() == Direction.RIGHT) {
                Helpers.drawTextureRegion(batch, viewport, Assets.getInstance().getBladeAssets().forehandRight.getKeyFrame(GigaGal.getInstance().getSwipeTimeSeconds()), position, Constants.BLADE_CENTER);
            } else {
                Helpers.drawTextureRegion(batch, viewport, Assets.getInstance().getBladeAssets().forehandLeft.getKeyFrame(GigaGal.getInstance().getSwipeTimeSeconds()), position, Constants.BLADE_CENTER);
            }
        }
    }

    public void setAttributes(Material weapon) {
        switch(weapon) {

        }
    }

    public final boolean isActive() { return active; }
    @Override public final Vector2 getPosition() { return position; }
    @Override public final float getWidth() { return radius * 2; }
    @Override public final float getHeight() { return radius * 2; }
    @Override public final float getLeft() { return position.x - radius; }
    @Override public final float getRight() { return position.x + radius; }
    @Override public final float getTop() { return position.y + radius; }
    @Override public final float getBottom() { return position.y - radius; }
    public Rectangle getBounds() { return bounds; }
    public final int getDamage() { return damage; }
    public final Vector2 getKnockback() { return knockback; }
    public final Material getType() { return weapon; }
    public final int getHitScore() { return hitScore; }
}
