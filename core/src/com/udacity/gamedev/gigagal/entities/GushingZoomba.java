package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Utils;

public class GushingZoomba extends Zoomba {

    //ctor
    public GushingZoomba(Box box) {
        super(box);
    }

    @Override
    public void render(SpriteBatch batch) {
        final float elapsedTime = Utils.secondsSince(super.getStartTime());
        final TextureRegion region = Assets.getInstance().getGushingZoombaAssets().gushingZoomba.getKeyFrame(elapsedTime, true);
        Utils.drawTextureRegion(batch, region, super.getPosition(), Constants.ZOOMBA_CENTER);
    }

    @Override public Vector2 getMountKnockback() { return Constants.GEISER_KNOCKBACK; }
    @Override public int getMountDamage() {return Constants.GEISER_DAMAGE; }
    @Override public Enums.WeaponType getType() { return Enums.WeaponType.WATER; }
}
