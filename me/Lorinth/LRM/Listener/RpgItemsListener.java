package me.Lorinth.LRM.Listener;

import me.lorinth.rpgitems.events.PlayerExperienceGainEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.UUID;

public class RpgItemsListener implements Listener {

    private static HashMap<UUID, Integer> entityExperience = new HashMap<>();

    @EventHandler(ignoreCancelled = true)
    public void onPlayerGainExperience(PlayerExperienceGainEvent event){
        Entity entity = event.getEntity();
        Integer exp = entityExperience.get(entity.getUniqueId());

        if(exp != null)
            event.setExp(exp);
    }

    public void bindExperienceEvent(UUID uid, Integer exp){
        entityExperience.put(uid, exp);
    }

}
