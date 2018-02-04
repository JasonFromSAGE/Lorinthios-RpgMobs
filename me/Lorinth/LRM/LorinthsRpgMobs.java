package me.Lorinth.LRM;

import me.Lorinth.LRM.Command.CommandConstants;
import me.Lorinth.LRM.Command.MainExecutor;
import me.Lorinth.LRM.Data.CreatureDataManager;
import me.Lorinth.LRM.Data.DataLoader;
import me.Lorinth.LRM.Data.LevelRegionManager;
import me.Lorinth.LRM.Data.SpawnPointManager;
import me.Lorinth.LRM.Listener.CreatureEventListener;
import me.Lorinth.LRM.Objects.OutputHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Main class of LorinthsRpgMobs contains main API Methods
 */
public class LorinthsRpgMobs extends JavaPlugin{

    protected static Updater updater;
    private static DataLoader dataLoader;
    public static LorinthsRpgMobs instance;

    @Override
    public void onEnable(){
        OutputHandler.PrintInfo("Enabling v" + getDescription().getVersion() + "...");
        loadConfiguration();
        registerCommands();
        checkAutoUpdates();

        dataLoader = new DataLoader();
        dataLoader.loadData(getConfig(), this);
        Bukkit.getPluginManager().registerEvents(new CreatureEventListener(dataLoader), this);
        Bukkit.getPluginManager().registerEvents(new UpdaterEventListener(updater), this);
        OutputHandler.PrintInfo("Finished!");

        instance = this;
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
     * @return level of creature
     */
    public static int GetLevelOfCreature(Creature creature){
        if(creature.hasMetadata("Level"))
            if(creature.getMetadata("Level").size() > 0)
                return creature.getMetadata("Level").get(0).asInt();

        return 1;
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
}
