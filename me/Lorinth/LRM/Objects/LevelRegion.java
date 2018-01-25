package me.Lorinth.LRM.Objects;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * Created by lorinthio on 1/24/2018.
 */
public class LevelRegion extends DirtyObject{
    private String Name;
    private int Level;

    protected void saveData(FileConfiguration config, String prefix){

    }

    public LevelRegion(FileConfiguration config, String name){

    }

    public LevelRegion(String Name, int Level){
        this.Name = Name;
        this.Level = Level;
    }

    public int getLevel(){
        return Level;
    }

    public String getName(){
        return Name;
    }
}
