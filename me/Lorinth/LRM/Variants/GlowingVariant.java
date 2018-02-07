package me.Lorinth.LRM.Variants;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GlowingVariant extends MobVariant{

    public GlowingVariant(){
        super("Glowing");
    }

    @Override
    boolean augment(LivingEntity entity) {
        entity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 0, false));
        return true;
    }
}