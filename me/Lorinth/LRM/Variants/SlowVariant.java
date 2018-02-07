package me.Lorinth.LRM.Variants;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;

public class SlowVariant extends MobVariant{

    public SlowVariant(){
        super("Slow");
    }

    @Override
    boolean augment(LivingEntity entity) {
        AttributeInstance instance = entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        if(instance != null) {
            instance.setBaseValue(instance.getValue() * 0.5);
            return true;
        }
        return false;
    }
}
