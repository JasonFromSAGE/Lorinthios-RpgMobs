package me.Lorinth.LRM.Variants;

import me.Lorinth.LRM.Objects.ConfigValue;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;

public class SlowVariant extends MobVariant{

    private double speedMultiplier = 0.5;

    public SlowVariant(){
        super("Slow", new ArrayList<ConfigValue>(){{ add(new ConfigValue<>("SpeedMultiplier", 0.5)); }});
    }

    protected void loadDetails(FileConfiguration config){
        ArrayList<ConfigValue> configValues = getConfigValues();
        speedMultiplier = (double) configValues.get(0).getValue(config);
    }

    private void setSpeedMultiplier(double multiplier){
        speedMultiplier = multiplier;
    }

    @Override
    boolean augment(LivingEntity entity) {
        AttributeInstance instance = entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        if(instance != null) {
            instance.setBaseValue(instance.getValue() * speedMultiplier);
            return true;
        }
        return false;
    }
}
