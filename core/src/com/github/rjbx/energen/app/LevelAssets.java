package com.github.rjbx.energen.app;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.rjbx.energen.entity.Avatar;
import com.github.rjbx.energen.entity.Boss;
import com.github.rjbx.energen.entity.Entity;
import com.github.rjbx.energen.entity.Ground;
import com.github.rjbx.energen.entity.Hazard;
import com.github.rjbx.energen.entity.Powerup;
import com.github.rjbx.energen.util.Enums;

public final class LevelAssets {

    public static final String TAG = LevelAssets.class.toString();
    private static LevelUpdater levelUpdater = LevelUpdater.getInstance();

    // non-instantiable
    private LevelAssets() {}

    // TODO: Remove / add items to list based on update bounds overlap to be globally accessed

    // Public getters
    public static final Array<Entity> getClonedEntities() {
//        Array<Entity> clonedEntities = new Array<Entity>();
//        for (Entity entity : levelUpdater.getEntities()) {
//            clonedEntities.add(entity.safeClone());
//        }
        return levelUpdater.getEntities();
    }

    public static final Array<Ground> getClonedGrounds() {
//        Array<Ground> clonedGrounds = new Array<Ground>();
//        for (Ground ground : levelUpdater.getGrounds()) {
//            clonedGrounds.add((Ground) ground.safeClone());
//        }
        return levelUpdater.getGrounds();
    }

    public static final Array<Hazard> getClonedHazards() {
//        Array<Hazard> clonedHazards = new Array<Hazard>();
//        for (Hazard hazard : levelUpdater.getHazards()) {
//            clonedHazards.add((Hazard) hazard.safeClone());
//        }
        return levelUpdater.getHazards();
    }

    public static final Array<Powerup> getClonedPowerups() {
//        Array<Powerup> clonedPowerups = new Array<Powerup>();
//        for (Powerup powerup : levelUpdater.getPowerups()) {
//            clonedPowerups.add((Powerup) powerup.safeClone());
//        }
        return levelUpdater.getPowerups();
    }

    public static final Boss getClonedBoss() { return levelUpdater.getBoss(); }
    public static final Avatar getClonedAvatar() { return levelUpdater.getAvatar(); }

    public static final long getTime() { return levelUpdater.getTime(); }
    public static final int getScore() { return levelUpdater.getScore(); }
    public static final Enums.Energy getType() { return levelUpdater.getType(); }
    public static final Viewport getViewport() { return levelUpdater.getViewport(); }
}
