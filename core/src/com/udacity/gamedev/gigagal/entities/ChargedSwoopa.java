package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.udacity.gamedev.gigagal.Level;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Utils;

public class ChargedSwoopa extends Swoopa {

    //ctor
    public ChargedSwoopa(BoxGround boxGround, Level level) {
        super(boxGround, level);
    }

    @Override
    public void render(SpriteBatch batch) {
        final float elapsedTime = Utils.secondsSince(super.getStartTime());
        final TextureRegion region = Assets.getInstance().getChargedSwoopaAssets().chargedSwoopa.getKeyFrame(elapsedTime, true);
        Utils.drawTextureRegion(batch, region, super.getPosition(), Constants.SWOOPA_CENTER);
    }

    @Override public Vector2 getMountKnockback() { return Constants.COIL_KNOCKBACK; }
    @Override public int getMountDamage() {return Constants.COIL_DAMAGE; }
    @Override public Enums.WeaponType getType() { return Enums.WeaponType.ELECTRIC; }
}
