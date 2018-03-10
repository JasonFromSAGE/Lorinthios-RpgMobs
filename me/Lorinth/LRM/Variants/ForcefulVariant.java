package me.Lorinth.LRM.Variants;

import me.Lorinth.LRM.Objects.ConfigValue;
import me.Lorinth.LRM.Util.VectorHelper;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;

public class ForcefulVariant extends MobVariant{

    private static double forceMultiplier;

    public ForcefulVariant(){
        super("Forceful", new ArrayList<ConfigValue>(){{ add(new ConfigValue<>("ForceMultiplier", 2.0)); }});
    }

    @Override
    protected void loadDetails(FileConfiguration config){
        ArrayList<ConfigValue> configValues = getConfigValues();
        forceMultiplier = (double) configValues.get(0).getValue(config);
    }

    @Override
    boolean augment(Entity entity) {
        if(!(entity instanceof LivingEntity))
            return false;

        return true;
    }

    @Override
    void removeAugment(Entity entity){
        return;
    }

    @Override
    public void onHit(LivingEntity target, EntityDamageByEntityEvent event){
        Entity entity = event.getDamager();
        target.getVelocity().add(VectorHelper.getNormalizedVector(entity.getLocation(), target.getLocation()).multiply(forceMultiplier));
    }

}
