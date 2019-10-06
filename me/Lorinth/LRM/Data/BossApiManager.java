package me.Lorinth.LRM.Data;

import me.Lorinth.LRM.Objects.RandomNullCollection;
import me.lorinth.bossapi.BossApi;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import java.util.HashMap;

public class BossApiManager {

    private static boolean enabled;

    public BossApiManager(){
        enabled = Bukkit.getPluginManager().getPlugin("LorinthsBossApi") != null;
    }

    public static boolean isEnabled(){
        return enabled;
    }

    public static boolean spawnBoss(Location loc, EntityType type, HashMap<EntityType, RandomNullCollection<String>> randomBossReplacement){
        if(!enabled)
            return false;

        RandomNullCollection<String> bossIds = randomBossReplacement.getOrDefault(type, null);
        if(bossIds == null)
            return false;
        else{
            return BossApi.api.bossNames.get(bossIds.next()).spawn(loc) != null;
        }
    }

}
