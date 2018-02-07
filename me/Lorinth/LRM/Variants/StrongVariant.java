package me.Lorinth.LRM.Variants;

import me.Lorinth.LRM.Objects.ConfigValue;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;

public class StrongVariant extends MobVariant{

    private int rawDamage = 2;
    private double damageMultiplier = 1.0;

    public StrongVariant(){
        super("Strong", new ArrayList<ConfigValue>(){{
            add(new ConfigValue<>("Damage", 2));
            add(new ConfigValue<>("DamageMultiplier", 1.0));
        }});
    }

    protected void loadDetails(FileConfiguration config){
        ArrayList<ConfigValue> configValues = getConfigValues();
        rawDamage = (int) configValues.get(0).getValue(config);
        damageMultiplier = (double) configValues.get(1).getValue(config);
    }

    private void setRawDamageBonus(int rawDamageBonus){
        rawDamage = rawDamageBonus;
    }

    private void setDamageMultiplier(double multiplier){
        damageMultiplier = multiplier;
    }

    @Override
    boolean augment(LivingEntity entity) {
        AttributeInstance instance = entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        if(instance != null) {
            instance.setBaseValue(instance.getValue() * damageMultiplier + rawDamage);
            return true;
        }
        return false;
    }
}
