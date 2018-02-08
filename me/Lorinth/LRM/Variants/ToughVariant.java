package me.Lorinth.LRM.Variants;

import me.Lorinth.LRM.Objects.ConfigValue;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;

public class ToughVariant extends MobVariant{

    private static int rawDefense;
    private static double defenseMultiplier;

    public ToughVariant(){
        super("Tough", new ArrayList<ConfigValue>(){{
            add(new ConfigValue<>("Armor", 2));
            add(new ConfigValue<>("ArmorMultiplier", 1.0));
        }});
    }

    @Override
    protected void loadDetails(FileConfiguration config){
        ArrayList<ConfigValue> configValues = getConfigValues();
        rawDefense = (int) configValues.get(0).getValue(config);
        defenseMultiplier = (double) configValues.get(1).getValue(config);
    }

    @Override
    boolean augment(Entity entity) {
        if(entity instanceof LivingEntity) {
            AttributeInstance instance = ((LivingEntity) entity).getAttribute(Attribute.GENERIC_ARMOR);
            if (instance != null) {
                instance.setBaseValue(instance.getValue() * defenseMultiplier + rawDefense);
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
            instance.setBaseValue(instance.getValue() * (1 / defenseMultiplier) - rawDefense);
        }
    }
}
