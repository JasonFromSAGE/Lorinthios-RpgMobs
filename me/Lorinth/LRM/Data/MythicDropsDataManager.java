package me.Lorinth.LRM.Data;

import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDrops;
import com.tealcube.minecraft.bukkit.mythicdrops.items.builders.MythicDropBuilder;
import me.Lorinth.LRM.Listener.MythicDropsListener;
import me.Lorinth.LRM.Objects.CreatureData;
import me.Lorinth.LRM.Objects.DataManager;
import me.Lorinth.LRM.Objects.Disableable;
import me.Lorinth.LRM.Objects.MythicDrops.MythicDropsData;
import me.Lorinth.LRM.Util.ConfigHelper;
import me.Lorinth.LRM.Util.OutputHandler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class MythicDropsDataManager extends Disableable implements DataManager {

    private MythicDropsListener mythicDropsListener;
    private MythicDrops mythicDrops;
    private MythicDropBuilder mythicDropBuilder;
    private DataLoader dataLoader;

    public MythicDropsDataManager(DataLoader dataloader){
        this.dataLoader = dataloader;
    }

    @Override
    public void loadData(FileConfiguration config, Plugin plugin) {
        if(!ConfigHelper.ConfigContainsPath(config, "MythicDrops.Enabled")){
            OutputHandler.PrintInfo("MythicDrops options not found, Generating...");
            setDefaults(config, plugin);
        }

        mythicDrops = (MythicDrops) Bukkit.getServer().getPluginManager().getPlugin("MythicDrops");
        if(mythicDrops == null)
            this.setDisabled(true);
        else
            this.setDisabled(!config.getBoolean("MythicDrops.Enabled"));


        if(this.isDisabled())
            OutputHandler.PrintInfo("MythicDrops Integration is Disabled");
        else {
            OutputHandler.PrintInfo("MythicDrops Integration is Enabled!");
            mythicDropsListener = new MythicDropsListener(this);
            mythicDropBuilder = new MythicDropBuilder(mythicDrops);
            Bukkit.getPluginManager().registerEvents(mythicDropsListener, plugin);
        }
    }

    @Override
    public boolean saveData(FileConfiguration config) {
        return false;
    }

    private void setDefaults(FileConfiguration config, Plugin plugin){
        config.set("MythicDrops.Enabled", false);
        plugin.saveConfig();
    }

    public ItemStack getDrop(Entity entity, int level){
        CreatureData data = dataLoader.getCreatureDataManager().getData(entity.getType());
        MythicDropsData mythicDropsData = data.getMythicDropsData();
        String tierName = mythicDropsData.getNextTierName(level);

        if(tierName != null){
            ItemStack item = mythicDropBuilder.withTier(tierName).build();
            return item;
        }

        return null;
    }
}
