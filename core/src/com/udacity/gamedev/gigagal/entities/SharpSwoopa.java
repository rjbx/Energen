package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.udacity.gamedev.gigagal.Level;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Utils;

public class SharpSwoopa extends Swoopa {

    //ctor
    public SharpSwoopa(Platform platform, Level level) {
        super(platform, level);
    }

    @Override
    public void render(SpriteBatch batch) {
        final TextureRegion region = Assets.getInstance().getSharpSwoopaAssets().sharpSwoopa;
        Utils.drawTextureRegion(batch, region, super.getPosition(), Constants.SWOOPA_CENTER);
    }

    @Override
    public Vector2 getMountKnockback() { return Constants.SPIKE_KNOCKBACK; }


    @Override
    public int getMountDamage() {return Constants.SPIKE_DAMAGE; }
}
