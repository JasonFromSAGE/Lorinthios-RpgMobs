package me.Lorinth.LRM.Variants;

import me.Lorinth.LRM.Events.RpgMobDeathEvent;
import me.Lorinth.LRM.Objects.ConfigValue;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;

public class WealthyVariant extends MobVariant {

    private double currencyValueMutiplier;

    public WealthyVariant(){
        super("Wealthy", new ArrayList<ConfigValue>(){{
            add(new ConfigValue("CurrencyValueMutiplier", 5.0));
        }});
    }

    @Override
    protected void loadDetails(FileConfiguration config) {
        ArrayList<ConfigValue> configValues = getConfigValues();
        currencyValueMutiplier = (double) configValues.get(0).getValue(config);
    }

    @Override
    boolean augment(Entity entity) {
        if(!(entity instanceof LivingEntity))
            return false;

        return true;
    }

    @Override
    void removeAugment(Entity entity) {

    }

    @Override
    public void onDeathEvent(RpgMobDeathEvent event) {
        event.setCurrencyValue(event.getCurrencyValue() * currencyValueMutiplier);
    }
}
