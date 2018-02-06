package me.Lorinth.LRM.Objects;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Random;

public class EquipmentDetail {

    ArrayList<Material> Materials = new ArrayList<>();
    ArrayList<Double> Chances = new ArrayList<>();
    ArrayList<Double> DropChances = new ArrayList<>();
    private Random random = new Random();

    public EquipmentDetail(FileConfiguration config, String path){
        String line = config.getString(path);
        String[] items = line.split(",");
        for(String item : items){
            item = item.trim();
            String[] details = item.split(" ");
            try{
                Material mat = Material.valueOf(details[0]);
                Double chance = 100d;
                Double dropChance = 0d;
                if(details.length > 1)
                    chance = Double.parseDouble(details[1].replace("%", "").trim());
                if(details.length > 2)
                    dropChance = Double.parseDouble(details[2].replace("drop:", "").replace("%", "").trim());

                if(mat != null){
                    Materials.add(mat);
                    Chances.add(chance);
                    DropChances.add(dropChance);
                }
            }
            catch(Exception exception){
                OutputHandler.PrintException("Failed to load item data, " + item, exception);
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
                return new EquipmentResult(new ItemStack(Materials.get(i)), DropChances.get(i));
            }
        }

        return new EquipmentResult(new ItemStack(Material.AIR), 0);
    }

}
