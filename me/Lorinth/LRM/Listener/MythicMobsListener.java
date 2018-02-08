package me.Lorinth.LRM.Listener;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobSpawnEvent;
import me.Lorinth.LRM.Data.MythicMobsDataManager;
import me.Lorinth.LRM.LorinthsRpgMobs;
import me.Lorinth.LRM.Util.MetaDataConstants;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class MythicMobsListener implements Listener {

    private MythicMobsDataManager dataManager;

    public MythicMobsListener(MythicMobsDataManager dataManager){
        this.dataManager = dataManager;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onMythicMobSpawn(MythicMobSpawnEvent event){
        if(dataManager.isWorldIgnored(event.getLocation().getWorld()))
            return;

        Entity entity = event.getEntity();

        Integer level = LorinthsRpgMobs.GetLevelOfEntity(event.getEntity());
        if(level != null)
            event.setMobLevel(level);

        if(LorinthsRpgMobs.GetMobVariantOfEntity(entity) != null)
            LorinthsRpgMobs.GetMobVariantOfEntity(entity).remove(entity);

    }

    public boolean isMythicMob(Entity entity){
        return MythicMobs.inst().getAPIHelper().isMythicMob(entity);
    }
}
