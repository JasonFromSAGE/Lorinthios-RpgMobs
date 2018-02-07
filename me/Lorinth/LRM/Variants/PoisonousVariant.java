package me.Lorinth.LRM.Variants;

import me.Lorinth.LRM.Objects.ConfigValue;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class PoisonousVariant extends MobVariant{

    private int duration = 3 * 20;
    private int potionLevel = 0;

    public PoisonousVariant(){
        super("Poisonous", new ArrayList<ConfigValue>(){{
            add(new ConfigValue<>("Poison.Duration", 3 * 20));
            add(new ConfigValue<>("Poison.Level", 0));
            add(new ConfigValue<>("Poison.Chance", 50));
        }});
    }

    protected void loadDetails(FileConfiguration config){
        ArrayList<ConfigValue> configValues = getConfigValues();
        duration = (int) configValues.get(0).getValue(config);
        potionLevel = (int) configValues.get(1).getValue(config);
    }

    private void setPoisionDuration(int duration){
        this.duration = duration;
    }

    private void setPoisonLevel(int level){
        potionLevel = level;
    }

    @Override
    boolean augment(LivingEntity entity) {
        if(entity instanceof Monster)
            return true;
        return false;
    }

    @Override
    public void onHit(LivingEntity target){
        target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, duration, potionLevel));
    }
}
