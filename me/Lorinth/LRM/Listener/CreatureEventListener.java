package me.Lorinth.LRM.Listener;

import com.herocraftonline.heroes.api.events.ExperienceChangeEvent;
import com.herocraftonline.heroes.api.events.HeroKillCharacterEvent;
import com.herocraftonline.heroes.characters.classes.HeroClass;
import com.sucy.skill.api.skills.Skill;
import me.Lorinth.LRM.Data.DataLoader;
import me.Lorinth.LRM.Data.HeroesDataManager;
import me.Lorinth.LRM.Data.SkillAPIDataManager;
import me.Lorinth.LRM.LorinthsRpgMobs;
import me.Lorinth.LRM.Objects.CreatureData;
import me.Lorinth.LRM.Objects.LevelRegion;
import me.Lorinth.LRM.Objects.NameData;
import me.Lorinth.LRM.Objects.NameOptions;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Creature;
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

/**
 * Controls creature data and defaults
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

            CreatureData data = dataLoader.getCreatureDataManager().getData(entity);
            if(data.isDisabled(entity.getWorld().getName()))
                return;

            //Set Level
            int level = dataLoader.calculateLevel(entity.getLocation());
            entity.setMetadata("Level", new FixedMetadataValue(LorinthsRpgMobs.instance, level));

            int health = (int)data.getHealthAtLevel(level);
            entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue((double) health);
            entity.setHealth((double) health);

            //Set Name
            LevelRegion region = LorinthsRpgMobs.GetLevelRegionManager().getHighestPriorityLeveledRegionAtLocation(entity.getLocation());
            NameData regionNameData = region != null ? region.getEntityName(entity.getType()) : null;
            NameOptions nameOptions = dataLoader.getNameOptions();
            entity.setCustomNameVisible(nameOptions.getTagsAlwaysOn());
            String name = data.getNameAtLevel(nameOptions.getNameFormat(), regionNameData, level);
            if(name != null)
                entity.setCustomName(name);

            entity.setRemoveWhenFarAway(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onCreatureAttack(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Creature){
            Creature creature = (Creature) event.getDamager();

            CreatureData data = dataLoader.getCreatureDataManager().getData(creature);
            if (data.isDisabled(creature.getWorld().getName()))
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
            //Using built in minecraft kill detection
            if(event.getDroppedExp() == 0 || creature.getKiller() == null){
                return;
            }

            CreatureData data = dataLoader.getCreatureDataManager().getData(creature);
            if (data.isDisabled(creature.getWorld().getName()))
                return;

            int level = LorinthsRpgMobs.GetLevelOfCreature(creature);
            int exp = data.getExperienceAtLevel(level);

            if(exp > 0){
                Player player = creature.getKiller();
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
}
