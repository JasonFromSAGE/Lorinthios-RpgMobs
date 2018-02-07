package me.Lorinth.LRM.Listener;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobSpawnEvent;
import me.Lorinth.LRM.Data.MythicMobsDataManager;
import me.Lorinth.LRM.LorinthsRpgMobs;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MythicMobsListener implements Listener {

    private MythicMobsDataManager dataManager;

    public MythicMobsListener(MythicMobsDataManager dataManager){
        this.dataManager = dataManager;
    }

    @EventHandler(ignoreCancelled = true)
    public void onMythicMobSpawn(MythicMobSpawnEvent event){
        if(dataManager.isWorldIgnored(event.getLocation().getWorld()))
            return;

        Integer level = LorinthsRpgMobs.GetLevelOfEntity(event.getEntity());
        if(level != null)
            event.setMobLevel(level);
    }

    public boolean isMythicMob(Entity entity){
        return MythicMobs.inst().getAPIHelper().isMythicMob(entity);
    }
}
