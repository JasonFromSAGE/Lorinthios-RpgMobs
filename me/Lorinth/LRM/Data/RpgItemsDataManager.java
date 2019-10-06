package me.Lorinth.LRM.Data;

import me.Lorinth.LRM.Listener.RpgItemsListener;
import me.Lorinth.LRM.Objects.DataManager;
import me.Lorinth.LRM.Objects.Disableable;
import me.Lorinth.LRM.Util.ConfigHelper;
import me.Lorinth.LRM.Util.OutputHandler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.Plugin;

public class RpgItemsDataManager extends Disableable implements DataManager {

    private RpgItemsListener rpgItemsListener;

    @Override
    public void loadData(FileConfiguration config, Plugin plugin) {
        if(!ConfigHelper.ConfigContainsPath(config, "LorinthsRpgItems.Enabled")){
            OutputHandler.PrintInfo("LorinthsRpgItems options not found, Generating...");
            setDefaults(config, plugin);
        }

        if(Bukkit.getServer().getPluginManager().getPlugin("LorinthsRpgItems") == null)
            this.setDisabled(true);
        else{
            this.setDisabled(!config.getBoolean("LorinthsRpgItems.Enabled"));
        }

        if(this.isDisabled())
            OutputHandler.PrintInfo("LorinthsRpgItems Integration is Disabled");
        else {
            OutputHandler.PrintInfo("LorinthsRpgItems Integration is Enabled!");
            rpgItemsListener = new RpgItemsListener();
            Bukkit.getPluginManager().registerEvents(rpgItemsListener, plugin);
        }
    }

    //Unused method
    public boolean saveData(FileConfiguration config){
        return false;
    }

    public boolean handleEntityDeathEvent(EntityDeathEvent deathEvent, int exp) {
        if (this.isDisabled())
            return false;

        rpgItemsListener.bindExperienceEvent(deathEvent.getEntity().getUniqueId(), exp);
        return true;
    }

    private void setDefaults(FileConfiguration config, Plugin plugin){
        config.set("RpgItems.Enabled", false);

        plugin.saveConfig();
    }
}
