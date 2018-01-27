package me.Lorinth.LRM.Data;

import me.Lorinth.LRM.Objects.*;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by lorinthio on 1/24/2018.
 */
public class DataLoader {

    private NameOptions nameOptions;
    protected DistanceAlgorithm distanceAlgorithm = DistanceAlgorithm.Optimized;
    private SpawnPointManager spawnPointManager = new SpawnPointManager(this);
    private CreatureDataManager creatureDataManager = new CreatureDataManager(this);

    private HashMap<String, ArrayList<LevelRegion>> allLevelRegions = new HashMap<>(); // String: World Name, List<LevelRegion> list of regions

    public DataLoader(FileConfiguration config){
        load(config);
    }

    public int calculateLevel(Location location){
        SpawnPoint closestSpawnPoint = spawnPointManager.getSpawnPointForLocation(location);
        if(closestSpawnPoint != null)
            return closestSpawnPoint.calculateLevel(location, distanceAlgorithm);
        return -1; //this world is disabled if -1
    }

    public NameOptions getNameOptions(){
        return nameOptions;
    }

    public boolean saveDirtyObjects(FileConfiguration config){
        return spawnPointManager.saveSpawnPoints(config) || saveLevelRegions(config) || creatureDataManager.saveCreatureData(config);
    }

    public SpawnPointManager getSpawnPointManager(){
        return spawnPointManager;
    }

    public CreatureDataManager getCreatureDataManager(){
        return creatureDataManager;
    }

    private void load(FileConfiguration config){
        loadNameOptions(config);
        loadDistanceAlgorithm(config);
        spawnPointManager.loadSpawnPoints(config);
        creatureDataManager.loadCreatureData(config);
    }

    private void loadNameOptions(FileConfiguration config){
        nameOptions = new NameOptions(config);
    }

    private void loadDistanceAlgorithm(FileConfiguration config){
        String algo = config.getString("DistanceAlgorithm");
        try{
            distanceAlgorithm = DistanceAlgorithm.valueOf(algo);
        }
        catch(Exception error){
            OutputHandler.PrintError("Distance Algorithm : " + OutputHandler.HIGHLIGHT + algo + OutputHandler.ERROR + " is not a valid Algorithm " + OutputHandler.HIGHLIGHT + "(Accurate/Optimized)");
            distanceAlgorithm = DistanceAlgorithm.Optimized;
        }
    }

    private boolean saveLevelRegions(FileConfiguration config){
        boolean changed = false;
        for(String key : allLevelRegions.keySet()){
            for(LevelRegion region : allLevelRegions.get(key)){
                if(region.save(config, "LevelRegions." + key + "."))
                    changed = true;
            }
        }
        return changed;
    }

}
