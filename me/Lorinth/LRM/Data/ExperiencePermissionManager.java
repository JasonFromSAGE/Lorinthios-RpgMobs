package me.Lorinth.LRM.Data;

import me.Lorinth.LRM.Objects.DataManager;
import me.Lorinth.LRM.Objects.ExperiencePermission;
import me.Lorinth.LRM.Util.ConfigHelper;
import me.Lorinth.LRM.Util.OutputHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;


public class ExperiencePermissionManager implements DataManager {

    private ArrayList<ExperiencePermission> experiencePermissions;
    private float defaultValue = 1.0f;

    @Override
    public void loadData(FileConfiguration config, Plugin plugin) {
        experiencePermissions = new ArrayList<>();
        if(ConfigHelper.ConfigContainsPath(config, "ExperiencePermissions")){
            for(String key : config.getConfigurationSection("ExperiencePermissions").getKeys(false)){
                if(key.equalsIgnoreCase("default"))
                    defaultValue = (float) config.getDouble("ExperiencePermissions." + key);
                else{
                    ExperiencePermission perm = new ExperiencePermission(key, (float) config.getDouble("ExperiencePermissions." + key));
                    experiencePermissions.add(perm);
                    OutputHandler.PrintInfo("Loaded permission, " + OutputHandler.HIGHLIGHT + perm.getPermission() + OutputHandler.INFO + " with exp multiplier " + OutputHandler.HIGHLIGHT + perm.getMultiplier());
                }
            }
        }
        else{
            //define and save
            setDefaults(config, plugin);
            loadData(plugin.getConfig(), plugin);
        }
    }

    @Override
    public boolean saveData(FileConfiguration config) {
        return false;
    }

    public float getExperienceMultiplier(Player player){
        float multi = defaultValue;
        for(ExperiencePermission perm : experiencePermissions){
            if(perm.hasPermission(player)){
                OutputHandler.PrintInfo(player.getDisplayName() + " has permission, " + perm.getPermission());
                if(perm.getMultiplier() > multi)
                    multi = perm.getMultiplier();
            }
        }
        return multi;
    }

    private void setDefaults(FileConfiguration config, Plugin plugin){
        config.set("ExperiencePermissions.default", 1.0);
        config.set("ExperiencePermissions.vip", 1.5);

        plugin.saveConfig();
    }

}
