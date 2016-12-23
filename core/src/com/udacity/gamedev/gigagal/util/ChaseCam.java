package com.udacity.gamedev.gigagal.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.udacity.gamedev.gigagal.entities.GigaGal;

// immutable singleton
public final class ChaseCam {

    // fields
    public static final String TAG = ChaseCam.class.getName();
    public Camera camera;
    public GigaGal target;
    private static Boolean following;
    private static final ChaseCam INSTANCE = new ChaseCam();

    // non-instantiable; cannot be subclassed
    private ChaseCam() { following = true; }

    // static factory
    public static ChaseCam getInstance() {
        return INSTANCE;
    }

    public void update(float delta) {

        if (Gdx.input.isKeyJustPressed(Keys.TAB)) {
            following = !following;
        }

        if (following) {
            camera.position.x = target.getPosition().x;
            camera.position.y = target.getPosition().y;
        } else {
            if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
                camera.position.x -= delta * Constants.CHASE_CAM_MOVE_SPEED;
            }
            if (Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT)) {
                camera.position.x += delta * Constants.CHASE_CAM_MOVE_SPEED;
            }
            if (Gdx.input.isKeyPressed(Keys.NUM_6)) {
                camera.position.y += delta * Constants.CHASE_CAM_MOVE_SPEED;
            }
            if (Gdx.input.isKeyPressed(Keys.SPACE)) {
                camera.position.y -= delta * Constants.CHASE_CAM_MOVE_SPEED;
            }
        }
    }

    public final void setFollowing(boolean following) { this.following = following; }
    public final boolean getFollowing() { return following; }
}
