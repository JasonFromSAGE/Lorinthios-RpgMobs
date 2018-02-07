package me.Lorinth.LRM.Variants;

import me.Lorinth.LRM.LorinthsRpgMobs;
import me.Lorinth.LRM.Objects.Disableable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;

public abstract class MobVariant extends Disableable{

    private String name;

    public MobVariant(String name){
        this.name = name;
    }

    public void apply(LivingEntity entity){
        if(isDisabled() || LorinthsRpgMobs.IsMythicMob(entity))
            return;

        if(augment(entity)) {
            entity.setCustomName(entity.getCustomName().replace("{Variant}", getName()).replace("{variant}", getName().toLowerCase()));
            entity.setMetadata("MobVariant", new FixedMetadataValue(LorinthsRpgMobs.instance, this));
        }
    }

    /**
     * Applies the variant to the entity
     * @param entity
     * @return - if the variant took effect
     */
    abstract boolean augment(LivingEntity entity);

    public void onHit(LivingEntity living){

    }

    public String getName(){
        return this.name;
    }

}
