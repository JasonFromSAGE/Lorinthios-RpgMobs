package me.Lorinth.LRM.Variants;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;

public class StrongVariant extends MobVariant{

    public StrongVariant(){
        super("Strong");
    }

    @Override
    boolean augment(LivingEntity entity) {
        AttributeInstance instance = entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        if(instance != null) {
            instance.setBaseValue(instance.getValue() + 2);
            return true;
        }
        return false;
    }
}
