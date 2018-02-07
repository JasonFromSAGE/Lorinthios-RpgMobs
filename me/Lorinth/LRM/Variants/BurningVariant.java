package me.Lorinth.LRM.Variants;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.WaterMob;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BurningVariant extends MobVariant{

    public BurningVariant(){
        super("Burning");
    }

    @Override
    boolean augment(LivingEntity entity) {
        if(entity instanceof WaterMob)
            return false;

        entity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 1, false));
        entity.setFireTicks(Integer.MAX_VALUE);
        return true;
    }
}
