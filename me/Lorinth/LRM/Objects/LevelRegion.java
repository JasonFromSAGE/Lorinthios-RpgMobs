package me.Lorinth.LRM.Objects;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

/**
 * Created by lorinthio on 1/24/2018.
 */
public class LevelRegion extends DirtyObject{
    private String Name;
    private int Level;
    private HashMap<String, String> EntityNames = new HashMap<>();
    private boolean isDisabled = false;

    @Override
    protected void saveData(FileConfiguration config, String prefix){

    }

    public LevelRegion(FileConfiguration config, String name){

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

    public String getName(){
        return Name;
    }

    public boolean isDisabled(){
        return isDisabled || isDeleted();
    }
}
