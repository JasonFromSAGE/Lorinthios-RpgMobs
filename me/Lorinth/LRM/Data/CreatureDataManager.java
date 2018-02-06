package me.Lorinth.LRM.Data;

import me.Lorinth.LRM.Objects.CreatureData;
import me.Lorinth.LRM.Objects.DataManager;
import me.Lorinth.LRM.Util.OutputHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Manager of all CreatureData objects in memory. Manipulate most aspects of monster data here
 */
public class CreatureDataManager implements DataManager {

    private ArrayList<String> ignoredKeys = new ArrayList<String>(){{
        add("Disabled");
        add("DisabledWorlds");
    }};
    private HashMap<String, CreatureData> animalData = new HashMap<>();
    private HashMap<String, CreatureData> monsterData = new HashMap<>();

    public CreatureData getData(EntityType type){
        HashMap<EntityType, CreatureData> creatureData = null;
        String typeString = type.toString();
        if(monsterData.containsKey(typeString))
            return monsterData.get(typeString);
        else if(animalData.containsKey(typeString))
            return animalData.get(typeString);
        else
            return null;

    }

    public CreatureData getData(Creature creature){
        CreatureData data = getData(creature.getType());
        if(data != null){
            return data;
        }
        else{
            CreatureData newData = new CreatureData(creature);
            if(creature instanceof Monster)
                monsterData.put(creature.getType().toString(), newData);
            else
                animalData.put(creature.getType().toString(), newData);
            return newData;
        }
    }

    public boolean saveData(FileConfiguration config){
        boolean changed = false;
        for(CreatureData data : animalData.values()){
            if(data.save(config, "Entity.Animal."))
                changed = true;
        }
        for(CreatureData data : monsterData.values()){
            if(data.save(config, "Entity.Monster."))
                changed = true;
        }
        return changed;
    }

    public void loadData(FileConfiguration config, Plugin plugin){
        loadCreatureSection(config, "Entity.Animal");
        loadCreatureSection(config, "Entity.Monster");
    }

    private void loadCreatureSection(FileConfiguration config, String prefix) {
        if(config.contains(prefix)){
            for(String key : config.getConfigurationSection(prefix).getKeys(false)){
                if(!ignoredKeys.contains(key))
                    loadEntity(config, prefix, key);
            }
        }
    }

    private void loadEntity(FileConfiguration config, String prefix, String key){
        try{
            EntityType type = EntityType.valueOf(key);
            if(type == null) {
                OutputHandler.PrintError("Entity type, " + OutputHandler.HIGHLIGHT + key + OutputHandler.ERROR + ", not valid. Remove from config to remove this error");
                return;
            }

            if(prefix.contains("Animal"))
                animalData.put(type.toString(), new CreatureData(type, prefix, config));
            else
                monsterData.put(type.toString(), new CreatureData(type, prefix, config));
        }
        catch(Exception error){
            OutputHandler.PrintError("Failed to load entity : " + OutputHandler.HIGHLIGHT + key);
            error.printStackTrace();
        }
    }
}
