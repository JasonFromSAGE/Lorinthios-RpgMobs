package me.Lorinth.LRM.Data;

import me.Lorinth.LRM.Objects.*;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * This class shouldn't be used outside of the plugin
 */
public class DataLoader {

    private NameOptions nameOptions;
    protected DistanceAlgorithm distanceAlgorithm = DistanceAlgorithm.Optimized;

    private CreatureDataManager creatureDataManager = new CreatureDataManager();
    private LevelRegionManager levelRegionManager = new LevelRegionManager();
    private SpawnPointManager spawnPointManager = new SpawnPointManager(this);

    private HashMap<String, ArrayList<LevelRegion>> allLevelRegions = new HashMap<>(); // String: World Name, List<LevelRegion> list of regions

    public DataLoader(FileConfiguration config){
        load(config);
    }

    public int calculateLevel(Location location){
        int strictLevel = levelRegionManager.getHighestPriorityLevelAtLocation(location);
        boolean strictLevelIsValid = strictLevel != -1;

        SpawnPoint closestSpawnPoint = spawnPointManager.getSpawnPointForLocation(location);
        if(closestSpawnPoint != null) {
            if(strictLevelIsValid)
                return strictLevel; //Regions set the level of mobs
            else
                return closestSpawnPoint.calculateLevel(location, distanceAlgorithm);
        }
        return -1; //this world is disabled if -1
    }

    public NameOptions getNameOptions(){
        return nameOptions;
    }

    public boolean saveDirtyObjects(FileConfiguration config){
        return spawnPointManager.saveSpawnPoints(config) || levelRegionManager.saveData(config) || creatureDataManager.saveCreatureData(config);
    }

    public SpawnPointManager getSpawnPointManager(){
        return spawnPointManager;
    }

    public CreatureDataManager getCreatureDataManager(){
        return creatureDataManager;
    }

    public LevelRegionManager getLevelRegionManager(){
        return levelRegionManager;
    }

    private void load(FileConfiguration config){
        loadNameOptions(config);
        loadDistanceAlgorithm(config);
        spawnPointManager.loadSpawnPoints(config);
        creatureDataManager.loadCreatureData(config);
        levelRegionManager.loadAllRegions(config);
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

}
