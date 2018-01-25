package me.Lorinth.LRM;

import me.Lorinth.LRM.Objects.ConsoleOutput;
import me.Lorinth.LRM.Objects.SpawnPoint;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Creature;
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
        ConsoleOutput.PrintMessage("Enabling...");
        loadConfiguration();

        dataLoader = new DataLoader(getConfig());
        Bukkit.getPluginManager().registerEvents(new CreatureEventListener(dataLoader), this);
        ConsoleOutput.PrintMessage("Finished!");

        instance = this;
    }

    @Override
    public void onDisable(){
        ConsoleOutput.PrintMessage("Disabling...");

        //Load possible changes in the file from user
        reloadConfig();
        //Apply the changes we gained during the session
        dataLoader.SaveDirtyObjects(getConfig());
        saveConfig();
    }

    private void loadConfiguration(){
        if(!new File(getDataFolder(), "config.yml").exists()){
            saveDefaultConfig();
        }
    }

    //API Methods
    /**
     * Get level of a given creature
     * @param creature - creature you want to check
     * @return - level of creature
     */
    public static int GetLevelOfCreature(Creature creature){
        return creature.getMetadata("Level").get(0).asInt();
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
     * Finds the responsible spawn point for a location
     * @param location - location you want to check
     * @return - Spawn point that would be used (null if world is disabled)
     */
    public static SpawnPoint GetSpawnPointForLocation(Location location){
        return dataLoader.getSpawnPointForLocation(location);
    }

}
