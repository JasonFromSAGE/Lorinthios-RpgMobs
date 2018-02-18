package me.Lorinth.LRM.Objects;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Contains data for spawn calculations including, starting level, distance, center buffer, max level, and if it is disabled
 */
public class SpawnPoint extends DirtyObject{

    private String Name;
    private Location Center;
    private int StartingLevel = 1;
    private int LevelDistance = 50;
    private int CenterBuffer = 50;
    private int MaxLevel = 1000;

    /**
     * Loads a pre-existing spawn point from config
     * @param config config file
     * @param worldName world the spawnpoint exists in
     * @param prefix yml path prefix
     */
    public SpawnPoint(FileConfiguration config, String worldName, String spawnerName, String prefix){
        World world = Bukkit.getWorld(worldName);
        if(world != null){
            int x, y, z;
            x = config.getInt(prefix + ".Location.X");
            z = config.getInt(prefix + ".Location.Z");

            Name = spawnerName;
            Center = new Location(Bukkit.getWorld(worldName), x, 0, z);
            StartingLevel = config.getInt(prefix + ".Level");
            LevelDistance = config.getInt(prefix + ".Distance");
            CenterBuffer = config.getInt(prefix + ".CenterBuffer");
            setDisabled(config.getBoolean(prefix + ".Disabled"));
            MaxLevel = config.getInt(prefix + ".MaxLevel");
            if(MaxLevel == -1)
                MaxLevel = Integer.MAX_VALUE;
        }
        else{
            setDisabled(true);
        }
    }

    /**
     * Creates a new Spawn Point via code
     * @param center center of the spawnpoint
     * @param name the alias
     * @param level starting level of mobs
     * @param distance distance between level increments
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
        prefix += Name;
        if(this.isDeleted()){
            config.set(prefix, null);
        }
        else{
            config.set(prefix + ".Disabled", isDisabled());
            config.set(prefix + ".Level", StartingLevel);
            config.set(prefix + ".MaxLevel", MaxLevel == Integer.MAX_VALUE ? -1 : MaxLevel);
            config.set(prefix + ".Distance", LevelDistance);
            config.set(prefix + ".CenterBuffer", CenterBuffer);
            config.set(prefix + ".Location.X", Center.getBlockX());
            config.set(prefix + ".Location.Z", Center.getBlockZ());
        }
    }

    /**
     * Checks if this spawn point is disabled, or is marked deleted
     * @return isDisabled
     */
    @Override
    public boolean isDisabled(){
        return super.isDisabled() || isDeleted();
    }

    /**
     * Gets the name of the spawn point
     * @return Name of the spawn point
     */
    public String getName(){
        return Name;
    }

    /**
     * Gets the world the spawn point is located in, To change worlds use setCenter in a different world
     * @return the world the spawn point is in
     */
    public World getWorld(){
        return Center.getWorld();
    }

    /**
     * Gets the center location of the spawn point, (Note: Y is at 0)
     * @return Center Point
     */
    public Location getCenter(){
        return Center;
    }

    /**
     * Sets the center point of the spawn region which distance will be calculated from
     * @param loc center of the spawn point (if null nothing changes)
     */
    public void setCenter(Location loc){
        if(loc == null)
            return;

        loc.setY(0);
        Center = loc;
        this.setDirty();
    }

    /**
     * Gets the center buffer of the spawn region, this delays leveling calculations of the spawn point by a given distance
     * @return
     */
    public int getCenterBuffer(){
        return CenterBuffer;
    }

    /**
     * Sets the center buffer of the spawn region which will delay leveling calculations by a given distance
     * @param centerBuffer the center distance delay/buffer
     */
    public void setCenterBuffer(int centerBuffer){
        CenterBuffer = centerBuffer;
    }

    /**
     * Gets the level this spawn point starts incrementing from
     * @return Starting Level
     */
    public int getStartingLevel(){
        return StartingLevel;
    }

    /**
     * Sets the level this spawn point starts incrementing from
     * @param level between 1 and Integer.MAX_VALUE
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
     * @param levelDistance the distance between level increments
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
        return MaxLevel;
    }

    /**
     * Sets the max level of monsters in this spawn points area
     * @param maxLevel the max level of monsters
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
     * @param loc The location we want to check
     * @param algorithm The algorithm to calculate distance
     * @return
     */
    public int calculateDistance(Location loc, DistanceAlgorithm algorithm){
        loc.setY(0); // We only care about x/y

        if(algorithm == DistanceAlgorithm.Circle){
            return (int) (Center.distance(loc));
        }
        else if(algorithm == DistanceAlgorithm.Diamond){
            return (int) (getSimpleDistance(Center, loc));
        }
        else if(algorithm == DistanceAlgorithm.Square){
            return (int) (getSquareDistance(Center, loc));
        }

        return 1;
    }

    private int calculateDistanceWithBuffer(Location loc, DistanceAlgorithm algorithm){
        return Math.max(0, calculateDistance(loc, algorithm) - CenterBuffer);
    }

    /**
     * Calculate the level at a location based on this spawn point
     * @param loc The location we want to check
     * @param algorithm The algorithm to calculate distance
     * @return
     */
    public int calculateLevel(Location loc, DistanceAlgorithm algorithm){
        loc.setY(0); // We only care about x/y

        try{
            return (int) Math.min(MaxLevel, StartingLevel + (calculateDistanceWithBuffer(loc, algorithm) / (double) LevelDistance));
        }
        catch(Exception e){
            e.printStackTrace();
            return 1;
        }
    }

    /**
     *
     * @param a first location to compare distance
     * @param b second location to compare distance
     * @return the difference in x + z (results in diamond-esque shapes)
     */
    private double getSimpleDistance(Location a, Location b){
        int x = Math.abs(a.getBlockX() - b.getBlockX());
        int z = Math.abs(a.getBlockZ() - b.getBlockZ());

        return x+z;
    }

    private double getSquareDistance(Location a, Location b){
        int x = Math.abs(a.getBlockX() - b.getBlockX());
        int z = Math.abs(a.getBlockZ() - b.getBlockZ());

        return Math.max(x,z);
    }
}
