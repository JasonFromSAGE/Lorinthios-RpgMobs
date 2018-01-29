package me.Lorinth.LRM.Data;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.Lorinth.LRM.Objects.LevelRegion;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

/**
 * Created by lorinthio on 1/28/2018.
 */
public class LevelRegionManager {

    private DataLoader dataLoader;
    private HashMap<String, HashMap<String, LevelRegion>> allLevelRegions = new HashMap<>();

    public LevelRegionManager(DataLoader loader){
        dataLoader = loader;
    }

    protected void loadAllRegions(FileConfiguration config){

    }

    /**
     *
     * @param location - location to check regions for level setting
     * @return -1 if no regions, otherwise returns the lowest level set by regions at the location
     */
    public int getLowestLevelAtLocation(Location location){
        int level = Integer.MAX_VALUE;
        HashMap<String, LevelRegion> regionsInWorld = allLevelRegions.get(location.getWorld().getName());
        if(regionsInWorld == null)
            return -1; //For invalid

        RegionManager regionManager = WGBukkit.getRegionManager(location.getWorld());
        ApplicableRegionSet set = regionManager.getApplicableRegions(location);
        for(ProtectedRegion region : set.getRegions()){
            if(regionsInWorld.containsKey(region.getId()))
                level = Math.min(level, regionsInWorld.get(region.getId()).getLevel());
        }

        return level;
    }

    /**
     * Matches world guard concepts in terms of higher priority = setting for area
     * @param location - location to check level
     * @return -1 if no regions, otherwise returns the level setting of the highest priority region
     */
    public int getHighestPriorityLevelAtLocation(Location location){
        ProtectedRegion highestPriority = null;
        int level = -1;
        HashMap<String, LevelRegion> regionsInWorld = allLevelRegions.get(location.getWorld().getName());
        if(regionsInWorld == null)
            return -1; //For invalid

        RegionManager regionManager = WGBukkit.getRegionManager(location.getWorld());
        ApplicableRegionSet set = regionManager.getApplicableRegions(location);
        for(ProtectedRegion region : set.getRegions()){
            if(regionsInWorld.containsKey(region.getId()) && (highestPriority == null || highestPriority.getPriority() < region.getPriority())){
                highestPriority = region;
                level = regionsInWorld.get(region.getId()).getLevel();
            }
        }

        return level;
    }

    /**
     *
     * @param location - location to check regions for level setting
     * @return -1 if no regions, otherwise returns the highest level set by regions at the location
     */
    public int getHighestLevelAtLocation(Location location){
        int level = 1;
        HashMap<String, LevelRegion> regionsInWorld = allLevelRegions.get(location.getWorld().getName());
        if(regionsInWorld == null)
            return -1; //For invalid

        RegionManager regionManager = WGBukkit.getRegionManager(location.getWorld());
        ApplicableRegionSet set = regionManager.getApplicableRegions(location);
        for(ProtectedRegion region : set.getRegions()){
            if(regionsInWorld.containsKey(region.getId()))
                level = Math.max(level, regionsInWorld.get(region.getId()).getLevel());
        }

        return level;
    }

}
