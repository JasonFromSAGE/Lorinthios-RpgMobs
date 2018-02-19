package me.Lorinth.LRM.Variants;

import me.Lorinth.LRM.Objects.ConfigValue;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;

public class SturdyVariant extends MobVariant {

    private double knockbackResistance;

    public SturdyVariant(){
        super("Sturdy", new ArrayList<ConfigValue>(){{
            add(new ConfigValue("KnockbackResistance", 1.0));
        }});
    }

    @Override
    protected void loadDetails(FileConfiguration config) {
        ArrayList<ConfigValue> configValues = getConfigValues();
        knockbackResistance = (double) configValues.get(0).getValue(config);
    }

    @Override
    boolean augment(Entity entity) {
        if(entity instanceof LivingEntity) {
            LivingEntity living = (LivingEntity) entity;
            AttributeInstance instance = living.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
            if (instance != null) {
                instance.setBaseValue(knockbackResistance);
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
        AttributeInstance instance = living.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
        if (instance != null) {
            instance.setBaseValue(instance.getDefaultValue());
        }
    }
}
