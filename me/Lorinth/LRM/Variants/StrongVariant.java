package me.Lorinth.LRM.Variants;

import me.Lorinth.LRM.Objects.ConfigValue;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;

public class StrongVariant extends MobVariant{

    private static int rawDamage;
    private static double damageMultiplier;

    public StrongVariant(){
        super("Strong", new ArrayList<ConfigValue>(){{
            add(new ConfigValue<>("Damage", 2));
            add(new ConfigValue<>("DamageMultiplier", 1.0));
        }});
    }

    @Override
    protected void loadDetails(FileConfiguration config){
        ArrayList<ConfigValue> configValues = getConfigValues();
        rawDamage = (int) configValues.get(0).getValue(config);
        damageMultiplier = (double) configValues.get(1).getValue(config);
    }

    @Override
    boolean augment(Entity entity) {
        if(entity instanceof LivingEntity) {
            AttributeInstance instance = ((LivingEntity) entity).getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
            if (instance != null) {
                instance.setBaseValue(instance.getValue() * damageMultiplier + rawDamage);
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
        AttributeInstance instance = living.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        if (instance != null) {
            instance.setBaseValue(instance.getValue() * (1 / damageMultiplier) - rawDamage);
        }
    }
}
