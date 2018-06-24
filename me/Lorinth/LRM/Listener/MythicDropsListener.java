package me.Lorinth.LRM.Listener;

import me.Lorinth.LRM.Data.MythicDropsDataManager;
import me.Lorinth.LRM.LorinthsRpgMobs;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class MythicDropsListener implements Listener{

    private MythicDropsDataManager manager;

    public MythicDropsListener(MythicDropsDataManager manager){
        this.manager = manager;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event){
        if(event.getEntity().getKiller() != null){
            Integer level = LorinthsRpgMobs.GetLevelOfEntity(event.getEntity());
            if(level != null){
                event.getDrops().add(manager.getDrop(event.getEntity(), level));
            }
        }
    }

}
