package me.Lorinth.LRM.Variants;

import me.Lorinth.LRM.Objects.ConfigValue;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;

public class ToughVariant extends MobVariant{

    private int rawDefense = 2;
    private double defenseMultiplier = 1.0;

    public ToughVariant(){
        super("Tough", new ArrayList<ConfigValue>(){{
            add(new ConfigValue<>("Armor", 2));
            add(new ConfigValue<>("ArmorMultiplier", 1.0));
        }});
    }

    protected void loadDetails(FileConfiguration config){
        ArrayList<ConfigValue> configValues = getConfigValues();
        rawDefense = (int) configValues.get(0).getValue(config);
        defenseMultiplier = (double) configValues.get(1).getValue(config);
    }

    private void setRawDefenseBonus(int rawDefenseBonus){
        rawDefense = rawDefenseBonus;
    }

    private void setDefenseMultiplier(double multiplier){
        defenseMultiplier = multiplier;
    }

    @Override
    boolean augment(LivingEntity entity) {
        AttributeInstance instance = entity.getAttribute(Attribute.GENERIC_ARMOR);
        if(instance != null) {
            instance.setBaseValue(instance.getValue() * defenseMultiplier + rawDefense);
            return true;
        }
        return false;
    }
}
