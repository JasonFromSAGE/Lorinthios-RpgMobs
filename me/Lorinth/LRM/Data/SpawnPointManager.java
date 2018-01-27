package me.Lorinth.LRM.Data;

import me.Lorinth.LRM.Objects.SpawnPoint;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by lorinthio on 1/27/2018.
 */
public class SpawnPointManager {

    private HashMap<String, ArrayList<SpawnPoint>> allSpawnPoints = new HashMap<>(); // String: World Name, List
    private DataLoader dataLoader;

    public SpawnPointManager(DataLoader loader){
        dataLoader = loader;
    }

    public SpawnPoint getSpawnPointForLocation(Location location){
        if(allSpawnPoints.containsKey(location.getWorld().getName())){
            ArrayList<SpawnPoint> spawnPoints = allSpawnPoints.get(location.getWorld().getName());

            SpawnPoint closestSpawnPoint = null;
            if(spawnPoints.size() == 1) {
                closestSpawnPoint = spawnPoints.get(0);
            }
            else if(spawnPoints.size() > 1){
                int shortestDistance = Integer.MAX_VALUE;

                for(SpawnPoint spawnPoint : spawnPoints){
                    int currentDistance = spawnPoint.calculateDistance(location, dataLoader.distanceAlgorithm);
                    if(currentDistance < shortestDistance){
                        closestSpawnPoint = spawnPoint;
                        shortestDistance = currentDistance;
                    }
                }
            }

            return closestSpawnPoint;
        }
        return null;
    }

    public SpawnPoint getSpawnPointInWorldByName(World world, String name){
        for(SpawnPoint spawn : allSpawnPoints.get(world.getName())){
            if(spawn.getName().equalsIgnoreCase(name))
                return spawn;
        }
        return null;
    }

    //Loading & Saving
    protected void loadSpawnPoints(FileConfiguration config){
        Set<String> worldNames = config.getConfigurationSection("SpawnPoints").getKeys(false);
        for(String worldName : worldNames){
            Set<String> spawnPointsInWorld = config.getConfigurationSection("SpawnPoints." + worldName).getKeys(false);
            for(String spawnPointName : spawnPointsInWorld){
                loadSpawnPoint(config, worldName, spawnPointName, "SpawnPoints." + worldName + "." + spawnPointName);
            }
        }
    }

    private void loadSpawnPoint(FileConfiguration config, String worldName, String spawnPointName, String prefix){
        if(!allSpawnPoints.containsKey(worldName)){
            allSpawnPoints.put(worldName, new ArrayList<>());
        }

        SpawnPoint spawnPoint = new SpawnPoint(config, worldName, spawnPointName, prefix);
        if(!spawnPoint.isDisabled())
            allSpawnPoints.get(worldName).add(spawnPoint);
    }

    protected boolean saveSpawnPoints(FileConfiguration config){
        boolean changed = false;
        for(String key : allSpawnPoints.keySet()){
            for(SpawnPoint spawnPoint : allSpawnPoints.get(key)){
                if(spawnPoint.save(config, "SpawnPoints." + key + "."))
                    changed = true;
            }
        }
        return changed;
    }

}
