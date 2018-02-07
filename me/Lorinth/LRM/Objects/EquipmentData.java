package me.Lorinth.LRM.Objects;

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
        EquipmentResult mainHand = getHighest(level, WeaponLevels);
        equipment.setItemInMainHand(mainHand.getItem());
        equipment.setItemInMainHandDropChance((float) mainHand.getDropChance());

        //Off Hand
        EquipmentResult offHand = getHighest(level, OffHandLevels);
        equipment.setItemInOffHand(offHand.getItem());
        equipment.setItemInOffHandDropChance((float) offHand.getDropChance());

        //Helmet
        EquipmentResult helmet = getHighest(level, HelmetLevels);
        equipment.setHelmet(helmet.getItem());
        equipment.setHelmetDropChance((float) helmet.getDropChance());

        //Chest
        EquipmentResult chest = getHighest(level, ChestLevels);
        equipment.setChestplate(chest.getItem());
        equipment.setChestplateDropChance((float) chest.getDropChance());

        //Legs
        EquipmentResult legs = getHighest(level, LegLevels);
        equipment.setLeggings(legs.getItem());
        equipment.setLeggingsDropChance((float) legs.getDropChance());

        //Boots
        EquipmentResult boots = getHighest(level, BootLevels);
        equipment.setBoots(boots.getItem());
        equipment.setBootsDropChance((float) boots.getDropChance());
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
        return new EquipmentResult(new ItemStack(Material.AIR), 0);
    }
}
