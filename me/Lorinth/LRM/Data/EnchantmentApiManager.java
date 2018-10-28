package me.Lorinth.LRM.Data;

import com.sucy.enchant.EnchantmentAPI;
import me.Lorinth.LRM.Objects.DataManager;
import me.Lorinth.LRM.Objects.Disableable;
import me.Lorinth.LRM.Util.OutputHandler;
import me.Lorinth.LRM.Util.TryParse;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class EnchantmentApiManager extends Disableable implements DataManager {

    private static boolean IsDisabled = false;

    public static Object getEnchantment(String name){
        if(IsDisabled)
            return TryParse.parseEnchantFriendlyName(name);
        else
            return EnchantmentAPI.getEnchantment(name);
    }

    @Override
    public void loadData(FileConfiguration config, Plugin plugin) {
        if(Bukkit.getPluginManager().getPlugin("EnchantmentAPI") == null){
            IsDisabled = true;
            OutputHandler.PrintInfo("Not using EnchantmentAPI");
        }
        else {
            IsDisabled = false;
            OutputHandler.PrintInfo("Using EnchantmentAPI!");
        }
    }

    @Override
    public boolean saveData(FileConfiguration config) {
        return false;
    }
}
