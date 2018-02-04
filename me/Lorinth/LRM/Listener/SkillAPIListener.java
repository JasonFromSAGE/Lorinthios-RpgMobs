package me.Lorinth.LRM.Listener;

import com.sucy.skill.api.enums.ExpSource;
import com.sucy.skill.api.event.PlayerExperienceGainEvent;
import me.Lorinth.LRM.Objects.CreatureDeathData;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.HashMap;

public class SkillAPIListener implements Listener {

    private HashMap<Player, ArrayList<CreatureDeathData>> deathEvents = new HashMap<>();

    @EventHandler
    public void onSkillAPIExperienceGain(PlayerExperienceGainEvent event){
        if(event.getSource() == ExpSource.MOB){
            if(deathEvents.containsKey(event.getPlayerData().getPlayer())){
                ArrayList<CreatureDeathData> dataList = deathEvents.get(event.getPlayerData().getPlayer());

                if(dataList.size() > 0)
                    event.setExp((int) dataList.remove(0).getExperience());
            }
        }
    }

    public void bindDeathEvent(Player player, CreatureDeathData data){
        if(!deathEvents.containsKey(player))
            deathEvents.put(player, new ArrayList<CreatureDeathData>());

        deathEvents.get(player).add(data);
    }
}
