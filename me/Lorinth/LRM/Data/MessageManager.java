package me.Lorinth.LRM.Data;

import me.Lorinth.LRM.Objects.DataManager;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class MessageManager implements DataManager {

    public static String MoneyDrop;

    @Override
    public void loadData(FileConfiguration config, Plugin plugin) {
        MoneyDrop = ChatColor.translateAlternateColorCodes('&', config.getString("MoneyDrop"));
    }

    @Override
    public boolean saveData(FileConfiguration config) {
        return false;
    }

}
