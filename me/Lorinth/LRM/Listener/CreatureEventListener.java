package me.Lorinth.LRM.Listener;

import com.herocraftonline.heroes.api.events.ExperienceChangeEvent;
import com.herocraftonline.heroes.api.events.HeroKillCharacterEvent;
import com.herocraftonline.heroes.characters.classes.HeroClass;
import com.sucy.skill.api.skills.Skill;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
import me.Lorinth.LRM.Data.DataLoader;
import me.Lorinth.LRM.Data.HeroesDataManager;
import me.Lorinth.LRM.Data.MobVariantDataManager;
import me.Lorinth.LRM.Data.SkillAPIDataManager;
import me.Lorinth.LRM.LorinthsRpgMobs;
import me.Lorinth.LRM.Objects.*;
import me.Lorinth.LRM.Util.MetaDataConstants;
import me.Lorinth.LRM.Util.OutputHandler;
import me.Lorinth.LRM.Variants.MobVariant;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

/**
 * Controls creature data and defaults
 */
public class CreatureEventListener implements Listener {

    private DataLoader dataLoader;

    public CreatureEventListener(DataLoader dataLoader){
        this.dataLoader = dataLoader;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onCreatureSpawn(CreatureSpawnEvent event){
        LivingEntity entity = event.getEntity();
        CreatureData data = dataLoader.getCreatureDataManager().getData(entity);
        if(data.isDisabled(entity.getWorld().getName()))
            return;

        //Set Level
        int level = dataLoader.calculateLevel(entity.getLocation());
        entity.setMetadata(MetaDataConstants.Level, new FixedMetadataValue(LorinthsRpgMobs.instance, level));

        setHealth(entity, data, level);
        setDamage(entity, data, level);
        setEquipment(entity, data, level);
        setName(entity, data, level);
        setVariant(entity);
    }

    private void setHealth(LivingEntity entity, CreatureData data, int level){
        int health = (int)data.getHealthAtLevel(level);
        AttributeInstance attribute = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if(attribute != null)
            attribute.setBaseValue((double) health);
        else
            entity.setMaxHealth(health);
        entity.setHealth((double) health);
    }

    private void setDamage(LivingEntity entity, CreatureData data, int level){
        double damage = (int)data.getDamageAtLevel(level);
        AttributeInstance attribute = entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        if(attribute != null)
            attribute.setBaseValue(damage);
        entity.setMetadata(MetaDataConstants.Damage, new FixedMetadataValue(LorinthsRpgMobs.instance, damage));
    }

    private void setEquipment(LivingEntity entity, CreatureData data, int level){
        EquipmentData equipmentData = data.getEquipmentData();
        if(equipmentData != null)
            equipmentData.equip(entity, level);
    }

    private void setName(LivingEntity entity, CreatureData data, int level){
        LevelRegion region = LorinthsRpgMobs.GetLevelRegionManager().getHighestPriorityLeveledRegionAtLocation(entity.getLocation());
        NameData regionNameData = region != null ? region.getEntityName(entity.getType()) : null;
        NameOptions nameOptions = dataLoader.getNameOptions();
        entity.setCustomNameVisible(nameOptions.getTagsAlwaysOn());
        String name = data.getNameAtLevel(nameOptions.getNameFormat(), regionNameData, level);
        if(name != null)
            entity.setCustomName(name);

        //Allows creatures with custom names to be removed
        entity.setRemoveWhenFarAway(true);
    }

    private void setVariant(LivingEntity entity){
        MobVariantDataManager.GetVariant(entity);

        String name = entity.getCustomName();
        ArrayList<String> removeTags = new ArrayList<String>(){{
            add("{variant} ");
            add("{Variant} ");
            add("{variant}");
            add("{Variant}");
        }};
        for(String tag : removeTags)
            name = name.replace(tag, "");

        entity.setCustomName(name);
    }

    @EventHandler(ignoreCancelled = true)
    public void onTame(EntityTameEvent event){
        event.getEntity().setRemoveWhenFarAway(false);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityHit(EntityDamageByEntityEvent event){
        if(event.getEntity() instanceof LivingEntity){
            LivingEntity target = (LivingEntity) event.getEntity();

            MobVariant variant = LorinthsRpgMobs.GetMobVariantOfEntity(event.getDamager());
            if(variant != null)
                variant.onHit(target);
        }
        if(event.getDamager() instanceof LivingEntity){
            LivingEntity damager = (LivingEntity) event.getDamager();

            MobVariant variant = LorinthsRpgMobs.GetMobVariantOfEntity(event.getEntity());
            if(variant != null)
                variant.whenHit(damager);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onCreatureDeath(EntityDeathEvent event){
        LivingEntity entity = event.getEntity();
        if(event.getDroppedExp() == 0 || entity.getKiller() == null){
            return;
        }

        CreatureData data = dataLoader.getCreatureDataManager().getData(entity);
        if (data.isDisabled(entity.getWorld().getName()))
            return;

        int level = LorinthsRpgMobs.GetLevelOfEntity(entity);
        int exp = data.getExperienceAtLevel(level);

        if(exp > 0){
            Player player = entity.getKiller();
            HeroesDataManager heroesManager = dataLoader.getHeroesDataManager();
            SkillAPIDataManager skillAPIDataManager = dataLoader.getSkillAPIDataManager();
            if(!heroesManager.handleEntityDeathEvent(event, player, exp))
                event.setDroppedExp(0);
            else if(!skillAPIDataManager.handleEntityDeathEvent(event, player, exp))
                event.setDroppedExp(0);
            else
                event.setDroppedExp(exp);
        }
    }
}
