package me.Lorinth.LRM.Variants;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class InvisibleVariant extends MobVariant{

    public InvisibleVariant(){
        super("Invisible");
    }

    protected void loadDetails(FileConfiguration config){}

    boolean augment(LivingEntity entity) {
        entity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, true));
        return true;
    }
}
