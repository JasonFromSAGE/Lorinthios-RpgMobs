package me.Lorinth.LRM.Objects.MythicDrops;

import me.Lorinth.LRM.Util.TryParse;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Random;

public class MythicDropsLevelData {

    ArrayList<String> TierNames = new ArrayList<>();
    ArrayList<Double> Chances = new ArrayList<>();
    private Random random = new Random();

    public MythicDropsLevelData(FileConfiguration config, String path) {
        String line = config.getString(path);
        String[] tiers = line.split(",");
        for (String tier : tiers) {
            String[] args = tier.split(" ");
            double percent = 0.0;
            String tierName = "";
            for(String arg : args){
                if(arg.contains("%")){
                    arg = arg.replace('%', ' ').trim();
                    if(TryParse.parseDouble(arg))
                        percent = Double.parseDouble(arg);
                }
                else{
                    tierName = arg.trim();
                }
            }

            if(percent != 0.0 && !tierName.equalsIgnoreCase("")) {
                TierNames.add(tierName);
                Chances.add(percent);
            }
        }
    }

    public String getTierName(){
        double rand = random.nextDouble() * 100.0;
        double current = 0;
        for(int i=0; i<Chances.size(); i++){
            Double Chance = Chances.get(i);
            current += Chance;
            if(rand < current) {
                return TierNames.get(i);
            }
        }

        return null;
    }

}
