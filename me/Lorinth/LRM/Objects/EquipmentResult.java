package me.Lorinth.LRM.Objects;

import org.bukkit.inventory.ItemStack;

public class EquipmentResult {

    ItemStack item;
    double dropChance;

    public EquipmentResult(ItemStack item, double dropChance){
        this.item = item;
        this.dropChance = dropChance;
    }

    public ItemStack getItem(){ return item; }

    public double getDropChance(){ return dropChance; }

}
