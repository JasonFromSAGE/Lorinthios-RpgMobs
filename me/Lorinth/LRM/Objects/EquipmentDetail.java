package me.Lorinth.LRM.Objects;

import me.Lorinth.LRM.Util.OutputHandler;
import me.Lorinth.LRM.Util.TryParse;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class EquipmentDetail {

    ArrayList<ItemStack> ItemStacks = new ArrayList<ItemStack>();
    ArrayList<Double> Chances = new ArrayList<>();
    ArrayList<Double> DropChances = new ArrayList<>();
    private Random random = new Random();
    private EquipmentResult empty = new EquipmentResult(new ItemStack(Material.AIR), 0, true);

    public EquipmentDetail(FileConfiguration config, String path){
        String line = config.getString(path);
        String[] items = line.split(",");
        for(String item : items){
            item = item.trim();
            String[] details = item.split(" ");
            Material mat = null;
            Double chance = 100d;
            Double dropChance = 0d;
            ItemStack itemStack = null;
            Map<Enchantment, Integer> enchants = new HashMap<>();

            for(String detail : details){
                detail = detail.replace("%", "").trim();
                if(detail.contains(":")){
                    if(detail.contains("drop:") && TryParse.parseDouble(detail.replace("drop:", "").replace("%", "").trim()))
                        dropChance = Double.parseDouble(detail.replace("drop:", "").replace("%", "").trim());
                    else{
                        String[] args = detail.split(":");
                        if(args.length == 2){
                            if(TryParse.parseEnchantFriendlyName(args[0]) != null && TryParse.parseInt(args[1])){
                                enchants.put(TryParse.parseEnchantFriendlyName(args[0]), Integer.parseInt(args[1]));
                            }
                        }
                    }
                }
                else if(TryParse.parseDouble(detail))
                    chance = Double.parseDouble(detail);
                else if(TryParse.parseMaterial(detail))
                    itemStack = new ItemStack(Material.valueOf(detail), 1);
            }

            if(itemStack != null){
                for(Map.Entry<Enchantment, Integer> entry : enchants.entrySet()){
                    if(entry.getKey().canEnchantItem(itemStack))
                        itemStack.addEnchantment(entry.getKey(), entry.getValue());
                    else
                        OutputHandler.PrintError("Can't apply enchant, " + entry.getKey().getName() + " to material, " + itemStack.getType().name());
                }

                ItemStacks.add(itemStack);
                Chances.add(chance);
                DropChances.add(dropChance);
            }
            else{
                OutputHandler.PrintError("Failed to load item data, " + item);
            }
        }
    }

    public EquipmentResult getItem(){
        double rand = random.nextDouble() * 100.0;
        double current = 0;
        for(int i=0; i<Chances.size(); i++){
            Double Chance = Chances.get(i);
            current += Chance;
            if(rand < current) {
                return new EquipmentResult(ItemStacks.get(i).clone(), DropChances.get(i));
            }
        }

        return empty;
    }

}
