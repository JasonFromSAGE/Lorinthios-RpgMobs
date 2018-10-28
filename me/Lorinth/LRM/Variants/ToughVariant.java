package me.Lorinth.LRM.Variants;

import me.Lorinth.LRM.LorinthsRpgMobs;
import me.Lorinth.LRM.Objects.ConfigValue;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class ToughVariant extends MobVariant{

    private static int rawDefense;
    private static double defenseMultiplier;

    public ToughVariant(){
        super("Tough", new ArrayList<ConfigValue>(){{
            add(new ConfigValue<>("Armor", 2));
            add(new ConfigValue<>("ArmorMultiplier", 1.0));
        }});
    }

    @Override
    protected void loadDetails(FileConfiguration config){
        ArrayList<ConfigValue> configValues = getConfigValues();
        rawDefense = (int) configValues.get(0).getValue(config);
        defenseMultiplier = (double) configValues.get(1).getValue(config);
    }

    @Override
    boolean augment(Entity entity) {
        if(entity instanceof LivingEntity) {
            LivingEntity living = (LivingEntity) entity;
            if(LorinthsRpgMobs.properties.IsAttributeVersion){
                AttributeInstance instance = living.getAttribute(Attribute.GENERIC_ARMOR);
                if (instance != null) {
                    instance.setBaseValue(instance.getValue() * defenseMultiplier + rawDefense);
                    return true;
                }
            }
            else
                living.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1), true);
        }
        return false;
    }

    @Override
    void removeAugment(Entity entity){
        if(!(entity instanceof LivingEntity))
            return;

        LivingEntity living = (LivingEntity) entity;
        if(LorinthsRpgMobs.properties.IsAttributeVersion){
            AttributeInstance instance = living.getAttribute(Attribute.GENERIC_ARMOR);
            if (instance != null) {
                instance.setBaseValue((instance.getValue() - rawDefense) * (1 / defenseMultiplier));
            }
        }
        else
            living.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
    }
}
