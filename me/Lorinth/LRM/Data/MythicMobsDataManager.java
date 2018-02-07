package me.Lorinth.LRM.Data;

import me.Lorinth.LRM.Listener.MythicMobsListener;
import me.Lorinth.LRM.Objects.DataManager;
import me.Lorinth.LRM.Objects.Disableable;
import me.Lorinth.LRM.Util.OutputHandler;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public class MythicMobsDataManager extends Disableable implements DataManager{

    private ArrayList<String> ignoredWorlds = new ArrayList<>();
    private MythicMobsListener mythicMobsListener;

    public void loadData(FileConfiguration config, Plugin plugin){
        if(!config.getKeys(false).contains("MythicMobs") ||
                !config.getConfigurationSection("MythicMobs").getKeys(false).contains("Enabled")){
            OutputHandler.PrintInfo("MythicMobs options not found, Generating...");
            setDefaults(config, plugin);
        }

        if(Bukkit.getServer().getPluginManager().getPlugin("MythicMobs") == null)
            this.setDisabled(true);
        else
            this.setDisabled(!config.getBoolean("MythicMobs.Enabled"));


        if(this.isDisabled())
            OutputHandler.PrintInfo("MythicMobs Integration is Disabled");
        else {
            OutputHandler.PrintInfo("MythicMobs Integration is Enabled!");
            loadIgnoredWorlds(config);

            mythicMobsListener = new MythicMobsListener(this);
            Bukkit.getPluginManager().registerEvents(mythicMobsListener, plugin);
        }
    }

    private void loadIgnoredWorlds(FileConfiguration config){
        if(config.getConfigurationSection("MythicMobs").getKeys(false).contains("IgnoredWorlds"))
            ignoredWorlds.addAll(config.getStringList("MythicMobs.IgnoredWorlds"));
    }

    public boolean isWorldIgnored(World world){
        return ignoredWorlds.contains(world.getName());
    }

    public boolean isMythicMob(Entity entity){
        if(!isDisabled())
            return mythicMobsListener.isMythicMob(entity);
        return false;
    }

    public boolean saveData(FileConfiguration config){
        return false;
    }

    private void setDefaults(FileConfiguration config, Plugin plugin){
        config.set("MythicMobs.Enabled", false);
        config.set("MythicMobs.IgnoredWorlds", new ArrayList<String>(){{ add("world_the_end"); }});
        plugin.saveConfig();
    }
}
