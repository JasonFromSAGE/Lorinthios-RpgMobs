package me.Lorinth.LRM.Variants;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;

public class FastVariant extends MobVariant{

    public FastVariant(){
        super("Fast");
    }

    @Override
    boolean augment(LivingEntity entity) {
        AttributeInstance instance = entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        if(instance != null) {
            instance.setBaseValue(instance.getValue() * 1.5);
            return true;
        }
        return false;
    }
}
