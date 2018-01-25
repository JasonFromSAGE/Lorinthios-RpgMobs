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

    //Loaded from config
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

    //Newly added during session
    public SpawnPoint(Location center, String name, int level, int distance){
        center.setY(0);

        Name = name;
        Center = center;
        StartingLevel = level;
        LevelDistance = distance;

        this.setNew();
    }

    protected void SaveData(FileConfiguration config, String prefix){

    }

    public boolean IsDisabled(){
        return isDisabled;
    }

    public Location GetCenter(){
        return Center;
    }

    public int GetStartingLevel(){
        return StartingLevel;
    }

    public int GetLevelDistance(){
        return LevelDistance;
    }

    public int GetMaxLevel(){
        if(hasMaxLevel)
            return MaxLevel;
        return Integer.MAX_VALUE;
    }

    public int CalculateDistance(Location loc, DistanceAlgorithm algorithm){
        loc.setY(0); // We only care about x/y

        if(algorithm == DistanceAlgorithm.Accurate){
            return (int) (Center.distance(loc));
        }
        else if(algorithm == DistanceAlgorithm.Optimized){
            return (int) (getSimpleDistance(Center, loc));
        }

        return 1;
    }

    public int CalculateLevel(Location loc, DistanceAlgorithm algorithm){
        loc.setY(0); // We only care about x/y

        try{
            return (int) (CalculateDistance(loc, algorithm) / (double) LevelDistance) + 1;
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
