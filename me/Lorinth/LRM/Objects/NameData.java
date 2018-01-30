package me.Lorinth.LRM.Objects;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * Contains name data at a given level with a name/format and option to override global settings
 *
 * These will be overriden by region settings
 */
public class NameData {
    private int Level;
    private String Name;
    private boolean OverrideFormat; //Overrides global name format

    public NameData(int level, String name, boolean overrideFormat){
        Level = level;
        Name = name;
        OverrideFormat = overrideFormat;
    }

    public void save(FileConfiguration config, String prefix){
        config.set(prefix + ".Names." + Level + ".Name", Name);
        if(OverrideFormat)
            config.set(prefix + ".Names." + Level + ".OverrideFormat", OverrideFormat);
        else
            config.set(prefix + ".Names." + Level + ".OverrideFormat", null);
    }

    public String getName(int level, String format){
        if(OverrideFormat)
            return Name.replace("{level}", Integer.toString(level));
        else
            return format.replace("{name}", Name)
                         .replace("{level}", Integer.toString(level));
    }

    public int getLevel(){
        return Level;
    }
}
