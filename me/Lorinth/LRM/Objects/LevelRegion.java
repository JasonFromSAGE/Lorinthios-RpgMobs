package me.Lorinth.LRM.Objects;

import me.Lorinth.LRM.Util.OutputHandler;
import me.Lorinth.LRM.Util.TryParse;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Contains data for levels/names for a matched world guard region
 */
public class LevelRegion extends DirtyObject{
    private String Name;
    private Integer Level = 1;
    private Integer MaxLevel = 1;
    private HashMap<String, NameData> EntityNames = new HashMap<>();
    private HashMap<EntityType, RandomNullCollection<String>> randomBossReplacement = new HashMap<>();
    private List<String> DisabledEntities = new ArrayList<>();
    private boolean EntitiesDisabled = false;
    private Random random = new Random();

    public LevelRegion(String name, String level){
        Name = name;
        setLevel(level);
    }

    public LevelRegion(String name, int level){
        this(name, level, level);
    }

    public LevelRegion(String name, int level, int maxLevel){
        Name = name;
        Level = level;
        MaxLevel = maxLevel;
        setNew();
    }

    public LevelRegion(FileConfiguration config, String name, String prefix){
        Name = name;
        prefix += "." + name + ".";
        this.setDisabled(config.getBoolean(prefix + "Disabled"));

        if(!this.isDisabled()){
            loadLevel(config, prefix);
            loadNames(config, prefix);
            loadBosses(config, prefix);
            loadDisabledEntities(config, prefix);
        }
    }

    private void loadLevel(FileConfiguration config, String prefix){
        String level = config.getString(prefix + "Level");
        if(level.indexOf('-') > -1){
            String[] values = level.split(Pattern.quote("-"));
            Level = Integer.parseInt(values[0]);
            MaxLevel = Integer.parseInt(values[1]);
        }
        else{
            Level = Integer.parseInt(level);
            MaxLevel = Level;
        }

        if(Level < 1){
            Level = 1;
            if(MaxLevel < Level)
                MaxLevel = Level;
        }
    }

    private void loadNames(FileConfiguration config, String prefix){
        if(config.contains(prefix + "Names")){
            for(String key : config.getConfigurationSection(prefix + "Names").getKeys(false)){
                EntityNames.put(key, new NameData(Level, config.getString(prefix + "Names." + key ).replace("&", "§"), true));
            }
        }
    }

    private void loadBosses(FileConfiguration config, String prefix){
        if(config.contains(prefix + "Bosses")){
            for(String entityType : config.getConfigurationSection(prefix + "Bosses").getKeys(false)){
                if(TryParse.parseEnum(EntityType.class, entityType)){
                    EntityType type = EntityType.valueOf(entityType);

                    RandomNullCollection<String> randomBossIds = new RandomNullCollection<>();
                    for(String bossId : config.getConfigurationSection(prefix + "Bosses." + entityType).getKeys(false)){
                        randomBossIds.add(config.getDouble(prefix + "Bosses." + entityType + "." + bossId ), bossId);
                    }
                    randomBossReplacement.put(type, randomBossIds);
                }
                else{
                    OutputHandler.PrintError("Unable to handle EntityType: " + OutputHandler.HIGHLIGHT + entityType);
                }

            }
        }
    }

    private void loadDisabledEntities(FileConfiguration config, String prefix){
        if(config.contains(prefix + "DisabledEntities")){
            DisabledEntities = config.getStringList(prefix + "DisabledEntities");
            if(DisabledEntities.contains("*"))
                EntitiesDisabled = true;
        }
    }

    public HashMap<EntityType, RandomNullCollection<String>> getBossReplacements(){
        return randomBossReplacement;
    }

    public int getLevel(){
        if(MaxLevel != Level)
            return random.nextInt((MaxLevel+1) - Level) + Level;
        return Level;
    }

    public String getLevelRange(){
        if(MaxLevel != Level)
            return Level + "-" + MaxLevel;
        return Level.toString();
    }

    public void setLevel(String level){
        if(level.contains("-")){
            String[] values = level.split("-");
            Level = Integer.parseInt(values[0]);
            MaxLevel = Integer.parseInt(values[1]);
        }
        else{
            Level = Integer.parseInt(level);
            MaxLevel = Level;
        }

        if(Level < 1){
            Level = 1;
            if(MaxLevel < Level)
                MaxLevel = Level;
        }

        setDirty();
    }

    public boolean entityIsDisabled(Entity entity){
        return EntitiesDisabled || this.DisabledEntities.contains(entity.getType().toString());
    }

    public String getName(){
        return Name;
    }

    public NameData getEntityName(EntityType type){
        return EntityNames.getOrDefault(type.toString(), null);
    }

    @Override
    protected void saveData(FileConfiguration config, String prefix){
        prefix += "." + Name;
        if(isDeleted()){
            config.set(prefix, null);
            return;
        }
        String levelValue =  MaxLevel != 0 ? (Level.toString() + "-" + MaxLevel.toString()) : (Level.toString());
        config.set(prefix + ".Level", levelValue);
        config.set(prefix + ".Disabled", isDisabled());
    }

    @Override
    public boolean isDisabled(){
        return super.isDisabled() || isDeleted();
    }
}
