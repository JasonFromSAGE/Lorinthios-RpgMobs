package me.Lorinth.LRM.Variants;

import me.Lorinth.LRM.Data.MobVariantDataManager;
import me.Lorinth.LRM.LorinthsRpgMobs;
import me.Lorinth.LRM.Objects.ConfigValue;
import me.Lorinth.LRM.Objects.Disableable;
import me.Lorinth.LRM.Util.ConfigHelper;
import me.Lorinth.LRM.Util.MetaDataConstants;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public abstract class MobVariant extends Disableable{

    private String name;
    private int weight;
    private ArrayList<String> disabledEntityTypes = new ArrayList<>();
    private ArrayList<ConfigValue> configValues = new ArrayList<>();
    private String prefix = "MobVariants";

    public MobVariant(String name){
        this.name = name;
        load(LorinthsRpgMobs.instance.getConfig(), LorinthsRpgMobs.instance, prefix);
    }

    public MobVariant(String name, ArrayList<ConfigValue> configValues){
        this.name = name;
        this.configValues = configValues;
        for(ConfigValue configValue : configValues)
            configValue.setPath(prefix + "." + name + "." + configValue.getPath());
        load(LorinthsRpgMobs.instance.getConfig(), LorinthsRpgMobs.instance, prefix);
    }

    public void load(FileConfiguration config, Plugin plugin, String prefix){
        prefix += "." + name;
        //Check for config defaults
        if(!ConfigHelper.ConfigContainsPath(config, prefix + ".Disabled"))
            setDefaults(config, plugin, prefix);

        setDisabled(config.getBoolean(prefix + ".Disabled"));
        if(config.getConfigurationSection(prefix).getKeys(false).contains("DisabledTypes"))
            disabledEntityTypes.addAll(config.getStringList(prefix + ".DisabledTypes"));

        if(!isDisabled()) {
            weight = config.getInt(prefix + ".Weight");
            loadDetails(config);
            MobVariantDataManager.AddVariant(this);
        }
    }

    public void onHit(LivingEntity target, EntityDamageByEntityEvent event){}

    public void whenHit(LivingEntity attacker, EntityDamageByEntityEvent event){}

    public void onSpawn(LivingEntity entity){}

    public void onDeath(LivingEntity entity){}

    public String getName(){
        return this.name;
    }

    public int getWeight(){
        return weight;
    }

    protected String getPrefix(){
        return prefix + "." + name;
    }

    protected ArrayList<ConfigValue> getConfigValues(){
        return configValues;
    }

    protected abstract void loadDetails(FileConfiguration config);

    public boolean isDisabledEntityType(EntityType type){
        return disabledEntityTypes.contains(type.name());
    }

    /**
     * Applies the variant to the entity
     * @param entity - the entity to apply the variant to
     */
    public boolean apply(Entity entity){
        if(isDisabled() || LorinthsRpgMobs.IsMythicMob(entity) || disabledEntityTypes.contains(entity.getType().name()))
            return false;

        if(augment(entity)) {
            if(entity != null && getName() != null && entity.getCustomName() != null)
                entity.setCustomName(entity.getCustomName().replace("{Variant}", getName()).replace("{variant}", getName().toLowerCase()));
            entity.setMetadata(MetaDataConstants.Variant, new FixedMetadataValue(LorinthsRpgMobs.instance, this));
            if(entity instanceof LivingEntity)
                onSpawn((LivingEntity) entity);
            return true;
        }
        return false;
    }

    public void remove(Entity entity){
        removeAugment(entity);
        entity.removeMetadata(MetaDataConstants.Variant, LorinthsRpgMobs.instance);
    }

    /**
     * Use apply(LivingEntity) instead
     * @param entity
     * @return - if the variant took effect
     */
    abstract boolean augment(Entity entity);

    abstract void removeAugment(Entity entity);

    private void setDefaults(FileConfiguration config, Plugin plugin, String prefix){
        config.set(prefix + ".Disabled", false);
        config.set(prefix + ".DisabledTypes", new ArrayList<>());
        config.set(prefix + ".Weight", 10);
        for(ConfigValue configValue : configValues){
            configValue.setDefault(config);
        }
        plugin.saveConfig();
    }

}
