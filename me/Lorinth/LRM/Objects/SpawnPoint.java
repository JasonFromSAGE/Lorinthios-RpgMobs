package me.Lorinth.LRM.Objects;

import me.Lorinth.LRM.LorinthsRpgMobs;
import org.bukkit.Location;
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

    //Loaded from config
    public SpawnPoint(FileConfiguration config, String prefix){

    }

    //Newly added during session
    public SpawnPoint(Location center, String name, int level, int distance){
        Name = name;
        Center = center;
        StartingLevel = level;
        LevelDistance = distance;

        this.setNew();
    }

    protected void SaveData(FileConfiguration config, String prefix){

    }

    public void setMaxLevel(int maxLevel){
        MaxLevel = maxLevel;
        hasMaxLevel = true;

        this.setChanged();
    }

    public Location getCenter(){
        return Center;
    }

    public int getStartingLevel(){
        return StartingLevel;
    }

    public int getLevelDistance(){
        return LevelDistance;
    }

    public int getMaxLevel(){
        if(hasMaxLevel)
            return MaxLevel;
        return Integer.MAX_VALUE;
    }

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

    public int calculateLevel(Location loc, DistanceAlgorithm algorithm){
        loc.setY(0); // We only care about x/y

        try{
            return (int) (calculateDistance(loc, algorithm) / (double) LevelDistance);
        }
        catch(Exception e){
            e.printStackTrace();
            return 1;
        }
    }

    //simple math x+z = distance
    //results in steeper leveling diagonally, but quicker calculations
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
