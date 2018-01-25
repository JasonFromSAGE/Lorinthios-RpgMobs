package me.Lorinth.LRM.Objects;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * Created by lorinthio on 1/24/2018.
 */
public abstract class DirtyObject {

    //Object to check whether this item is new, or has been changed during the session

    private boolean Changed = false;
    private boolean New = false;

    public void Save(FileConfiguration config){
        if(isDirty()){
            SaveData(config, "");
            setClean();
        }
    }

    public void Save(FileConfiguration config, String prefix){
        if(isDirty()){
            SaveData(config, prefix);
            setClean();
        }
    }

    protected abstract void SaveData(FileConfiguration config, String prefix);

    public boolean isDirty(){
        return Changed || New;
    }

    public void setChanged(){
        Changed = true;
    }

    public void setNew(){
        New = true;
    }

    public void setClean(){
        Changed = false;
        New = false;
    }

}
