package me.Lorinth.LRM.Variants;

import me.Lorinth.LRM.Objects.ConfigValue;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Random;

public class PoisonousVariant extends MobVariant{

    private static int duration;
    private static int potionLevel;
    private static double chance;
    private static Random random = new Random();

    public PoisonousVariant(){
        super("Poisonous", new ArrayList<ConfigValue>(){{
            add(new ConfigValue<>("Poison.Duration", 3 * 20));
            add(new ConfigValue<>("Poison.Level", 0));
            add(new ConfigValue<>("Poison.Chance", 20.0));
        }});
    }

    @Override
    protected void loadDetails(FileConfiguration config){
        ArrayList<ConfigValue> configValues = getConfigValues();
        duration = (int) configValues.get(0).getValue(config);
        potionLevel = (int) configValues.get(1).getValue(config);
        chance = (double) configValues.get(2).getValue(config);
    }

    @Override
    boolean augment(Entity entity) {
        if(entity instanceof Monster)
            return true;
        return false;
    }

    void removeAugment(Entity entity){

    }

    @Override
    public void onHit(LivingEntity target, EntityDamageByEntityEvent event){
        if(random.nextDouble() * 100 < chance)
            target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, duration, potionLevel));
    }
}
