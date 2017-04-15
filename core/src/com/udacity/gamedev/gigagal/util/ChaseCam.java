package com.udacity.gamedev.gigagal.util;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.entities.GigaGal;

// immutable singleton
public final class ChaseCam {

    // fields
    public static final String TAG = ChaseCam.class.getName();
    private static final ChaseCam INSTANCE = new ChaseCam();
    public Camera camera;
    public GigaGal target;
    private boolean following;
    private InputControls inputControls;

    // cannot be subclassed
    private ChaseCam() {}

    // static factory
    public static ChaseCam getInstance() {
        return INSTANCE;
    }

    public void create() {
        following = true;
    }

    public void update(SpriteBatch batch, Viewport viewport, float delta) {
        batch.begin();
        if (following) {
            camera.position.x = target.getPosition().x;
            if (target.getLookStartTime() != 0 && target.getGroundState() == Enums.GroundState.PLANTED) {
                camera.position.y = target.getChaseCamPosition().y;
            } else {
                camera.position.y = target.getPosition().y;
            }
        } else {
            if (inputControls.leftButtonPressed) {
                camera.position.x -= delta * Constants.CHASE_CAM_MOVE_SPEED;
            }
            if (inputControls.rightButtonPressed) {
                camera.position.x += delta * Constants.CHASE_CAM_MOVE_SPEED;
            }
            if (inputControls.upButtonPressed) {
                camera.position.y += delta * Constants.CHASE_CAM_MOVE_SPEED;
            }
            if (inputControls.downButtonPressed) {
                camera.position.y -= delta * Constants.CHASE_CAM_MOVE_SPEED;
            }
        }
        batch.end();
    }

    public final void setFollowing(boolean following) { this.following = following; }
    public final void setInputControls(InputControls inputControls) { this.inputControls = inputControls; }
    public final boolean getFollowing() { return following; }
}
