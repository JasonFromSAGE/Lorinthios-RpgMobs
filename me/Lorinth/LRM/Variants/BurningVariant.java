package me.Lorinth.LRM.Variants;

import me.Lorinth.LRM.LorinthsRpgMobs;
import me.Lorinth.LRM.Objects.ConfigValue;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.WaterMob;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Random;

public class BurningVariant extends MobVariant{

    private static int defensiveBurnTicks;
    private static double defensiveBurnChance;
    private Random random = new Random();

    public BurningVariant(){
        super("Burning", new ArrayList<ConfigValue>(){{
            add(new ConfigValue<>("DefensiveBurn.Ticks", 20));
            add(new ConfigValue<>("DefensiveBurn.Chance", 20.0));
        }});
    }

    @Override
    protected void loadDetails(FileConfiguration config){
        ArrayList<ConfigValue> configValues = getConfigValues();
        defensiveBurnTicks = (int) configValues.get(0).getValue(config);
        defensiveBurnChance = (double) configValues.get(1).getValue(config);
    }

    @Override
    boolean augment(final Entity entity) {
        if(entity instanceof WaterMob)
            return false;
        if(entity instanceof LivingEntity){
            ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 1, true));
            Bukkit.getScheduler().runTaskLater(LorinthsRpgMobs.instance, () -> {
                entity.setFireTicks(Integer.MAX_VALUE);
            }, 10);

            return true;
        }
        return false;
    }

    @Override
    void removeAugment(Entity entity){
        entity.setFireTicks(0);
        if(entity instanceof LivingEntity)
            ((LivingEntity) entity).removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
    }

    @Override
    public void whenHit(LivingEntity entity){
        if(random.nextDouble() * 100 < defensiveBurnChance)
            entity.setFireTicks(defensiveBurnTicks);
    }
}
