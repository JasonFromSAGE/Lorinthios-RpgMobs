package me.Lorinth.LRM.Variants;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;

public class ToughVariant extends MobVariant{

    public ToughVariant(){
        super("Tough");
    }

    @Override
    boolean augment(LivingEntity entity) {
        AttributeInstance instance = entity.getAttribute(Attribute.GENERIC_ARMOR);
        if(instance != null) {
            instance.setBaseValue(instance.getValue() + 2);
            return true;
        }
        return false;
    }
}
