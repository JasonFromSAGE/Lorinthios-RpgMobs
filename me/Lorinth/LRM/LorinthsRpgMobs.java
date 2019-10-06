package me.Lorinth.LRM;

import com.google.common.io.Files;
import me.Lorinth.LRM.Command.CommandConstants;
import me.Lorinth.LRM.Command.MainExecutor;
import me.Lorinth.LRM.Data.*;
import me.Lorinth.LRM.Listener.CreatureEventListener;
import me.Lorinth.LRM.Listener.UpdaterEventListener;
import me.Lorinth.LRM.Objects.Properties;
import me.Lorinth.LRM.Util.MetaDataConstants;
import me.Lorinth.LRM.Util.OutputHandler;
import me.Lorinth.LRM.Util.ResourceHelper;
import me.Lorinth.LRM.Variants.MobVariant;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Main class of LorinthsRpgMobs contains main API Methods
 */
public class LorinthsRpgMobs extends JavaPlugin {

    public static Updater updater;
    public static LorinthsRpgMobs instance;
    public static Properties properties;
    private static DataLoader dataLoader;
    private int loads = 0;

    @Override
    public void onEnable(){
        instance = this;
        OutputHandler.PrintInfo("Enabling v" + getDescription().getVersion() + "...");
        firstLoad();
        registerCommands();

        if(createDataLoader()){
            loadMinecraftVersion();
            loadMessages();
            loadBossManager();
            Bukkit.getPluginManager().registerEvents(new CreatureEventListener(dataLoader), this);
            Bukkit.getPluginManager().registerEvents(new UpdaterEventListener(), this);
            OutputHandler.PrintInfo("Finished!");
        }
    }

    @Override
    public void onDisable(){
        OutputHandler.PrintInfo("Disabling...");

        if(dataLoader != null){
            //Load possible changes in the file from user
            File file = new File(getDataFolder(), "config.yml");
            File backup = new File(getDataFolder(), "backup.yml");
            try {
                Files.copy(file, backup);
                YamlConfiguration config = new YamlConfiguration();
                config.load(file);

                //Apply the changes we gained during the session
                dataLoader.saveData(config);

                config.save(file);
            } catch(Exception e){
                OutputHandler.PrintException("Error on Disable", e);
            }
        }
        HandlerList.unregisterAll(this);
    }

    public static void Reload(){
        instance.onDisable();
        instance.onEnable();
    }

    private boolean createDataLoader(){
        try {
            FileConfiguration config;
            if(this.loads == 0)
                config = this.getConfig();
            else{
                config = new YamlConfiguration();
                config.load(new File(getDataFolder(), "config.yml"));
            }

            checkAutoUpdates(config);

            dataLoader = new DataLoader();
            dataLoader.loadData(config, this);
            this.loads++;
            return true;
        }
        catch(Exception e) {
            OutputHandler.PrintException("Error loading config.yml", e);
            OutputHandler.PrintError("Rpg Mobs Disabled");
            return false;
        }
    }

    private void loadMessages(){
        try {
            File file = new File(getDataFolder(), "messages.yml");
            YamlConfiguration config = new YamlConfiguration();
            config.load(file);

            new MessageManager().loadData(config, this);
        }
        catch(Exception e){
            OutputHandler.PrintException("Error loading messages.yml", e);
        }
    }

    private void loadBossManager(){
        new BossApiManager();
    }

    private void firstLoad(){
        try{
            ResourceHelper.copy(getResource("config.yml"), new File(getDataFolder(), "config.yml"));
            ResourceHelper.copy(getResource("resources/messages.yml"), new File(getDataFolder(), "messages.yml"));
        }
        catch(Exception exc){
            exc.printStackTrace();
        }
    }

    private void loadMinecraftVersion(){
        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        int subVersion = Integer.parseInt(version.replace("v1_", "").replaceAll("_R\\d", ""));
        properties.IsAttributeVersion = subVersion > 8;
    }

    private void registerCommands(){
        getCommand(CommandConstants.LorinthsRpgMobsCommand).setExecutor(new MainExecutor());
    }

    private void checkAutoUpdates(FileConfiguration config){
        if(config.getKeys(false).contains("AllowAutoUpdates")){
            if(config.getBoolean("AllowAutoUpdates"))
                updater = new Updater(this, getFile(), Updater.UpdateType.DEFAULT);
            else
                updater = new Updater(this, getFile(), Updater.UpdateType.NO_DOWNLOAD);
        }
        else{
            config.set("AllowAutoUpdates", false);
            saveConfig();
        }
    }

    public static Updater ForceUpdate(Updater.UpdateCallback callback){
        return new Updater(instance, instance.getFile(), Updater.UpdateType.DEFAULT, callback);
    }

    //API Methods
    /**
     * Get level of a given creature
     * @param creature creature you want to check
     * @return level of creature, null if no level
     */
    public static Integer GetLevelOfCreature(Creature creature){
        return GetLevelOfEntity(creature);
    }

    /**
     * Get level of entity
     * @param entity the entity you want to check
     * @return null if no level, otherwise returns level of entity
     */
    public static Integer GetLevelOfEntity(Entity entity){
        if(entity instanceof LivingEntity){
            if(entity.hasMetadata(MetaDataConstants.Level))
                if(entity.getMetadata(MetaDataConstants.Level).size() > 0)
                    return entity.getMetadata(MetaDataConstants.Level).get(0).asInt();
        }
        return null;
    }

    /**
     * Returns the variant of the entity. Such as Burning, Poisonous, Fast, Slow, etc
     * @param entity
     * @return - The variant of the entity, null if no variant applied
     */
    public static MobVariant GetMobVariantOfEntity(Entity entity){
        if(entity.hasMetadata(MetaDataConstants.Variant)){
            return (MobVariant) entity.getMetadata(MetaDataConstants.Variant).get(0).value();
        }

        return null;
    }

    /**
     * Get the level of a specific location
     * @param location location to check
     * @return level at location
     */
    public static int GetLevelAtLocation(Location location){
        return dataLoader.calculateLevel(location, null);
    }

    /**
     * Get the spawn point manager which contains data for all spawn points which you can read/write to
     * @return SpawnPointManager
     */
    public static SpawnPointManager GetSpawnPointManager(){
        return dataLoader.getSpawnPointManager();
    }

    public static MobVariantDataManager GetMobVariantManager(){ return dataLoader.getMobVariantManager(); }

    public static MythicMobsDataManager GetMythicMobsDataManager(){
        return dataLoader.getMythicMobsDataManager();
    }

    /**
     * Get the level region manager which contains data for all regions with level settings
     * @return LevelRegionManager
     */
    public static LevelRegionManager GetLevelRegionManager(){
        return dataLoader.getLevelRegionManager();
    }

    /**
     * Get the creature data manager which contains data for all entities which you can read/write to
     * @return CreatureDataManager
     */
    public static CreatureDataManager GetCreatureDataManager(){
        return dataLoader.getCreatureDataManager();
    }

    public static boolean IsMythicMob(Entity entity){
        return dataLoader.getMythicMobsDataManager().isMythicMob(entity);
    }
}
