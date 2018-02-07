package me.Lorinth.LRM.Variants;

import me.Lorinth.LRM.Data.MobVariantDataManager;
import me.Lorinth.LRM.LorinthsRpgMobs;
import me.Lorinth.LRM.Objects.ConfigValue;
import me.Lorinth.LRM.Objects.Disableable;
import me.Lorinth.LRM.Util.ConfigHelper;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
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

    protected String getPrefix(){
        return prefix + "." + name;
    }

    public int getWeight(){
        return weight;
    }

    public void load(FileConfiguration config, Plugin plugin, String prefix){
        prefix += "." + name;
        //Check for config defaults
        if(!ConfigHelper.ConfigContainsPath(config, prefix + ".Disabled")){
            setDefaults(config, plugin, prefix);
        }

        setDisabled(config.getBoolean(prefix + ".Disabled"));
        if(config.getConfigurationSection(prefix).getKeys(false).contains("DisabledTypes")){
            disabledEntityTypes.addAll(config.getStringList(prefix + ".DisabledTypes"));
        }

        if(!isDisabled()) {
            weight = config.getInt(prefix + ".Weight");
            loadDetails(config);
            MobVariantDataManager.AddVariant(this);
        }
    }

    protected ArrayList<ConfigValue> getConfigValues(){
        return configValues;
    }

    protected abstract void loadDetails(FileConfiguration config);

    /**
     * Applies the variant to the entity
     * @param entity - the entity to apply the variant to
     */
    public boolean apply(LivingEntity entity){
        if(name == "Normal")
            return true;
        if(isDisabled() || LorinthsRpgMobs.IsMythicMob(entity) || disabledEntityTypes.contains(entity.getType().name()))
            return false;

        if(augment(entity)) {
            entity.setCustomName(entity.getCustomName().replace("{Variant}", getName()).replace("{variant}", getName().toLowerCase()));
            entity.setMetadata("Lrm.MobVariant", new FixedMetadataValue(LorinthsRpgMobs.instance, this));
            return true;
        }
        return false;
    }

    /**
     * Use apply(LivingEntity) instead
     * @param entity
     * @return - if the variant took effect
     */
    abstract boolean augment(LivingEntity entity);

    public void onHit(LivingEntity target){
        return;
    }

    public void whenHit(LivingEntity attacker){

    }

    public String getName(){
        return this.name;
    }

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
