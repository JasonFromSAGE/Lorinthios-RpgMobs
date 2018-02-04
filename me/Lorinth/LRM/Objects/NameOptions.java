package me.Lorinth.LRM.Objects;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

/**
 * Global name settings used for entities
 */
public class NameOptions implements DataManager{

    private boolean tagsAlwaysOn = false;
    private String format = "[Lvl.{level}]{name}";

    public void loadData(FileConfiguration config, Plugin plugin){
        tagsAlwaysOn = config.getBoolean("Names.TagsAlwaysOn");
        format = config.getString("Names.Format").replaceAll("&", "§");
    }

    public boolean saveData(FileConfiguration config) {
        config.set("Names.TagsAlwaysOn", tagsAlwaysOn);
        config.set("Names.Format", format);
        return true;
    }

    public boolean getTagsAlwaysOn(){
        return tagsAlwaysOn;
    }

    public String getNameFormat(){
        return format;
    }
}
