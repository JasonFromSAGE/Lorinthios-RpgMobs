package me.Lorinth.LRM.Data;

import me.Lorinth.LRM.LorinthsRpgMobs;
import me.Lorinth.LRM.Objects.*;
import me.Lorinth.LRM.Util.OutputHandler;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

/**
 * This class shouldn't be used outside of the plugin
 */
public class DataLoader implements DataManager{

    protected DistanceAlgorithm distanceAlgorithm = DistanceAlgorithm.Diamond;

    private CreatureDataManager creatureDataManager = new CreatureDataManager();
    private ExperiencePermissionManager experiencePermissionManager = new ExperiencePermissionManager();
    private HeroesDataManager heroesDataManager = new HeroesDataManager();
    private LevelRegionManager levelRegionManager = new LevelRegionManager();
    private MobVariantDataManager mobVariantDataManager = new MobVariantDataManager();
    private MythicDropsDataManager mythicDropsDataManager = new MythicDropsDataManager(this);
    private MythicMobsDataManager mythicMobsDataManager = new MythicMobsDataManager();
    private SkillAPIDataManager skillAPIDataManager = new SkillAPIDataManager();
    private SpawnPointManager spawnPointManager = new SpawnPointManager(this);

    public int calculateLevel(Location location, Entity entity){
        LevelRegion region = levelRegionManager.getHighestPriorityLeveledRegionAtLocation(location);
        if(region != null && entity != null)
            if(region.entityIsDisabled(entity))
                return -1;

        if(region != null && region.getLevel() != -1)
            return region.getLevel();
        else{
            SpawnPoint closestSpawnPoint = spawnPointManager.getSpawnPointForLocation(location);
            if(closestSpawnPoint != null) {
                return closestSpawnPoint.calculateLevel(location, distanceAlgorithm);
            }
        }

        return -1; //this world is disabled if -1
    }

    public CreatureDataManager getCreatureDataManager(){
        return creatureDataManager;
    }

    public ExperiencePermissionManager getExperiencePermissionManager(){ return experiencePermissionManager; }

    public HeroesDataManager getHeroesDataManager(){
        return heroesDataManager;
    }

    public LevelRegionManager getLevelRegionManager(){
        return levelRegionManager;
    }

    public MobVariantDataManager getMobVariantManager(){ return mobVariantDataManager; }

    public MythicDropsDataManager getMythicDropsDataManager(){ return mythicDropsDataManager; }

    public MythicMobsDataManager getMythicMobsDataManager() { return mythicMobsDataManager; }

    public SkillAPIDataManager getSkillAPIDataManager(){ return skillAPIDataManager; }

    public SpawnPointManager getSpawnPointManager(){
        return spawnPointManager;
    }

    public boolean saveData(FileConfiguration config){
        return spawnPointManager.saveData(config) || levelRegionManager.saveData(config) || creatureDataManager.saveData(config) ||
                heroesDataManager.saveData(config) || skillAPIDataManager.saveData(config) || mythicDropsDataManager.saveData(config) ||
                mythicMobsDataManager.saveData(config) || mobVariantDataManager.saveData(config) || experiencePermissionManager.saveData(config);
    }

    public void loadData(FileConfiguration config, Plugin plugin){
        loadGlobalOptions(config, plugin);
        creatureDataManager.loadData(config, plugin);
        experiencePermissionManager.loadData(config, plugin);
        heroesDataManager.loadData(config, plugin);
        levelRegionManager.loadData(config, plugin);
        mobVariantDataManager.loadData(config, plugin);
        mythicDropsDataManager.loadData(config, plugin);
        mythicMobsDataManager.loadData(config, plugin);
        skillAPIDataManager.loadData(config, plugin);
        spawnPointManager.loadData(config, plugin);
    }

    private void loadGlobalOptions(FileConfiguration config, Plugin plugin){
        LorinthsRpgMobs.properties.NameTagsAlwaysOn = config.getBoolean("Names.TagsAlwaysOn");
        LorinthsRpgMobs.properties.NameFormat = config.getString("Names.Format").replaceAll("&", "§");
        loadDistanceAlgorithm(config, plugin);
        loadVanillaEquipmentOverride(config, plugin);
    }

    private void loadDistanceAlgorithm(FileConfiguration config, Plugin plugin){
        String algo = config.getString("DistanceAlgorithm");
        try{
            distanceAlgorithm = DistanceAlgorithm.valueOf(algo);
        }
        catch(Exception error){
            OutputHandler.PrintError("Distance Algorithm : " + OutputHandler.HIGHLIGHT + algo + OutputHandler.ERROR + " is not a valid Algorithm " + OutputHandler.HIGHLIGHT + "(Circle/Diamond/Square)");
            OutputHandler.PrintError("Using " + OutputHandler.HIGHLIGHT + "Diamond" + OutputHandler.ERROR + " Distance Algorithm");
            distanceAlgorithm = DistanceAlgorithm.Diamond;
        }
    }

    private void loadVanillaEquipmentOverride(FileConfiguration config, Plugin plugin){
        LorinthsRpgMobs.properties.VanillaMobEquipmentOverrides = config.getBoolean("VanillaMobEquipmentOverrides");
    }

}
