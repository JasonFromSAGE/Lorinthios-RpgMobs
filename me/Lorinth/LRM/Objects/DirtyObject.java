package me.Lorinth.LRM.Objects;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * Created by lorinthio on 1/24/2018.
 */
public abstract class DirtyObject {

    //Object to check whether this item is new, or has been changed during the session

    private boolean Changed = false;
    private boolean New = false;

    /**
     * Save to file
     * @param config - config file to save to
     */
    public void save(FileConfiguration config){
        if(isDirty()){
            saveData(config, "");
            setClean();
        }
    }

    /**
     * @param config - config file to save to
     * @param prefix - the config path prefix
     */
    public void save(FileConfiguration config, String prefix){
        if(isDirty()){
            saveData(config, prefix);
            setClean();
        }
    }

    /**
     * Method that subclasses implement to specify how they are saved
     * @param config - config file to save to
     * @param prefix - the path prefix we will use
     */
    protected abstract void saveData(FileConfiguration config, String prefix);

    /**
     * Checks if this object has been changed
     * @return - true - the object was changed, false - it stayed the same
     */
    public boolean isDirty(){
        return Changed || New;
    }

    /**
     * Internally set the object has changed
     */
    protected void setDirty(){
        Changed = true;
    }

    /**
     * Mark that this object was created new, not from file
     */
    protected void setNew(){
        New = true;
    }

    /**
     * Mark the object clean, only used after saving to ensure all updates are saved/reloaded correctly
     */
    protected void setClean(){
        Changed = false;
        New = false;
    }

}
