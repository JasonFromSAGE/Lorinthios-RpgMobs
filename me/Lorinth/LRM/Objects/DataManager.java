package me.Lorinth.LRM.Objects;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public interface DataManager {

    void loadData(FileConfiguration config, Plugin plugin);

    boolean saveData(FileConfiguration config);

}
