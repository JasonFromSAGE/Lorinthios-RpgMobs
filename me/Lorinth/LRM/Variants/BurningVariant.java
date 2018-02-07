package me.Lorinth.LRM.Variants;

import me.Lorinth.LRM.Objects.ConfigValue;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.WaterMob;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Random;

public class BurningVariant extends MobVariant{

    private int defensiveBurnTicks = 20;
    private double defensiveBurnChance = 100;
    private Random random = new Random();

    public BurningVariant(){
        super("Burning", new ArrayList<ConfigValue>(){{
            add(new ConfigValue<>("DefensiveBurn.Ticks", 20));
            add(new ConfigValue<>("DefensiveBurn.Chance", 100.0));
        }});
    }

    protected void loadDetails(FileConfiguration config){
        ArrayList<ConfigValue> configValues = getConfigValues();
        defensiveBurnTicks = (int) configValues.get(0).getValue(config);
        defensiveBurnChance = (double) configValues.get(1).getValue(config);
    }

    private void setDefensiveBurnTicks(int defensiveBurnTicks){
        this.defensiveBurnTicks = defensiveBurnTicks;
    }

    boolean augment(LivingEntity entity) {
        if(entity instanceof WaterMob)
            return false;

        entity.setFireTicks(entity.getMaxFireTicks());
        entity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 1, false));

        return true;
    }

    @Override
    public void whenHit(LivingEntity entity){
        if(random.nextDouble() * 100 < defensiveBurnChance)
            entity.setFireTicks(defensiveBurnTicks);
    }
}
