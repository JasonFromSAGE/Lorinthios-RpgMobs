package me.Lorinth.LRM.Data;

import me.Lorinth.LRM.LorinthsRpgMobs;
import me.Lorinth.LRM.Objects.*;
import me.Lorinth.LRM.Util.ConfigHelper;
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

    protected DistanceAlgorithm distanceAlgorithm = DistanceAlgorithm.Diamond;

    private CreatureDataManager creatureDataManager = new CreatureDataManager();
    private HeroesDataManager heroesDataManager = new HeroesDataManager();
    private LevelRegionManager levelRegionManager = new LevelRegionManager();
    private MobVariantDataManager mobVariantDataManager = new MobVariantDataManager();
    private MythicMobsDataManager mythicMobsDataManager = new MythicMobsDataManager();
    private SkillAPIDataManager skillAPIDataManager = new SkillAPIDataManager();
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

    public CreatureDataManager getCreatureDataManager(){
        return creatureDataManager;
    }

    public HeroesDataManager getHeroesDataManager(){
        return heroesDataManager;
    }

    public LevelRegionManager getLevelRegionManager(){
        return levelRegionManager;
    }

    public MobVariantDataManager getMobVariantManager(){ return mobVariantDataManager; }

    public MythicMobsDataManager getMythicMobsDataManager() { return mythicMobsDataManager; }

    public SkillAPIDataManager getSkillAPIDataManager(){ return skillAPIDataManager; }

    public SpawnPointManager getSpawnPointManager(){
        return spawnPointManager;
    }

    public boolean saveData(FileConfiguration config){
        return spawnPointManager.saveData(config) || levelRegionManager.saveData(config) || creatureDataManager.saveData(config) ||
                heroesDataManager.saveData(config) || skillAPIDataManager.saveData(config) || mythicMobsDataManager.saveData(config) ||
                mobVariantDataManager.saveData(config);
    }

    public void loadData(FileConfiguration config, Plugin plugin){
        loadGlobalOptions(config, plugin);
        creatureDataManager.loadData(config, plugin);
        heroesDataManager.loadData(config, plugin);
        levelRegionManager.loadData(config, plugin);
        mobVariantDataManager.loadData(config, plugin);
        mythicMobsDataManager.loadData(config, plugin);
        skillAPIDataManager.loadData(config, plugin);
        spawnPointManager.loadData(config, plugin);
    }

    private void loadGlobalOptions(FileConfiguration config, Plugin plugin){
        LorinthsRpgMobs.properties.NameTagsAlwaysOn = config.getBoolean("Names.TagsAlwaysOn");
        LorinthsRpgMobs.properties.NameFormat = config.getString("Names.Format").replaceAll("&", "§");
        loadDistanceAlgorithm(config, plugin);

        if(ConfigHelper.ConfigContainsPath(config, "VanillaMobEquipmentOverrides")){
            LorinthsRpgMobs.properties.VanillaMobEquipmentOverrides = config.getBoolean("VanillaMobEquipmentOverrides");
        }
        else{

        }
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
