package me.Lorinth.LRM.Data;

import me.Lorinth.LRM.Objects.CreatureData;
import me.Lorinth.LRM.Objects.OutputHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by lorinthio on 1/27/2018.
 */
public class CreatureDataManager {

    private ArrayList<String> ignoredKeys = new ArrayList<String>(){{
        add("Disabled");
        add("DisabledWorlds");
    }};
    private HashMap<EntityType, CreatureData> animalData = new HashMap<>();
    private HashMap<EntityType, CreatureData> monsterData = new HashMap<>();
    private DataLoader dataLoader;

    public CreatureDataManager(DataLoader loader){
        dataLoader = loader;
    }

    public CreatureData getData(EntityType type){
        HashMap<EntityType, CreatureData> creatureData = null;
        if(monsterData.containsKey(type))
            return monsterData.get(type);
        else if(animalData.containsKey(type))
            return animalData.get(type);
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
                monsterData.put(creature.getType(), newData);
            else
                animalData.put(creature.getType(), newData);
            return newData;
        }
    }

    protected boolean saveCreatureData(FileConfiguration config){
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

    protected void loadCreatureData(FileConfiguration config){
        loadCreatureSection(config, "Entity.Animal.");
        loadCreatureSection(config, "Entity.Monster.");

    }

    private void loadCreatureSection(FileConfiguration config, String prefix) {
        for(String key : config.getConfigurationSection(prefix).getKeys(false)){
            if(!ignoredKeys.contains(key))
                loadEntity(config, prefix, key);
        }
    }

    private void loadEntity(FileConfiguration config, String prefix, String key){
        try{
            EntityType type = EntityType.valueOf(key);
            if(type == null) {
                OutputHandler.PrintError("Failed to find entity type for, " + OutputHandler.HIGHLIGHT + key);
                return;
            }

            if(prefix.contains("Animal"))
                animalData.put(type, new CreatureData(type, prefix, config));
            else
                monsterData.put(type, new CreatureData(type, prefix, config));
        }
        catch(Exception error){
            OutputHandler.PrintError("Failed to load entity : " + OutputHandler.HIGHLIGHT + key);
            error.printStackTrace();
        }
    }
}
