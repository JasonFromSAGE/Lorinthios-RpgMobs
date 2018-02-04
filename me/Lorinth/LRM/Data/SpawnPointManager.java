package me.Lorinth.LRM.Data;

import me.Lorinth.LRM.Objects.DataManager;
import me.Lorinth.LRM.Objects.SpawnPoint;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Manager object of all Spawn Points in memomry. Get spawn points by location or by world/name
 */
public class SpawnPointManager implements DataManager{

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
                    if(!spawnPoint.isDisabled()){
                        int currentDistance = spawnPoint.calculateDistance(location, dataLoader.distanceAlgorithm);
                        if(currentDistance < shortestDistance){
                            closestSpawnPoint = spawnPoint;
                            shortestDistance = currentDistance;
                        }
                    }
                }
            }

            return closestSpawnPoint;
        }
        return null;
    }

    public SpawnPoint getSpawnPointInWorldByName(World world, String name){
        for(SpawnPoint spawn : getAllSpawnPointsInWorld(world)){
            if(spawn.getName().equalsIgnoreCase(name))
                return spawn;
        }
        return null;
    }

    public void addSpawnPointInWorld(World world, SpawnPoint spawnPoint){
        if(!allSpawnPoints.containsKey(world.getName())){
            allSpawnPoints.put(world.getName(), new ArrayList<SpawnPoint>());
        }

        allSpawnPoints.get(world.getName()).add(spawnPoint);
    }

    public ArrayList<SpawnPoint> getAllSpawnPointsInWorld(World world){
        ArrayList<SpawnPoint> spawnPointsInWorld = new ArrayList<SpawnPoint>();
        if(allSpawnPoints.containsKey(world.getName()))
            spawnPointsInWorld = allSpawnPoints.get(world.getName());

        return spawnPointsInWorld;
    }

    //Loading & Saving
    public void loadData(FileConfiguration config, Plugin plugin){
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

    public boolean saveData(FileConfiguration config){
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
