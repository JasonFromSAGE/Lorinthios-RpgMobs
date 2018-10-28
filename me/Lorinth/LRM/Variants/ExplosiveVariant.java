package me.Lorinth.LRM.Variants;

import me.Lorinth.LRM.Objects.ConfigValue;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;

public class ExplosiveVariant extends MobVariant{

    private boolean breakBlocks;
    private boolean setFire;
    private double power;

    public ExplosiveVariant(){
        super("Explosive", new ArrayList<ConfigValue>(){{
            add(new ConfigValue("BreakBlocks", false));
            add(new ConfigValue("IgniteBlocks", false));
            add(new ConfigValue("Power", 4.0));
        }});
    }


    @Override
    protected void loadDetails(FileConfiguration config) {
        ArrayList<ConfigValue> configValues = getConfigValues();
        breakBlocks = (boolean) configValues.get(0).getValue(config);
        setFire = (boolean) configValues.get(1).getValue(config);
        power = (double) configValues.get(2).getValue(config);
    }

    @Override
    boolean augment(Entity entity) {
        if(!(entity instanceof LivingEntity))
            return false;

        return true;
    }

    @Override
    void removeAugment(Entity entity) {

    }

    @Override
    public void onDeath(LivingEntity entity){
        Location loc = entity.getLocation();
        entity.getWorld().createExplosion(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), (float) power, setFire, breakBlocks);
    }
}
