package me.Lorinth.LRM.Objects;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * Used for dirty tracking/saving within LorinthsRpgMobs
 */
public abstract class DirtyObject extends Disableable{

    //Object to check whether this item is new, changed, or marked for deletion
    private boolean isChanged = false;
    private boolean isDeleted = false;
    private boolean isNew = false;

    /**
     * Save to file
     * @param config - config file to save to
     */
    public boolean save(FileConfiguration config){
        return save(config, "");
    }

    /**
     * @param config - config file to save to
     * @param prefix - the config path prefix
     */
    public boolean save(FileConfiguration config, String prefix){
        if(isDirty() || isDeleted()){
            saveData(config, prefix);
            setClean();

            return true;
        }
        return false;
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
        return isChanged || isNew;
    }

    /**
     * Checks if this object has been deleted
     * @return - true - the object is marked to be deleted, false - it stayed the same
     */
    public boolean isDeleted(){
        return isDeleted;
    }

    /**
     * Internally set the object is deleted
     */
    public void setDeleted(){
        isDeleted = true;
    }

    /**
     * Internally set the object has changed
     */
    protected void setDirty(){
        isChanged = true;

        if(isDeleted)
            isDeleted = false;
    }

    /**
     * Mark that this object was created new, not from file
     */
    protected void setNew(){
        isNew = true;
    }

    /**
     * Mark the object clean, only used after saving to ensure all updates are saved/reloaded correctly
     */
    protected void setClean(){
        isChanged = false;
        isNew = false;
        isDeleted = false;
    }

}
