package me.Lorinth.LRM.Objects;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * Global name settings used for entities
 */
public class NameOptions extends DirtyObject{

    private boolean tagsAlwaysOn = false;
    private String format = "[Lvl.{level}]{name}";

    public NameOptions(FileConfiguration config){
        loadOptions(config);
    }

    private void loadOptions(FileConfiguration config){
        tagsAlwaysOn = config.getBoolean("Names.TagsAlwaysOn");
        format = config.getString("Names.Format").replaceAll("&", "§");
    }

    @Override
    protected void saveData(FileConfiguration config, String prefix) {
        config.set("Names.TagsAlwaysOn", tagsAlwaysOn);
        config.set("Names.Format", format);
    }

    public boolean getTagsAlwaysOn(){
        return tagsAlwaysOn;
    }

    public void setTagsAlwaysOn(boolean tagsAlwaysOn){
        this.tagsAlwaysOn = tagsAlwaysOn;
        this.setDirty();
    }

    public String getNameFormat(){
        return format;
    }

    public void setNameFormat(String format){
        this.format= format;
        this.setDirty();
    }
}
