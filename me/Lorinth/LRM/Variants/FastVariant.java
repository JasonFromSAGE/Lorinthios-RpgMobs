package me.Lorinth.LRM.Variants;

import me.Lorinth.LRM.Objects.ConfigValue;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;

public class FastVariant extends MobVariant{

    private static double speedMultiplier;

    public FastVariant(){
        super("Fast", new ArrayList<ConfigValue>(){{ add(new ConfigValue<>("SpeedMultiplier", 1.5)); }});
    }

    @Override
    protected void loadDetails(FileConfiguration config){
        ArrayList<ConfigValue> configValues = getConfigValues();
        speedMultiplier = (double) configValues.get(0).getValue(config);
    }

    @Override
    boolean augment(Entity entity) {
        if(entity instanceof LivingEntity) {
            LivingEntity living = (LivingEntity) entity;
            AttributeInstance instance = living.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
            if (instance != null) {
                instance.setBaseValue(instance.getValue() * speedMultiplier);
                return true;
            }
        }
        return false;
    }

    @Override
    void removeAugment(Entity entity){
        if(!(entity instanceof LivingEntity))
            return;

        LivingEntity living = (LivingEntity) entity;
        AttributeInstance instance = living.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        if (instance != null) {
            instance.setBaseValue(instance.getDefaultValue());
        }
    }
}
