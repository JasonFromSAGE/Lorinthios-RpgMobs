package me.Lorinth.LRM.Data;

import me.Lorinth.LRM.Objects.DataManager;
import me.Lorinth.LRM.Util.ConfigHelper;
import me.Lorinth.LRM.Util.OutputHandler;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import static org.bukkit.Bukkit.getServer;

public class VaultManager implements DataManager {

    private static boolean enabled = false;
    public static Economy economy;

    @Override
    public void loadData(FileConfiguration config, Plugin plugin) {
        if(!ConfigHelper.ConfigContainsPath(config, "Vault.Enabled")){
            OutputHandler.PrintInfo("Vault options not found, Generating...");
            setDefaults(config, plugin);
        }

        boolean configEnabled = config.getBoolean("Vault.Enabled");
        enabled = configEnabled && setupEconomy();

        if(enabled){
            OutputHandler.PrintInfo("Vault Integration is Enabled!");
        }
        else if(configEnabled && !enabled){
            OutputHandler.PrintError("Vault Integration was enabled, but unable to find VAULT PLUGIN");
        }
    }

    @Override
    public boolean saveData(FileConfiguration config) {
        return false;
    }

    private void setDefaults(FileConfiguration config, Plugin plugin){
        config.set("Vault.Enabled", false);
        plugin.saveConfig();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            OutputHandler.PrintInfo("No Vault Plugin");
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (rsp == null) {
            OutputHandler.PrintInfo("No RegisteredServiceProvider");
            return false;
        }
        economy = rsp.getProvider();
        return true;
    }

    public static boolean isEnabled(){
        return enabled;
    }

    public static void addMoney(Player player, Entity entity, Integer money){
        if(isEnabled()) {
            economy.depositPlayer(player, money);
            if(MessageManager.MoneyDrop != null){
                player.sendMessage(
                        MessageManager.MoneyDrop
                                .replace("{entity}", entity.getCustomName() != null ? entity.getCustomName() : entity.getName())
                                .replace("{money}", money.toString())
                );
            }
        }
    }

}
