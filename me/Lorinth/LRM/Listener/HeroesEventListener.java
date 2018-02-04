package me.Lorinth.LRM.Listener;

import com.herocraftonline.heroes.Heroes;
import com.herocraftonline.heroes.api.events.ExperienceChangeEvent;
import com.herocraftonline.heroes.characters.classes.HeroClass;
import me.Lorinth.LRM.LorinthsRpgMobs;
import me.Lorinth.LRM.Objects.CreatureDeathData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class HeroesEventListener implements Listener {

    private HashMap<Location, CreatureDeathData> DeathToExperience = new HashMap<>();

    @EventHandler
    public void onExperienceChange(ExperienceChangeEvent experienceChangeEvent){
        if(experienceChangeEvent.getSource() == HeroClass.ExperienceType.KILLING){
            if(DeathToExperience.containsKey(experienceChangeEvent.getLocation())){
                experienceChangeEvent.setExpGain(DeathToExperience.get(experienceChangeEvent.getLocation()).getExperience());
            }
        }
    }

    public void bindExperienceEvent(Location location, CreatureDeathData data){
        DeathToExperience.put(location, data);
        Bukkit.getScheduler().scheduleSyncDelayedTask(LorinthsRpgMobs.instance, new BukkitRunnable() {
            @Override
            public void run() {
               DeathToExperience.remove(location);
            }
        }, 10);
    }
}
