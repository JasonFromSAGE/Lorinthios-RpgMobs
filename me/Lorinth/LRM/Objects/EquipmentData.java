package me.Lorinth.LRM.Objects;

import me.Lorinth.LRM.LorinthsRpgMobs;
import me.Lorinth.LRM.Util.OutputHandler;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class EquipmentData{

    private HashMap<Integer, EquipmentDetail> WeaponLevels = new HashMap<>();
    private HashMap<Integer, EquipmentDetail> OffHandLevels = new HashMap<>();
    private HashMap<Integer, EquipmentDetail> HelmetLevels = new HashMap<>();
    private HashMap<Integer, EquipmentDetail> ChestLevels = new HashMap<>();
    private HashMap<Integer, EquipmentDetail> LegLevels = new HashMap<>();
    private HashMap<Integer, EquipmentDetail> BootLevels = new HashMap<>();

    public void loadData(FileConfiguration config, String prefix){
        prefix += ".Equipment";

        loadLevels(config, prefix + ".Weapon", WeaponLevels);
        loadLevels(config, prefix + ".Offhand", OffHandLevels);
        loadLevels(config, prefix + ".Helmet", HelmetLevels);
        loadLevels(config, prefix + ".Chestplate", ChestLevels);
        loadLevels(config, prefix + ".Leggings", LegLevels);
        loadLevels(config, prefix + ".Boots", BootLevels);
    }

    private void loadLevels(FileConfiguration config, String prefix, HashMap<Integer, EquipmentDetail> data){
        if(config.contains(prefix)){
            for(String key : config.getConfigurationSection(prefix).getKeys(false)){
                try{
                    Integer level = Integer.parseInt(key);
                    data.put(level, new EquipmentDetail(config, prefix + "." + key));
                }
                catch(Exception exception){
                    OutputHandler.PrintException("Failed to load equipment level, " + key, exception);
                }
            }
        }
    }

    public void equip(LivingEntity creature, int level){
        EntityEquipment equipment = creature.getEquipment();
        //Main Hand
        if (!(doesVanillaItemOverride(equipment.getItemInMainHand()))){
        	EquipmentResult mainHand = getHighest(level, WeaponLevels);
            if(mainHand != null && !mainHand.isEmpty) {
                equipment.setItemInMainHand(mainHand.getItem());
                equipment.setItemInMainHandDropChance((float) mainHand.getDropChance());
            }
        }

        //Off Hand
        if (!(doesVanillaItemOverride(equipment.getItemInOffHand()))){
        	EquipmentResult offHand = getHighest(level, OffHandLevels);
            if(offHand != null && !offHand.isEmpty) {
                equipment.setItemInOffHand(offHand.getItem());
                equipment.setItemInOffHandDropChance((float) offHand.getDropChance());
            }
        }

        //Helmet
        if (!(doesVanillaItemOverride(equipment.getHelmet()))){
        	EquipmentResult helmet = getHighest(level, HelmetLevels);
            if(helmet != null && !helmet.isEmpty) {
                equipment.setHelmet(helmet.getItem());
                equipment.setHelmetDropChance((float) helmet.getDropChance());
            }
        }

        //Chest
        if (!(doesVanillaItemOverride(equipment.getChestplate()))){
            EquipmentResult chest = getHighest(level, ChestLevels);
            if(chest != null && !chest.isEmpty) {
                equipment.setChestplate(chest.getItem());
                equipment.setChestplateDropChance((float) chest.getDropChance());
            }

        }

        //Legs
        if (!(doesVanillaItemOverride(equipment.getLeggings()))){
            EquipmentResult legs = getHighest(level, LegLevels);
            if(legs != null && !legs.isEmpty) {
                equipment.setLeggings(legs.getItem());
                equipment.setLeggingsDropChance((float) legs.getDropChance());
            }
        }

        //Boots
        if (!(doesVanillaItemOverride(equipment.getBoots()))){
            EquipmentResult boots = getHighest(level, BootLevels);
            if(boots != null && !boots.isEmpty) {
                equipment.setBoots(boots.getItem());
                equipment.setBootsDropChance((float) boots.getDropChance());
            }
        }
    }

    private boolean doesVanillaItemOverride(ItemStack item){
        return LorinthsRpgMobs.properties.VanillaMobEquipmentOverrides && item != null && item.getType() != Material.AIR &&
                item.getItemMeta() != null && item.getItemMeta().hasEnchants();
    }

    private EquipmentResult getHighest(int level, HashMap<Integer, EquipmentDetail> equipLevels){
        int highest = 0;
        for(Integer key : equipLevels.keySet()){
            if(key >= highest && key <= level)
                highest = key;
        }

        if(highest > 0){
            return equipLevels.get(highest).getItem();
        }
        return null;
    }
}
