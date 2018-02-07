package me.Lorinth.LRM.Variants;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class InvisibleVariant extends MobVariant{

    public InvisibleVariant(){
        super("Invisible");
    }

    @Override
    boolean augment(LivingEntity entity) {
        entity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, true));
        return true;
    }
}
