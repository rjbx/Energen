//package com.udacity.gamedev.gigagal.entity;
//
//import com.badlogic.gdx.graphics.g2d.Animation;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.graphics.g2d.TextureRegion;
//import com.badlogic.gdx.math.Rectangle;
//import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.utils.TimeUtils;
//import com.badlogic.gdx.utils.viewport.Viewport;
//import com.udacity.gamedev.gigagal.app.LevelUpdater;
//import com.udacity.gamedev.gigagal.util.Assets;
//import com.udacity.gamedev.gigagal.util.Constants;
//import com.udacity.gamedev.gigagal.util.Enums;
//import com.udacity.gamedev.gigagal.util.Enums.*;
//import com.udacity.gamedev.gigagal.util.Helpers;
//
//// immutable
//public final class Blade extends Hazard implements Indestructible, Orientable {
//
//    // fields
//    public final static String TAG = Blade.class.getName();
//    public static final Blade INSTANCE = new Blade();
//    private LevelUpdater level;
//    private Vector2 position;
//    private Vector2 bladeCenter;
//    private Direction direction;
//    private Orientation orientation;
//    private ShotIntensity shotIntensity;
//    private Material weapon;
//    private Rectangle bounds;
//    private int damage;
//    private float radius;
//    private float scale;
//    private float rotation;
//    private Entity source;
//    private boolean active;
//    private int hitScore;
//    private Vector2 knockback; // class-level instantiation
//    private Animation animation;
//    private long startTime;
//
//    // non-instantiable
//    private Blade() {}
//
//    public static Blade getInstance() { return INSTANCE; }
//
//    public void create() {}
//
////
////    // ctor
////    public Blade(LevelUpdater level, Vector2 position, Direction direction, ShotIntensity shotIntensity, Material weapon, Entity source) {
////        this.level = level;
////        this.position = position;
////        this.direction = direction;
////        this.orientation = orientation;
////        this.shotIntensity = shotIntensity;
////        this.weapon = weapon;
////        this.source = source;
////        startTime = TimeUtils.nanoTime();
////        knockback = new Vector2();
////        damage = 0;
////        active = true;
////        hitScore = 0;
////        animation = null;
////        scale = 1;
////        rotation = 0;
////        if (shotIntensity == ShotIntensity.BLAST) {
////            radius = Constants.BLAST_RADIUS;
////        } else if (shotIntensity == ShotIntensity.CHARGED) {
////            scale += (Constants.CHARGE_DURATION / 3);
////            radius = Constants.SHOT_RADIUS;
////            radius *= scale;
////        } else {
////            radius = Constants.SHOT_RADIUS;
////        }
////        if (orientation == Orientation.Y) {
////            rotation = 90;
////            bladeCenter = new Vector2(-radius, radius);
////        } else {
////            bladeCenter = new Vector2(radius, radius);
////        }
////        bounds = new Rectangle(getLeft(), getBottom(), getWidth(), getHeight());
////        damage = Constants.AMMO_STANDARD_DAMAGE;
////        knockback = Constants.ZOOMBA_KNOCKBACK;
////    }
//
//    public void update(float delta) {
//        setAttributes(GigaGal.getInstance().getWeapon());
////        float bladeSpeed = Constants.AMMO_MAX_SPEED;
////        if (!(source instanceof GigaGal)) {
////            bladeSpeed = Constants.AMMO_NORMAL_SPEED;
////        }
////
////        if (orientation == Orientation.X) {
////            switch (direction) {
////                case LEFT:
////                    position.x -= delta * bladeSpeed;
////                    break;
////                case RIGHT:
////                    position.x += delta * bladeSpeed;
////                    break;
////            }
////        } else if (orientation == Orientation.Y) {
////            switch (direction) {
////                case DOWN:
////                    position.y -= delta * bladeSpeed;
////                    break;
////                case UP:
////                    position.y += delta * bladeSpeed;
////                    break;
////            }
////        }
////
////        if (orientation == Orientation.X) {
////            final float rangeWidth = level.getViewport().getWorldWidth() * 1.5f;
////            final float cameraX = level.getViewport().getCamera().position.x;
////            if (position.x < (cameraX - rangeWidth)
////                    || (position.x > (cameraX + rangeWidth))) {
////                active = false;
////            }
////        } else if (orientation == Orientation.Y) {
////            final float rangeHeight = level.getViewport().getWorldHeight() * 1.5f;
////            final float cameraY = level.getViewport().getCamera().position.y;
////            if (position.y < (cameraY - rangeHeight)
////                    || (position.y > (cameraY + rangeHeight))) {
////                active = false;
////            }
////        }
////        bounds.setCenter(position.x, position.y);
//    }
//
//    @Override
//    public void render(SpriteBatch batch, Viewport viewport) {
//        if (active) {
//            Helpers.drawTextureRegion(batch, viewport, animation.getKeyFrame(Helpers.secondsSince(startTime)), position, bladeCenter, scale, rotation);
//        }
//
//    }
//
//    public void setAttributes(Material weapon) {
//        switch(weapon) {
//        }
//    }
//
//    public final boolean isActive() { return active; }
//    public final void deactivate() { active = false; }
//    public final Enums.Direction getDirection() { return direction; }
//    @Override public final Vector2 getPosition() { return position; }
//    @Override public final float getWidth() { return radius * 2; }
//    @Override public final float getHeight() { return radius * 2; }
//    @Override public final float getLeft() { return position.x - radius; }
//    @Override public final float getRight() { return position.x + radius; }
//    @Override public final float getTop() { return position.y + radius; }
//    @Override public final float getBottom() { return position.y - radius; }
//    @Override public final Orientation getOrientation() { return orientation; }
//    public Rectangle getBounds() { return bounds; }
//    public final float getRadius() { return radius; }
//    public final int getDamage() { return damage; }
//    public final Vector2 getKnockback() { return knockback; }
//    public final ShotIntensity getShotIntensity() { return shotIntensity; }
//    public final Material getType() { return weapon; }
//    public final TextureRegion getTexture() { return animation.getKeyFrame(0); }
//    public final int getHitScore() { return hitScore; }
//    public final void setHitScore(int hitScore) { this.hitScore = hitScore; }
//    public final Entity getSource() { return source; }
//}
