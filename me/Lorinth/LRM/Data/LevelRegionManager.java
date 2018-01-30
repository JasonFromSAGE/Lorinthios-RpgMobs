package me.Lorinth.LRM.Data;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.Lorinth.LRM.Objects.LevelRegion;
import me.Lorinth.LRM.Objects.OutputHandler;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by lorinthio on 1/28/2018.
 */
public class LevelRegionManager {

    public boolean WorldGuardFound = false;
    private HashMap<String, HashMap<String, LevelRegion>> allLevelRegions = new HashMap<>();

    public LevelRegionManager(){
        try{
            WorldGuardFound = WGBukkit.getPlugin() != null;
        }
        catch(NoClassDefFoundError exception){
            WorldGuardFound = false;
        }
    }

    protected boolean saveData(FileConfiguration config){
        boolean changed = false;
        for(String worldName : allLevelRegions.keySet()){
            for(LevelRegion region : allLevelRegions.get(worldName).values()){
                changed = region.save(config, "LevelRegions." + worldName + ".");
            }
        }
        return changed;
    }

    protected void loadAllRegions(FileConfiguration config){
        if(config.contains("LevelRegions")){
            for(String world : config.getConfigurationSection("LevelRegions").getKeys(false)){
                allLevelRegions.put(world, new HashMap<>());

                for(String key : config.getConfigurationSection("LevelRegions." + world).getKeys(false)){
                    allLevelRegions.get(world).put(key, loadRegion(config, key, "LevelRegions." + world));
                }
            }
        }
    }

    private LevelRegion loadRegion(FileConfiguration config, String name, String prefix){
        return new LevelRegion(config, name, prefix);
    }

    public void addLevelRegionToWorld(World world, LevelRegion region){
        if(!allLevelRegions.containsKey(world.getName())){
            allLevelRegions.put(world.getName(), new HashMap<>());
        }

        allLevelRegions.get(world.getName()).put(region.getName(), region);
    }

    /**
     * Gets all level region in a given world, with a given name
     * @param world - world to check
     * @return ArrayList<LevelRegion>
     */
    public ArrayList<LevelRegion> getAllLeveledRegionsInWorld(World world){
        if(!WorldGuardFound)
            return new ArrayList<>();

        ArrayList<LevelRegion> regionsInWorld = new ArrayList<>();
        if(allLevelRegions.containsKey(world.getName()))
            regionsInWorld.addAll(allLevelRegions.get(world.getName()).values());

        return regionsInWorld;
    }

    /**
     * Gets a level region in a given world, with a given name
     * @param world - world to check
     * @param name - name of region
     * @return LevelRegion
     */
    public LevelRegion getLevelRegionByName(World world, String name){
        name = name.toLowerCase();
        if(!WorldGuardFound)
            return null;

        if(allLevelRegions.containsKey(world.getName()))
            if(allLevelRegions.get(world.getName()).containsKey(name))
                return allLevelRegions.get(world.getName()).get(name);

        return null;
    }

    /**
     * Gets the highest priority leveled region at a location
     * @param location the location to check
     * @return LevelRegion
     */
    public LevelRegion getHighestPriorityLeveledRegionAtLocation(Location location){
        if(!WorldGuardFound)
            return null;

        ProtectedRegion highestPriority = null;
        LevelRegion highestLevelRegion = null;
        HashMap<String, LevelRegion> regionsInWorld = allLevelRegions.get(location.getWorld().getName());
        if(regionsInWorld == null || regionsInWorld.size() == 0)
            return null;

        RegionManager regionManager = WGBukkit.getRegionManager(location.getWorld());
        ApplicableRegionSet set = regionManager.getApplicableRegions(location);
        for(ProtectedRegion region : set.getRegions()){
            if(regionsInWorld.containsKey(region.getId()) && (highestPriority == null || highestLevelRegion == null || (highestPriority.getPriority() < region.getPriority()) )){
                highestPriority = region;
                LevelRegion levelRegion = regionsInWorld.get(region.getId());
                if(levelRegion != null && !levelRegion.isDisabled()) {
                    highestPriority = region;
                    highestLevelRegion = levelRegion;
                }
            }
        }

        return highestLevelRegion;
    }

    /**
     *
     * @param location - location to check regions for level setting
     * @return -1 if no regions, otherwise returns the lowest level set by regions at the location
     */
    public int getLowestLevelAtLocation(Location location){
        if(!WorldGuardFound)
            return -1;

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
     *
     * @param location - location to check regions for level setting
     * @return -1 if no regions, otherwise returns the highest level set by regions at the location
     */
    public int getHighestLevelAtLocation(Location location){
        if(!WorldGuardFound)
            return -1;

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

    /**
     * Matches world guard concepts in terms of higher priority = setting for area
     * @param location - location to check level
     * @return -1 if no regions, otherwise returns the level setting of the highest priority region
     */
    public int getHighestPriorityLevelAtLocation(Location location){
        if(!WorldGuardFound)
            return -1;

        LevelRegion highestPriority = getHighestPriorityLeveledRegionAtLocation(location);
        if(highestPriority != null)
            return highestPriority.getLevel();

        return -1;
    }



}
