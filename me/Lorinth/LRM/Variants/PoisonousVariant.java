package me.Lorinth.LRM.Variants;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PoisonousVariant extends MobVariant{

    public PoisonousVariant(){
        super("Poisonous");
    }

    @Override
    boolean augment(LivingEntity entity) {
        entity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 0, false));
        return true;
    }

    @Override
    public void onHit(LivingEntity living){
        living.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 3 * 20, 0));
    }
}
