package me.Lorinth.LRM.Listener;

import me.Lorinth.LRM.Data.BossApiManager;
import me.Lorinth.LRM.Data.DataLoader;
import me.Lorinth.LRM.Data.MobVariantDataManager;
import me.Lorinth.LRM.Data.VaultManager;
import me.Lorinth.LRM.Events.RpgMobDeathEvent;
import me.Lorinth.LRM.LorinthsRpgMobs;
import me.Lorinth.LRM.Objects.CreatureData;
import me.Lorinth.LRM.Objects.EquipmentData;
import me.Lorinth.LRM.Objects.LevelRegion;
import me.Lorinth.LRM.Objects.NameData;
import me.Lorinth.LRM.Util.MetaDataConstants;
import me.Lorinth.LRM.Variants.MobVariant;
import me.Lorinth.LRM.Variants.WealthyVariant;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * Controls creature data and defaults
 */
public class CreatureEventListener implements Listener {

    private DataLoader dataLoader;
    private Random random;

    public CreatureEventListener(DataLoader dataLoader){
        this.dataLoader = dataLoader;
        random = new Random();
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntitySpawn(EntitySpawnEvent event){
        if(!(event.getEntity() instanceof LivingEntity))
            return;
        if(event.getEntity() instanceof Creature)
            return;

        updateEntity(event, (LivingEntity) event.getEntity(), null);
    }

    @EventHandler(ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event){
        updateEntity(event, event.getEntity(), event.getSpawnReason());
    }

    private void updateEntity(EntitySpawnEvent event, LivingEntity entity, CreatureSpawnEvent.SpawnReason spawnReason){
        if(!replaceWithBoss(event, entity)){
            if(spawnReason != null && spawnReason == CreatureSpawnEvent.SpawnReason.SPAWNER && dataLoader.ignoresSpawnerMobs()) {
                dataLoader.ignoreEntity(event.getEntity());
                return;
            }

            CreatureData data = dataLoader.getCreatureDataManager().getData(entity);
            if(data.isDisabled(entity.getWorld().getName()))
                return;
            if(isEliteMob(entity))
                return;

            //Set Level
            int level = dataLoader.calculateLevel(entity.getLocation(), entity);
            if(level == -1)
                return;

            setLevel(entity, level);
            setHealth(entity, data, level);
            setDamage(entity, data, level);
            setEquipment(entity, data, level);
            setName(entity, data, level);
            setVariant(entity);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPotionSplashEvent(PotionSplashEvent event){
        if(event.getPotion().getShooter() instanceof Witch){
            Witch witch = (Witch) event.getPotion().getShooter();
            if(witch.hasMetadata(MetaDataConstants.Damage)){
                int damage = witch.getMetadata(MetaDataConstants.Damage).get(0).asInt();

                ThrownPotion potion = event.getPotion();
                Collection<PotionEffect> effects = potion.getEffects();
                List<PotionEffect> removeEffects = new ArrayList<>();

                for(PotionEffect effect : effects){
                    if(effect.getType() == PotionEffectType.HARM){
                        removeEffects.add(effect);
                    }
                }

                for(PotionEffect effect : removeEffects){
                    potion.getEffects().add(new PotionEffect(effect.getType(), effect.getDuration(), damage-1));
                    potion.getEffects().remove(effect);
                }
            }
        }

    }

    private boolean replaceWithBoss(EntitySpawnEvent event, Entity entity){
        LevelRegion region = LorinthsRpgMobs.GetLevelRegionManager().getHighestPriorityLeveledRegionAtLocation(entity.getLocation());
        if(region == null)
            return false;

        if(BossApiManager.isEnabled() &&
                BossApiManager.spawnBoss(event.getLocation(), entity.getType(), region.getBossReplacements())){
            event.setCancelled(true);
            return true;
        }
        return false;
    }

    private void setLevel(Entity entity, int level){
        entity.setMetadata(MetaDataConstants.Level, new FixedMetadataValue(LorinthsRpgMobs.instance, level));
    }

    private boolean isEliteMob(Entity entity){
        return entity.hasMetadata("EliteMob") || entity.hasMetadata("PassiveEliteMob");
    }

    private void setHealth(LivingEntity entity, CreatureData data, int level){
        int health = (int)data.getHealthAtLevel(level);
        if(LorinthsRpgMobs.properties.IsAttributeVersion){
            AttributeInstance attribute = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if(attribute != null)
                attribute.setBaseValue((double) health);
            else
                entity.setMaxHealth(health);
        }
        else
            entity.setMaxHealth(health);
        entity.setHealth((double) health);
    }

    private void setDamage(LivingEntity entity, CreatureData data, int level){
        double damage = (int)data.getDamageAtLevel(level);
        if(LorinthsRpgMobs.properties.IsAttributeVersion) {
            AttributeInstance attribute = entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
            if (attribute != null)
                attribute.setBaseValue(damage);
        }
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
        entity.setCustomNameVisible(LorinthsRpgMobs.properties.NameTagsAlwaysOn);
        String name = data.getNameAtLevel(regionNameData, level);
        if(name != null)
            entity.setCustomName(name);

        //Allows creatures with custom names to be removed
        entity.setRemoveWhenFarAway(true);
    }

    private void setVariant(LivingEntity entity){
        MobVariant variant = MobVariantDataManager.GetVariant(entity);

        //If Variant hasn't been applied, replace all variant tags in format
        if(variant == null){
            String name = entity.getCustomName();
            if(name != null){
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
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onTame(EntityTameEvent event){
        event.getEntity().setRemoveWhenFarAway(false);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityHit(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Player)
            return;

        Entity entity = event.getEntity();
        Entity damager = event.getDamager();
        if(entity instanceof LivingEntity){
            LivingEntity target = (LivingEntity) entity;

            MobVariant variant = LorinthsRpgMobs.GetMobVariantOfEntity(damager);
            if(variant != null)
                variant.onHit(target, event);
        }
        if(damager instanceof Projectile){
            if(damager.hasMetadata(MetaDataConstants.Damage)) {
                double damage = damager.getMetadata(MetaDataConstants.Damage).get(0).asDouble();
                event.setDamage(damage);
            }

            damager = (Entity) ((Projectile) damager).getShooter();
        }
        if(damager instanceof LivingEntity){
            LivingEntity living = (LivingEntity) damager;

            if(living.hasMetadata(MetaDataConstants.Damage)){
                double damage = living.getMetadata(MetaDataConstants.Damage).get(0).asDouble();
                dataLoader.getHeroesDataManager().handleEntityDamageEvent(living, damage);
                event.setDamage(damage);
            }

            MobVariant variant = LorinthsRpgMobs.GetMobVariantOfEntity(entity);
            if(variant != null)
                variant.whenHit(living, event);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onCreatureDeath(EntityDeathEvent event){
        LivingEntity entity = event.getEntity();
        if(event.getDroppedExp() == 0 || entity.getKiller() == null)
            return;

        //if(dataLoader.isIgnoredEntity(entity, true))
            //return;

        CreatureData data = dataLoader.getCreatureDataManager().getData(entity);
        if (data.isDisabled(entity.getWorld().getName()))
            return;

        MobVariant variant = LorinthsRpgMobs.GetMobVariantOfEntity(entity);
        if(variant != null)
            variant.onDeath(entity);

        Integer level = LorinthsRpgMobs.GetLevelOfEntity(entity);
        if(level != null){
            int exp = data.getExperienceAtLevel(level);
            double currencyValue = 0;
            double currencyChance = data.getCurrencyChanceAtLevel(level);
            double roll = random.nextDouble() * 100.0;

            if(currencyChance > roll || variant instanceof WealthyVariant)
                currencyValue = data.getCurrencyValueAtLevel(level);

            if(exp > 0){
                Player player = getKillerFromEntity(event);
                float multiplier = dataLoader.getExperiencePermissionManager().getExperienceMultiplier(player);
                exp =  (int) (exp * multiplier);

                RpgMobDeathEvent rpgMobDeathEvent = new RpgMobDeathEvent(player, entity, level, exp, currencyValue);
                Bukkit.getPluginManager().callEvent(rpgMobDeathEvent);
                if(!rpgMobDeathEvent.isCancelled()){
                    if(variant != null)
                        variant.onDeathEvent(rpgMobDeathEvent);

                    if(rpgMobDeathEvent.getExp() > 0){
                        if(dataLoader.getHeroesDataManager().handleEntityDeathEvent(event, player, rpgMobDeathEvent.getExp(), rpgMobDeathEvent.getCurrencyValue()))
                            return;
                        else if(dataLoader.getSkillAPIDataManager().handleEntityDeathEvent(event, player, rpgMobDeathEvent.getExp(), rpgMobDeathEvent.getCurrencyValue()))
                            return;
                        else{
                            event.setDroppedExp(rpgMobDeathEvent.getExp());
                        }
                    }

                    Integer currency = (int)Math.floor(rpgMobDeathEvent.getCurrencyValue());
                    if(currency > 0){
                        VaultManager.addMoney(player, entity, currency);
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onProjectileLaunch(ProjectileLaunchEvent event){
        Projectile projectile = event.getEntity();
        if(projectile.getShooter() instanceof Creature){
            Creature creature = (Creature) projectile.getShooter();
            if(creature.hasMetadata(MetaDataConstants.Damage)){
                projectile.setMetadata(MetaDataConstants.Damage, creature.getMetadata(MetaDataConstants.Damage).get(0));
            }
        }
    }

    private Player getKillerFromEntity(EntityDeathEvent event){
        EntityDamageEvent lastDamageEvent = event.getEntity().getLastDamageCause();

        Player killer = event.getEntity().getKiller();
        killer = killer != null ? killer : getKillerFromTamedEntity(lastDamageEvent);

        return killer;
    }

    private Player getKillerFromTamedEntity(EntityDamageEvent lastDamageEvent){
        if(lastDamageEvent instanceof EntityDamageByEntityEvent){
            Entity damager = ((EntityDamageByEntityEvent) lastDamageEvent).getDamager();
            if(damager instanceof Player){
                return (Player) damager;
            }
            if(damager instanceof Tameable){
                AnimalTamer tamer = ((Tameable) damager).getOwner();
                if(tamer instanceof Player)
                    return (Player) tamer;
            }
        }

        return null;
    }
}
