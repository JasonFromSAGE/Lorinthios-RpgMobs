package me.Lorinth.LRM.Data;

import me.Lorinth.LRM.Objects.*;
import me.Lorinth.LRM.Util.OutputHandler;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class shouldn't be used outside of the plugin
 */
public class DataLoader implements DataManager{

    private NameOptions nameOptions;
    protected DistanceAlgorithm distanceAlgorithm = DistanceAlgorithm.Diamond;

    private CreatureDataManager creatureDataManager = new CreatureDataManager();
    private HeroesDataManager heroesDataManager = new HeroesDataManager();
    private LevelRegionManager levelRegionManager = new LevelRegionManager();
    private SkillAPIDataManager skillAPIDataManager = new SkillAPIDataManager();
    private MythicMobsDataManager mythicMobsDataManager = new MythicMobsDataManager();
    private SpawnPointManager spawnPointManager = new SpawnPointManager(this);

    private HashMap<String, ArrayList<LevelRegion>> allLevelRegions = new HashMap<>(); // String: World Name, List<LevelRegion> list of regions

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

    public CreatureDataManager getCreatureDataManager(){
        return creatureDataManager;
    }

    public HeroesDataManager getHeroesDataManager(){
        return heroesDataManager;
    }

    public LevelRegionManager getLevelRegionManager(){
        return levelRegionManager;
    }

    public MythicMobsDataManager getMythicMobsDataManager() { return mythicMobsDataManager; }

    public SkillAPIDataManager getSkillAPIDataManager(){ return skillAPIDataManager; }

    public SpawnPointManager getSpawnPointManager(){
        return spawnPointManager;
    }

    public boolean saveData(FileConfiguration config){
        return spawnPointManager.saveData(config) || levelRegionManager.saveData(config) || creatureDataManager.saveData(config) ||
                heroesDataManager.saveData(config) || skillAPIDataManager.saveData(config) || mythicMobsDataManager.saveData(config);
    }

    public void loadData(FileConfiguration config, Plugin plugin){
        loadNameOptions(config, plugin);
        loadDistanceAlgorithm(config, plugin);
        creatureDataManager.loadData(config, plugin);
        heroesDataManager.loadData(config, plugin);
        levelRegionManager.loadData(config, plugin);
        mythicMobsDataManager.loadData(config, plugin);
        skillAPIDataManager.loadData(config, plugin);
        spawnPointManager.loadData(config, plugin);
    }

    private void loadNameOptions(FileConfiguration config, Plugin plugin){
        nameOptions = new NameOptions();
        nameOptions.loadData(config, plugin);
    }

    private void loadDistanceAlgorithm(FileConfiguration config, Plugin plugin){
        String algo = config.getString("DistanceAlgorithm");
        boolean changed = false;

        if(algo.equalsIgnoreCase("Optimized")){
            algo = "Diamond";
            changed = true;
        }

        if(algo.equalsIgnoreCase("Accurate")){
            algo = "Circle";
            changed = true;
        }

        if(changed){
            config.set("DistanceAlgorithm", algo);
            plugin.saveConfig();
        }


        try{
            distanceAlgorithm = DistanceAlgorithm.valueOf(algo);
        }
        catch(Exception error){
            OutputHandler.PrintError("Distance Algorithm : " + OutputHandler.HIGHLIGHT + algo + OutputHandler.ERROR + " is not a valid Algorithm " + OutputHandler.HIGHLIGHT + "(Accurate/Optimized)");
            distanceAlgorithm = DistanceAlgorithm.Diamond;
        }
    }

}
