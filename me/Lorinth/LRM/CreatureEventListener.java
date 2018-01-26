package me.Lorinth.LRM;

import me.Lorinth.LRM.Objects.CreatureData;
import me.Lorinth.LRM.Objects.NameOptions;
import org.bukkit.Material;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Creature;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * Created by lorinthio on 1/24/2018.
 */
public class CreatureEventListener implements Listener {

    private DataLoader dataLoader;

    public CreatureEventListener(DataLoader dataLoader){
        this.dataLoader = dataLoader;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onCreatureSpawn(CreatureSpawnEvent event){
        if(event.getEntity() instanceof Creature){
            Creature entity = (Creature) event.getEntity();

            CreatureData data = dataLoader.getData(entity);
            if(data.isDisabled())
                return;

            //Set Level
            int level = dataLoader.calculateLevel(entity.getLocation());
            entity.setMetadata("Level", new FixedMetadataValue(LorinthsRpgMobs.instance, level));

            int health = (int)data.getHealthAtLevel(level);
            entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue((double) health);
            entity.setHealth((double) health);

            //Set Name
            NameOptions nameOptions = dataLoader.getNameOptions();
            entity.setCustomNameVisible(nameOptions.getTagsAlwaysOn());
            entity.setCustomName(data.getNameAtLevel(nameOptions.getNameFormat(), level));

            entity.setRemoveWhenFarAway(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onCreatureAttack(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Creature){
            Creature creature = (Creature) event.getDamager();

            CreatureData data = dataLoader.getData(creature);
            if (data.isDisabled())
                return;

            int level = LorinthsRpgMobs.GetLevelOfCreature(creature);
            event.setDamage(data.getDamageAtLevel(level));
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onTame(EntityTameEvent event){
        event.getEntity().setRemoveWhenFarAway(false);
    }

    @EventHandler(ignoreCancelled = true)
    public void onCreatureDeath(EntityDeathEvent event){
        if(event.getEntity() instanceof Creature) {
            Creature creature = (Creature) event.getEntity();

            CreatureData data = dataLoader.getData(creature);
            if (data.isDisabled())
                return;

            int level = LorinthsRpgMobs.GetLevelOfCreature(creature);
            event.setDroppedExp(data.getExperienceAtLevel(level));
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        event.getBlock().setType(Material.SKULL);
    }
}
