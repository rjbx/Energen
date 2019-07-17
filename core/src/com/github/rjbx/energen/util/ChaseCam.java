package com.github.rjbx.energen.util;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.github.rjbx.energen.entity.Avatar;

import static com.github.rjbx.energen.util.Enums.ChaseCamState.CONVERT;
import static com.github.rjbx.energen.util.Enums.ChaseCamState.FOLLOWING;

// TODO[L]: Add zoom toggle from overworld pause
// immutable singleton
public final class ChaseCam {

    // fields
    public static final String TAG = ChaseCam.class.getName();
    private static final ChaseCam INSTANCE = new ChaseCam();
    private ExtendViewport viewport;
    private OrthographicCamera camera;
    private Avatar avatar;
    private Vector2 roomPosition;
    private Array<Rectangle> convertBoundsArray;
    private Enums.ChaseCamState state;
    private long convertStartTime;
    private InputControls inputControls;

    // cannot be subclassed
    private ChaseCam() {}

    // static factory
    public static ChaseCam getInstance() {
        return INSTANCE;
    }

    public void create() {
        viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        state = FOLLOWING;
        avatar = Avatar.getInstance();
        convertStartTime = 0;
        convertBoundsArray = new Array<Rectangle>();
        camera = (OrthographicCamera) viewport.getCamera();
        inputControls = InputControls.getInstance();
        camera.zoom *= 1.25f;
    }

    public void update(SpriteBatch batch, float delta) {
        batch.begin();
        switch (state) {
            case FOLLOWING:
                camera.position.x = avatar.getPosition().x;
                if (avatar.getLookStartTime() != 0 && avatar.getGroundState() == Enums.GroundState.PLANTED) {
                    camera.position.y = avatar.getChaseCamPosition().y;
                } else {
                    camera.position.y = avatar.getPosition().y;
                }
                if (convertStartTime != 0 && Helpers.secondsSince(convertStartTime) > .5f) {
                    state = CONVERT;
                }
                break;
            case BOSS:
                camera.position.set(roomPosition.x, roomPosition.y, 0);
                break;
            case DEBUG:
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
                break;
            case CONVERT:
                int index = (int) (Helpers.secondsSince(convertStartTime) - 1);
                if (convertStartTime == 0) {
                    convertStartTime = TimeUtils.nanoTime();
                    state = FOLLOWING;
                } else if (index < convertBoundsArray.size){
                    camera.position.set(convertBoundsArray.get(index).x + convertBoundsArray.get(index).getWidth() / 2, convertBoundsArray.get(index).y + convertBoundsArray.get(index).getHeight() / 2, 0);
                } else {
                    convertBoundsArray.clear();
                    state = FOLLOWING;
                    convertStartTime = 0;
                }
                break;
        }
        batch.end();
    }

    public long getConvertStartTime() { return convertStartTime; }
    public final ExtendViewport getViewport() { return viewport; }
    public final Array<Rectangle> getConvertBounds() { return convertBoundsArray; }
    public final void setConvertBounds(Rectangle convertBounds) { this.convertBoundsArray.add(convertBounds); }
    public final void setInputControls(InputControls inputControls) { this.inputControls = inputControls; }
    public final void setRoomPosition(Vector2 position) { roomPosition = position; }
    public OrthographicCamera getCamera() { return camera; }
    public void setCamera(OrthographicCamera camera) { this.camera = camera; }
    public Avatar getAvatar() { return avatar; }
    public void setAvatar(Avatar avatar) { this.avatar = avatar; }
    public final Enums.ChaseCamState getState() { return state; }
    public final void setState(Enums.ChaseCamState state) {
        this.state = state;
        switch (state) {
            case BOSS: camera.zoom += .25f; break;
            case DEBUG: camera.zoom = .75f; break;
            default: camera.zoom = 1.25f;
        }
    }
}
