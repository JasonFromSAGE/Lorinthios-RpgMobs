package me.Lorinth.LRM.Data;

import me.Lorinth.LRM.Listener.SkillAPIListener;
import me.Lorinth.LRM.Objects.CreatureDeathData;
import me.Lorinth.LRM.Objects.DataManager;
import me.Lorinth.LRM.Objects.Disableable;
import me.Lorinth.LRM.Util.OutputHandler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.Plugin;

public class SkillAPIDataManager extends Disableable implements DataManager {

    private SkillAPIListener skillAPIEventListener;

    public void loadData(FileConfiguration config, Plugin plugin){
        if(!config.getKeys(false).contains("SkillAPI") ||
                !config.getConfigurationSection("SkillAPI").getKeys(false).contains("Enabled")){
            OutputHandler.PrintInfo("SkillAPI options not found, Generating...");
            setDefaults(config, plugin);
        }

        if(Bukkit.getServer().getPluginManager().getPlugin("SkillAPI") == null)
            this.setDisabled(true);
        else{
            this.setDisabled(!config.getBoolean("Heroes.Enabled"));
        }

        if(this.isDisabled())
            OutputHandler.PrintInfo("SkillAPI Integration is Disabled");
        else {
            OutputHandler.PrintInfo("SkillAPI Integration is Enabled!");
            skillAPIEventListener = new SkillAPIListener();
            Bukkit.getPluginManager().registerEvents(skillAPIEventListener, plugin);
        }
    }

    //Unused method
    public boolean saveData(FileConfiguration config){
        return false;
    }

    public boolean handleEntityDeathEvent(EntityDeathEvent deathEvent, Player player, int exp){
        if(this.isDisabled())
            return false;

        skillAPIEventListener.bindDeathEvent(player, new CreatureDeathData(exp, deathEvent.getEntity()));
        return true;
    }

    private void setDefaults(FileConfiguration config, Plugin plugin){
        config.set("SkillAPI.Enabled", false);
        plugin.saveConfig();
    }
}
