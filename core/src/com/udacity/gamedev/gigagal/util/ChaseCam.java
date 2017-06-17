package com.udacity.gamedev.gigagal.util;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.udacity.gamedev.gigagal.entity.GigaGal;

import static com.udacity.gamedev.gigagal.util.Enums.ChaseCamState.CONVERT;
import static com.udacity.gamedev.gigagal.util.Enums.ChaseCamState.FOLLOWING;

// immutable singleton
public final class ChaseCam {

    // fields
    public static final String TAG = ChaseCam.class.getName();
    private static final ChaseCam INSTANCE = new ChaseCam();
    public Camera camera;
    public GigaGal target;
    public Vector2 roomPosition;
    public Array<Rectangle> convertBoundsArray;
    private Enums.ChaseCamState state;
    private long convertStartTIme;
    private InputControls inputControls;

    // cannot be subclassed
    private ChaseCam() {}

    // static factory
    public static ChaseCam getInstance() {
        return INSTANCE;
    }

    public void create() {
        state = FOLLOWING;
        convertStartTIme = 0;
        convertBoundsArray = new Array<Rectangle>();
    }

    public void update(SpriteBatch batch, float delta) {
        batch.begin();
        switch (state) {
            case FOLLOWING:
                camera.position.x = target.getPosition().x;
                if (target.getLookStartTime() != 0 && target.getGroundState() == Enums.GroundState.PLANTED) {
                    camera.position.y = target.getChaseCamPosition().y;
                } else {
                    camera.position.y = target.getPosition().y;
                }
                if (convertStartTIme != 0 && Helpers.secondsSince(convertStartTIme) > .5f) {
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
                int index = (int) (Helpers.secondsSince(convertStartTIme) - 1);
                if (convertStartTIme == 0) {
                    convertStartTIme = TimeUtils.nanoTime();
                    state = FOLLOWING;
                } else if (index < convertBoundsArray.size){
                    camera.position.set(convertBoundsArray.get(index).x + convertBoundsArray.get(index).getWidth() / 2, convertBoundsArray.get(index).y + convertBoundsArray.get(index).getHeight() / 2, 0);
                } else {
                    convertBoundsArray.clear();
                    state = FOLLOWING;
                    convertStartTIme = 0;
                }
                break;
        }
        batch.end();
    }

    public final Array<Rectangle> getConvertBoundsArray() { return convertBoundsArray; }
    public final void setConvertBounds(Rectangle convertBounds) { this.convertBoundsArray.add(convertBounds); }
    public final void setInputControls(InputControls inputControls) { this.inputControls = inputControls; }
    public final void setRoomPosition(Vector2 position) { roomPosition = position; }
    public final void setState(Enums.ChaseCamState state) { this.state = state; }
    public final Enums.ChaseCamState getState() { return state; }
}
