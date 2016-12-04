package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.udacity.gamedev.gigagal.Level;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Utils;

public class ChargedAmmo extends Ammo {

    //ctor
    public ChargedAmmo(Level level, Vector2 position, Enums.Direction direction, Enums.AmmoType ammoType) {
        super(level, position, direction, ammoType);
    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion region = null;
        Vector2 bulletCenter = new Vector2();
        switch (super.getAmmoType()) {
            case REGULAR:
                region = Assets.getInstance().getAmmoAssets().electricShot;
                bulletCenter.set(Constants.BULLET_CENTER);
                break;
            case CHARGE:
                region = Assets.getInstance().getAmmoAssets().electricBlast;
                bulletCenter.set(Constants.CHARGE_BULLET_CENTER);
                break;
        }
        Utils.drawTextureRegion(batch, region, super.getPosition(), bulletCenter);
    }
}
