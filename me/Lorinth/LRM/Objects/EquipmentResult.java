package me.Lorinth.LRM.Objects;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class EquipmentResult {

    ItemStack item;
    double dropChance;
    boolean isEmpty;

    public EquipmentResult(ItemStack item, double dropChance){
        this.item = item;
        this.dropChance = dropChance;
    }

    public EquipmentResult(ItemStack item, double dropChance, boolean isEmpty){
        this.item = item;
        this.dropChance = dropChance;
        this.isEmpty = isEmpty;
    }

    public ItemStack getItem(){ return item.clone(); }

    public boolean isEmpty(){ return item.getType() == Material.AIR; }

    public double getDropChance(){ return dropChance; }

}
