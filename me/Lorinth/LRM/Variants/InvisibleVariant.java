package me.Lorinth.LRM.Variants;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class InvisibleVariant extends MobVariant{

    public InvisibleVariant(){
        super("Invisible");
    }

    protected void loadDetails(FileConfiguration config){}

    @Override
    boolean augment(Entity entity) {
        if(entity instanceof LivingEntity) {
            ((LivingEntity)entity).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false));
            return true;
        }
        return false;
    }

    @Override
    void removeAugment(Entity entity){
        if(entity instanceof LivingEntity)
            ((LivingEntity)entity).removePotionEffect(PotionEffectType.INVISIBILITY);
    }
}
