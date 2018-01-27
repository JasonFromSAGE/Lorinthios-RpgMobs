package me.Lorinth.LRM;

import me.Lorinth.LRM.Command.CommandConstants;
import me.Lorinth.LRM.Command.MainExecutor;
import me.Lorinth.LRM.Data.CreatureDataManager;
import me.Lorinth.LRM.Data.DataLoader;
import me.Lorinth.LRM.Data.SpawnPointManager;
import me.Lorinth.LRM.Objects.CreatureData;
import me.Lorinth.LRM.Objects.OutputHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Created by lorinthio on 1/24/2018.
 */
public class LorinthsRpgMobs extends JavaPlugin{

    private static DataLoader dataLoader;
    public static LorinthsRpgMobs instance;

    @Override
    public void onEnable(){
        OutputHandler.PrintMessage("Enabling...");
        loadConfiguration();
        registerCommands();

        dataLoader = new DataLoader(getConfig());
        Bukkit.getPluginManager().registerEvents(new CreatureEventListener(dataLoader), this);
        OutputHandler.PrintMessage("Finished!");

        instance = this;
    }

    @Override
    public void onDisable(){
        OutputHandler.PrintMessage("Disabling...");

        if(dataLoader != null){
            //Load possible changes in the file from user
            reloadConfig();
            //Apply the changes we gained during the session
            dataLoader.saveDirtyObjects(getConfig());
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

    //API Methods
    /**
     * Get level of a given creature
     * @param creature - creature you want to check
     * @return - level of creature
     */
    public static int GetLevelOfCreature(Creature creature){
        if(creature.hasMetadata("Level"))
            if(creature.getMetadata("Level").size() > 0)
                return creature.getMetadata("Level").get(0).asInt();

        return 1;
    }

    /**
     * Get the level of a specific location
     * @param location - location to check
     * @return - level at location
     */
    public static int GetLevelAtLocation(Location location){
        return dataLoader.calculateLevel(location);
    }

    /**
     * Get the spawn point manager which contains data for all spawn points which you can read/write to
     * @return
     */
    public static SpawnPointManager GetSpawnPointManager(){
        return dataLoader.getSpawnPointManager();
    }

    /**
     * Get the creature data manager which contains data for all entities which you can read/write to
     * @return
     */
    public static CreatureDataManager GetCreatureDataManager(){
        return dataLoader.getCreatureDataManager();
    }
}
