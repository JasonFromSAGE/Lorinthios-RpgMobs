package me.Lorinth.LRM;

import me.Lorinth.LRM.Objects.CreatureData;
import me.Lorinth.LRM.Objects.DistanceAlgorithm;
import me.Lorinth.LRM.Objects.LevelRegion;
import me.Lorinth.LRM.Objects.SpawnPoint;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by lorinthio on 1/24/2018.
 */
public class DataLoader {

    private DistanceAlgorithm distanceAlgorithm = DistanceAlgorithm.Optimized;
    private HashMap<String, ArrayList<SpawnPoint>> allSpawnPoints = new HashMap<>(); // String: World Name, List
    private HashMap<String, ArrayList<LevelRegion>> allLevelRegions = new HashMap<>(); // String: World Name, List<LevelRegion> list of regions
    private HashMap<EntityType, CreatureData> animalData = new HashMap<>();
    private HashMap<EntityType, CreatureData> monsterData = new HashMap<>();

    public DataLoader(FileConfiguration config){
        load(config);
    }

    public int calculateLevel(Location location){
        if(allSpawnPoints.containsKey(location.getWorld().getName())){
            ArrayList<SpawnPoint> spawnPoints = allSpawnPoints.get(location.getWorld().getName());

            SpawnPoint closestSpawnPoint = null;
            if(spawnPoints.size() == 1) {
                closestSpawnPoint = spawnPoints.get(0);
            }
            else if(spawnPoints.size() > 1){
                int shortestDistance = Integer.MAX_VALUE;

                for(SpawnPoint spawnPoint : spawnPoints){
                    int currentDistance = spawnPoint.calculateDistance(location, distanceAlgorithm);
                    if(currentDistance < shortestDistance){
                        closestSpawnPoint = spawnPoint;
                        shortestDistance = currentDistance;
                    }
                }
            }

            if(closestSpawnPoint != null)
                return closestSpawnPoint.calculateLevel(location, distanceAlgorithm);
        }

        return -1; //this world is disabled if -1
    }

    public CreatureData getData(Creature creature){
        HashMap<EntityType, CreatureData> creatureData = null;
        if(creature instanceof Monster)
            creatureData = monsterData;
        else
            creatureData = animalData;

        if(creatureData.containsKey(creature.getType())){
            return creatureData.get(creature.getType());
        }
        else{
            CreatureData newData = new CreatureData(creature);
            creatureData.put(creature.getType(), newData);
            return newData;
        }
    }

    public void SaveDirtyObjects(FileConfiguration config){
        saveSpawnPoints(config);
        saveLevelRegions(config);
        saveCreatureData(config);
    }

    private void load(FileConfiguration config){
        loadDistanceAlgorithm(config);
        loadSpawnPoints(config);
        loadCreatureData(config);
    }

    private void loadDistanceAlgorithm(FileConfiguration config){
        String algo = config.getString("DistanceAlgorithm");
        try{
            distanceAlgorithm = DistanceAlgorithm.valueOf(algo);
        }
        catch(Exception error){
            LorinthsRpgMobs.PrintMessage(LorinthsRpgMobs.Error + "Distance Algorithm : " + LorinthsRpgMobs.Highlight + algo + LorinthsRpgMobs.Error + " is not a valid Algorithm " + LorinthsRpgMobs.Highlight + "(Accurate/Optimized)");
            distanceAlgorithm = DistanceAlgorithm.Optimized;
        }
    }

    private void loadSpawnPoints(FileConfiguration config){
        Set<String> worldNames = config.getConfigurationSection("SpawnPoints").getKeys(false);
        for(World world : Bukkit.getServer().getWorlds()){
            String worldName = world.getName();
            if(worldNames.contains(worldName)){
                for(String key : config.getConfigurationSection("SpawnPoints." + worldName).getKeys(false)){
                    loadSpawnPoint(config, worldName, "SpawnPoints." + worldName + "." + key);
                }
            }
        }
    }

    private void loadSpawnPoint(FileConfiguration config, String worldName, String prefix){
        if(!allSpawnPoints.containsKey(worldName)){
            allSpawnPoints.put(worldName, new ArrayList<>());
        }

        allSpawnPoints.get(worldName).add(new SpawnPoint(config, prefix));
    }

    private void loadCreatureData(FileConfiguration config){
        loadCreatureSection(config, "Entity.Animal.");
        loadCreatureSection(config, "Entity.Monster.");

    }

    private void loadCreatureSection(FileConfiguration config, String prefix) {
        for(String key : config.getConfigurationSection(prefix).getKeys(false)){
            if(!key.equalsIgnoreCase("disabled"))
                loadEntity(config, prefix, key);
        }
    }

    private void loadEntity(FileConfiguration config, String prefix, String key){
        try{
            EntityType type = EntityType.fromName(key);
            if(type == null) {
                LorinthsRpgMobs.PrintMessage(LorinthsRpgMobs.Error + "Failed to find entity type for, " + LorinthsRpgMobs.Highlight + key);
                return;
            }

            if(prefix.contains("Animal"))
                animalData.put(type, new CreatureData(type, prefix, config));
            else
                monsterData.put(type, new CreatureData(type, prefix, config));
        }
        catch(Exception error){
            LorinthsRpgMobs.PrintMessage(LorinthsRpgMobs.Error + "Failed to load entity : " + LorinthsRpgMobs.Highlight + key);
            error.printStackTrace();
        }
    }

    private void saveSpawnPoints(FileConfiguration config){
        for(String key : allSpawnPoints.keySet()){
            for(SpawnPoint spawnPoint : allSpawnPoints.get(key)){
                spawnPoint.Save(config, "SpawnPoints." + key + ".");
            }
        }
    }

    private void saveLevelRegions(FileConfiguration config){
        for(String key : allLevelRegions.keySet()){
            for(LevelRegion region : allLevelRegions.get(key)){
                region.Save(config, "LevelRegions." + key + ".");
            }
        }
    }

    private void saveCreatureData(FileConfiguration config){
        for(CreatureData data : animalData.values()){
            data.Save(config, "Entity.Animal.");
        }
        for(CreatureData data : monsterData.values()){
            data.Save(config, "Entity.Monster.");
        }
    }
}
