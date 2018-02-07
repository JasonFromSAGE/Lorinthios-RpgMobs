package me.Lorinth.LRM.Data;

import me.Lorinth.LRM.Objects.DataManager;
import me.Lorinth.LRM.Objects.Disableable;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class MobVariantDataManager extends Disableable implements DataManager {

    @Override
    public void loadData(FileConfiguration config, Plugin plugin) {

    }

    @Override
    public boolean saveData(FileConfiguration config) {
        return false;
    }
}
