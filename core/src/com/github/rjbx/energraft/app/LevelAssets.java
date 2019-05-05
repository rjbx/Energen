package com.github.rjbx.energage.app;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.rjbx.energage.entity.Avatar;
import com.github.rjbx.energage.entity.Boss;
import com.github.rjbx.energage.entity.Entity;
import com.github.rjbx.energage.entity.Ground;
import com.github.rjbx.energage.entity.Hazard;
import com.github.rjbx.energage.entity.Powerup;
import com.github.rjbx.energage.util.Enums;

// TODO[H]: Either
//  a. Consolidate into single 'open-world' level with additional save points and
//   i. Remove level selector
//   ii. Display images of save points overlaid on map for level selection
//  b. Improve existing levels and remove unfinished
public final class LevelAssets {

    public static final String TAG = LevelAssets.class.toString();
    private static LevelUpdater levelUpdater = LevelUpdater.getInstance();

    // non-instantiable
    private LevelAssets() {}

    // Public getters
    public static final Array<Entity> getClonedEntities() {
        Array<Entity> clonedEntities = new Array<Entity>();
        for (Entity entity : levelUpdater.getEntities()) {
            clonedEntities.add(entity.clone());
        }
        return clonedEntities;
    }

    public static final Array<Ground> getClonedGrounds() {
        Array<Ground> clonedGrounds = new Array<Ground>();
        for (Ground ground : levelUpdater.getGrounds()) {
            clonedGrounds.add((Ground) ground.clone());
        }
        return clonedGrounds;
    }

    public static final Array<Hazard> getClonedHazards() {
        Array<Hazard> clonedHazards = new Array<Hazard>();
        for (Hazard hazard : levelUpdater.getHazards()) {
            clonedHazards.add((Hazard) hazard.clone());
        }
        return clonedHazards;
    }

    public static final Array<Powerup> getClonedPowerups() {
        Array<Powerup> clonedPowerups = new Array<Powerup>();
        for (Powerup powerup : levelUpdater.getPowerups()) {
            clonedPowerups.add((Powerup) powerup.clone());
        }
        return clonedPowerups;
    }

    public static final Boss getClonedBoss() { return (Boss) levelUpdater.getBoss().clone(); }
    public static final Avatar getClonedAvatar() { return (Avatar) levelUpdater.getAvatar().clone(); }

    public static final long getTime() { return levelUpdater.getTime(); }
    public static final int getScore() { return levelUpdater.getScore(); }
    public static final Enums.Material getType() { return levelUpdater.getType(); }
    public static final Viewport getViewport() { return levelUpdater.getViewport(); }
}
