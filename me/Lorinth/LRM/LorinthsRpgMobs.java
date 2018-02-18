package me.Lorinth.LRM;

import me.Lorinth.LRM.Command.CommandConstants;
import me.Lorinth.LRM.Command.MainExecutor;
import me.Lorinth.LRM.Data.*;
import me.Lorinth.LRM.Listener.CreatureEventListener;
import me.Lorinth.LRM.Listener.UpdaterEventListener;
import me.Lorinth.LRM.Objects.Properties;
import me.Lorinth.LRM.Util.MetaDataConstants;
import me.Lorinth.LRM.Util.OutputHandler;
import me.Lorinth.LRM.Variants.MobVariant;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Main class of LorinthsRpgMobs contains main API Methods
 */
public class LorinthsRpgMobs extends JavaPlugin{

    protected static Updater updater;
    private static DataLoader dataLoader;
    public static LorinthsRpgMobs instance;
    public static Properties properties;

    @Override
    public void onEnable(){
        instance = this;
        OutputHandler.PrintInfo("Enabling v" + getDescription().getVersion() + "...");
        loadConfiguration();
        registerCommands();
        checkAutoUpdates();

        dataLoader = new DataLoader();
        dataLoader.loadData(getConfig(), this);
        Bukkit.getPluginManager().registerEvents(new CreatureEventListener(dataLoader), this);
        Bukkit.getPluginManager().registerEvents(new UpdaterEventListener(updater), this);
        OutputHandler.PrintInfo("Finished!");
    }

    @Override
    public void onDisable(){
        OutputHandler.PrintInfo("Disabling...");

        if(dataLoader != null){
            //Load possible changes in the file from user
            reloadConfig();
            //Apply the changes we gained during the session
            dataLoader.saveData(getConfig());
            saveConfig();
        }
    }

    public static void Reload(){
        instance.onDisable();
        instance.onEnable();
    }

    private void loadConfiguration(){
        if(!new File(getDataFolder(), "config.yml").exists()){
            saveDefaultConfig();
        }
    }

    private void registerCommands(){
        getCommand(CommandConstants.LorinthsRpgMobsCommand).setExecutor(new MainExecutor());
    }

    private void checkAutoUpdates(){
        if(getConfig().getKeys(false).contains("AllowAutoUpdates")){
            if(getConfig().getBoolean("AllowAutoUpdates"))
                updater = new Updater(this, getFile(), Updater.UpdateType.DEFAULT);
            else
                updater = new Updater(this, getFile(), Updater.UpdateType.NO_DOWNLOAD);
        }
        else{
            getConfig().set("AllowAutoUpdates", false);
            saveConfig();
        }
    }

    public static Updater ForceUpdate(Updater.UpdateCallback callback){
        return new Updater(instance, instance.getFile(), Updater.UpdateType.DEFAULT, callback);
    }

    //API Methods
    /**
     * Get level of a given creature
     * @param creature creature you want to check
     * @return level of creature, null if no level
     */
    public static Integer GetLevelOfCreature(Creature creature){
        return GetLevelOfEntity(creature);
    }

    /**
     * Get level of entity
     * @param entity the entity you want to check
     * @return null if no level, otherwise returns level of entity
     */
    public static Integer GetLevelOfEntity(Entity entity){
        if(entity instanceof LivingEntity){
            if(entity.hasMetadata(MetaDataConstants.Level))
                if(entity.getMetadata(MetaDataConstants.Level).size() > 0)
                    return entity.getMetadata(MetaDataConstants.Level).get(0).asInt();
        }
        return null;
    }

    /**
     * Returns the variant of the entity. Such as Burning, Poisonous, Fast, Slow, etc
     * @param entity
     * @return - The variant of the entity, null if no variant applied
     */
    public static MobVariant GetMobVariantOfEntity(Entity entity){
        if(entity.hasMetadata(MetaDataConstants.Variant)){
            return (MobVariant) entity.getMetadata(MetaDataConstants.Variant).get(0).value();
        }

        return null;
    }

    /**
     * Get the level of a specific location
     * @param location location to check
     * @return level at location
     */
    public static int GetLevelAtLocation(Location location){
        return dataLoader.calculateLevel(location);
    }

    /**
     * Get the spawn point manager which contains data for all spawn points which you can read/write to
     * @return SpawnPointManager
     */
    public static SpawnPointManager GetSpawnPointManager(){
        return dataLoader.getSpawnPointManager();
    }

    public static MobVariantDataManager GetMobVariantManager(){ return dataLoader.getMobVariantManager(); }

    public static MythicMobsDataManager GetMythicMobsDataManager(){
        return dataLoader.getMythicMobsDataManager();
    }

    /**
     * Get the level region manager which contains data for all regions with level settings
     * @return LevelRegionManager
     */
    public static LevelRegionManager GetLevelRegionManager(){
        return dataLoader.getLevelRegionManager();
    }

    /**
     * Get the creature data manager which contains data for all entities which you can read/write to
     * @return CreatureDataManager
     */
    public static CreatureDataManager GetCreatureDataManager(){
        return dataLoader.getCreatureDataManager();
    }

    public static boolean IsMythicMob(Entity entity){
        return dataLoader.getMythicMobsDataManager().isMythicMob(entity);
    }
}
