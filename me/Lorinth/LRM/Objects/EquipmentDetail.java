package me.Lorinth.LRM.Objects;

import me.Lorinth.LRM.Util.OutputHandler;
import me.Lorinth.LRM.Util.TryParse;
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
            Material mat = null;
            Double chance = 100d;
            Double dropChance = 0d;

            for(String detail : details){
                detail = detail.replace("%", "").trim();
                if(detail.contains("drop:") && TryParse.parseDouble(detail.replace("drop:", "").replace("%", "").trim()))
                    dropChance = Double.parseDouble(detail.replace("drop:", "").replace("%", "").trim());
                else if(TryParse.parseDouble(detail))
                    chance = Double.parseDouble(detail);
                else if(TryParse.parseMaterial(detail))
                    mat = Material.valueOf(detail);
            }

            if(mat != null){
                Materials.add(mat);
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
                return new EquipmentResult(new ItemStack(Materials.get(i)), DropChances.get(i));
            }
        }

        return new EquipmentResult(new ItemStack(Material.AIR), 0);
    }

}
