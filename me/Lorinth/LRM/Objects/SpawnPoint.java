package me.Lorinth.LRM.Objects;

import me.Lorinth.LRM.LorinthsRpgMobs;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.logging.Level;

/**
 * Created by lorinthio on 1/24/2018.
 */
public class SpawnPoint extends DirtyObject{

    private String Name;
    private Location Center;
    private int StartingLevel = 1;
    private int LevelDistance = 50;
    private int MaxLevel = 1000;
    private boolean hasMaxLevel = false;
    private boolean isDisabled = false;

    /**
     * Loads a pre-existing spawn point from config
     * @param config - config file
     * @param worldName - world the spawnpoint exists in
     * @param prefix - yml path prefix
     */
    public SpawnPoint(FileConfiguration config, String worldName, String prefix){
        World world = Bukkit.getWorld(worldName);
        if(world != null){
            int x, y, z;
            x = config.getInt(prefix + ".Location.X");
            z = config.getInt(prefix + ".Location.Z");
            Center = new Location(Bukkit.getWorld(worldName), x, 0, z);
            StartingLevel = config.getInt(prefix + ".Level");
            LevelDistance = config.getInt(prefix + ".Distance");
            MaxLevel = config.getInt(prefix + ".MaxLevel");
            if(MaxLevel == -1)
                MaxLevel = Integer.MAX_VALUE;
        }
        else{
            isDisabled = true;
        }
    }

    /**
     * Creates a new Spawn Point via code
     * @param center - center of the spawnpoint
     * @param name - the alias
     * @param level - starting level of mobs
     * @param distance - distance between level increments
     */
    public SpawnPoint(Location center, String name, int level, int distance){
        center.setY(0);

        Name = name;
        Center = center;
        StartingLevel = level;
        LevelDistance = distance;

        this.setNew();
    }

    protected void saveData(FileConfiguration config, String prefix){

    }

    /**
     * Checks if this spawn point is disabled
     * @return - isDisabled
     */
    public boolean isDisabled(){
        return isDisabled;
    }

    /**
     * Gets the center location of the spawn point, (Note: Y is at 0)
     * @return - Center Point
     */
    public Location getCenter(){
        return Center;
    }

    /**
     * Sets the center point of the spawn region which distance will be calculated from
     * @param loc - center of the spawn point (if null nothing changes)
     */
    public void setCenter(Location loc){
        if(loc == null)
            return;

        loc.setY(0);
        Center = loc;
        this.setDirty();
    }

    /**
     * Gets the level this spawn point starts incrementing from
     * @return - Starting Level
     */
    public int getStartingLevel(){
        return StartingLevel;
    }

    /**
     * Sets the level this spawn point starts incrementing from
     * @param level - between 1 and Integer.MAX_VALUE
     */
    public void setStartingLevel(int level){
        if(level < 1){
            level = 1;
        }

        StartingLevel = level;
        this.setDirty();
    }

    /**
     * Get the distance between level increments for this spawn point
     * @return Level Distance
     */
    public int getLevelDistance(){
        return LevelDistance;
    }

    /**
     * Sets the distance between level increments
     * @param levelDistance - the distance between level increments
     */
    public void setLevelDistance(int levelDistance){
        if(levelDistance < 1){
            levelDistance = 1;
        }

        LevelDistance = levelDistance;
        this.setDirty();
    }

    /**
     * Get the max level allowed within the confines of this spawn point
     * Returns Integer.MAX_VALUE if not set
     * @return Max Level
     */
    public int getMaxLevel(){
        if(hasMaxLevel)
            return MaxLevel;
        return Integer.MAX_VALUE;
    }

    /**
     * Sets the max level of monsters in this spawn points area
     * @param maxLevel - the max level of monsters
     */
    public void setMaxLevel(int maxLevel){
        if(maxLevel < 1){
            maxLevel = 1;
        }

        MaxLevel = maxLevel;
        this.setDirty();
    }

    /**
     * Calculate the distance to a spawnpoint from a location
     * @param loc - The location we want to check
     * @param algorithm - The algorithm to calculate distance
     * @return
     */
    public int calculateDistance(Location loc, DistanceAlgorithm algorithm){
        loc.setY(0); // We only care about x/y

        if(algorithm == DistanceAlgorithm.Accurate){
            return (int) (Center.distance(loc));
        }
        else if(algorithm == DistanceAlgorithm.Optimized){
            return (int) (getSimpleDistance(Center, loc));
        }

        return 1;
    }

    /**
     * Calculate the level at a location based on this spawn point
     * @param loc - The location we want to check
     * @param algorithm - The algorithm to calculate distance
     * @return
     */
    public int calculateLevel(Location loc, DistanceAlgorithm algorithm){
        loc.setY(0); // We only care about x/y

        try{
            return (int) (calculateDistance(loc, algorithm) / (double) LevelDistance) + 1;
        }
        catch(Exception e){
            e.printStackTrace();
            return 1;
        }
    }

    /**
     *
     * @param a - first location to compare distance
     * @param b - second location to compare distance
     * @return - the difference in x + z (results in diamond-esque shapes)
     */
    private double getSimpleDistance(Location a, Location b){
        int x = a.getBlockX() - b.getBlockX();
        int z = a.getBlockX() - b.getBlockX();
        if(x < 0)
            x = -x;
        if(z < 0)
            z = -z;

        return x+z;
    }
}
