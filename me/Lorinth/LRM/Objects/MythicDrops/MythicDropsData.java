package me.Lorinth.LRM.Objects.MythicDrops;

import me.Lorinth.LRM.Util.ConfigHelper;
import me.Lorinth.LRM.Util.TryParse;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public class MythicDropsData {

    private HashMap<Integer, MythicDropsLevelData> tierData = new HashMap<>();

    public void loadData(FileConfiguration config, String prefix) {
        prefix += ".MythicDrops";

        if(ConfigHelper.ConfigContainsPath(config, prefix)){
            for(String levelKey: config.getConfigurationSection(prefix).getKeys(false)){
                if(TryParse.parseInt(levelKey)){
                    int level = Integer.parseInt(levelKey);
                    tierData.put(level, new MythicDropsLevelData(config, prefix + "." + levelKey));
                }
            }
        }
    }

    public String getNextTierName(int level){
        int highest = 0;
        for(Integer key : tierData.keySet()){
            if(key >= highest && key <= level)
                highest = key;
        }

        if(highest > 0){
            return tierData.get(highest).getTierName();
        }
        return null;
    }

}
