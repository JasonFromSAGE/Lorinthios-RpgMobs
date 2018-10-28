package me.Lorinth.LRM.Objects;

import com.sucy.enchant.EnchantmentAPI;
import com.sucy.enchant.api.CustomEnchantment;
import me.Lorinth.LRM.Data.EnchantmentApiManager;
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
            Double chance = 100d;
            Double dropChance = 0d;
            ItemStack itemStack = null;
            Map<String, Integer> enchants = new HashMap<>();

            for(String detail : details){
                detail = detail.replace("%", "").trim();
                if(detail.contains(":")){
                    if(detail.contains("drop:") && TryParse.parseDouble(detail.replace("drop:", "").replace("%", "").trim()))
                        dropChance = Double.parseDouble(detail.replace("drop:", "").replace("%", "").trim());
                    else{
                        String[] args = detail.split(":");
                        if(args.length == 2){
                            if(TryParse.parseEnchantFriendlyName(args[0]) != null && TryParse.parseInt(args[1])){
                                enchants.put(args[0], Integer.parseInt(args[1]));
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
                for(Map.Entry<String, Integer> entry : enchants.entrySet()){
                    Object enchant = EnchantmentApiManager.getEnchantment(entry.getKey());
                    if(enchant != null){
                        if(enchant instanceof Enchantment){
                            if(((Enchantment) enchant).canEnchantItem(itemStack))
                                itemStack.addEnchantment((Enchantment) enchant, entry.getValue());
                        }
                        else if(enchant instanceof CustomEnchantment){
                            if(((CustomEnchantment) enchant).canEnchantOnto(itemStack))
                                ((CustomEnchantment) enchant).addToItem(itemStack, entry.getValue());
                        }
                    }
                    else
                        OutputHandler.PrintError("Can't apply enchant, " + entry.getKey() + " to material, " + itemStack.getType().name());
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
