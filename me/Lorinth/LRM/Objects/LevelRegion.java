package me.Lorinth.LRM.Objects;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Contains data for levels/names for a matched world guard region
 */
public class LevelRegion extends DirtyObject{
    private String Name;
    private int Level;
    private HashMap<String, NameData> EntityNames = new HashMap<>();
    private List<String> DisabledEntities = new ArrayList<>();
    private boolean EntitiesDisabled = false;

    @Override
    protected void saveData(FileConfiguration config, String prefix){
        prefix += "." + Name;
        if(isDeleted()){
            config.set(prefix, null);
            return;
        }

        config.set(prefix + ".Level", Level);
        config.set(prefix + ".Disabled", isDisabled());
        for(String key : EntityNames.keySet()){
            config.set(prefix + ".Names." + key, EntityNames.get(key));
        }
    }

    public LevelRegion(FileConfiguration config, String name, String prefix){
        Name = name;
        prefix += "." + name + ".";
        Level = config.getInt(prefix + "Level");
        this.setDisabled(config.getBoolean(prefix + "Disabled"));
        if(config.contains(prefix + "Names")){
            for(String key : config.getConfigurationSection(prefix + "Names").getKeys(false)){
                EntityNames.put(key, new NameData(Level, config.getString(prefix + "Names." + key ).replace("&", "§"), true));
            }
        }
        if(config.contains(prefix + "DisabledEntities")){
            DisabledEntities = config.getStringList(prefix + "DisabledEntities");
            if(DisabledEntities.contains("*"))
                EntitiesDisabled = true;
        }
    }

    public LevelRegion(String name, int level){
        Name = name;
        Level = level;
        setNew();
    }

    public int getLevel(){
        return Level;
    }

    public void setLevel(int level){
        if (level < 1)
            level = 1;

        Level = level;
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
    public boolean isDisabled(){
        return super.isDisabled() || isDeleted();
    }
}
