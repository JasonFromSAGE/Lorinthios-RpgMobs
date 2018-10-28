package me.Lorinth.LRM.Data;

import me.Lorinth.LRM.LorinthsRpgMobs;
import me.Lorinth.LRM.Objects.DataManager;
import me.Lorinth.LRM.Objects.Disableable;
import me.Lorinth.LRM.Util.ConfigHelper;
import me.Lorinth.LRM.Util.OutputHandler;
import me.Lorinth.LRM.Variants.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MobVariantDataManager extends Disableable implements DataManager {

    private static HashMap<String, ArrayList<MobVariant>> entityTypeVariants = new HashMap<>();
    private static HashMap<String, Integer> entityTypeVariantWeight = new HashMap<>();
    private static ArrayList<String> disabledEntityTypes = new ArrayList<>();
    private static boolean disabled = false;
    private static int variantChance = 0;
    private static MobVariantDataManager instance;
    private static Random random = new Random();

    public MobVariantDataManager(){
        instance = this;
    }

    @Override
    public void loadData(FileConfiguration config, Plugin plugin) {
        if(!ConfigHelper.ConfigContainsPath(config, "MobVariants.Disabled")){
            OutputHandler.PrintInfo("Mob Variants options not found, Generating...");
            setDefaults(config, plugin);
        }

        if(config.getBoolean("MobVariants.Disabled")) {
            OutputHandler.PrintInfo("Mob Variants Disabled!");
            disabled = true;
            return;
        }

        OutputHandler.PrintInfo("Mob Variants Enabled!");
        variantChance = config.getInt("MobVariants.VariantChance");
        disabledEntityTypes.addAll(config.getStringList("MobVariants.DisabledTypes"));

        entityTypeVariants = new HashMap<>();
        entityTypeVariantWeight = new HashMap<>();
        loadInternalVariants();
    }

    //Variants self load/add when instantiated
    private void loadInternalVariants(){
        new BlindingVariant();
        new BurningVariant();
        new ExplosiveVariant();
        new FastVariant();
        new ForcefulVariant();
        new GlowingVariant();
        new InvisibleVariant();
        new PoisonousVariant();
        new SlowVariant();
        new StrongVariant();
        new SturdyVariant();
        new ToughVariant();
    }

    public static void AddVariant(MobVariant variant){
        for(EntityType type : EntityType.values()){
            if(!variant.isDisabledEntityType(type)){
                ArrayList<MobVariant> variants = entityTypeVariants.getOrDefault(type.name(), new ArrayList<>());
                Integer totalWeight = entityTypeVariantWeight.getOrDefault(type.name(), 0);

                variants.add(variant);
                entityTypeVariants.put(type.name(), variants);
                entityTypeVariantWeight.put(type.name(), totalWeight + variant.getWeight());
            }
        }
    }



    public static MobVariant GetVariant(LivingEntity entity){
        if(disabled ||
            disabledEntityTypes.contains(entity.getType().name()) ||
            entityTypeVariantWeight.getOrDefault(entity.getType().name(), 0) == 0 ||
            LorinthsRpgMobs.IsMythicMob(entity))
            return null;

        if(random.nextInt(100) < variantChance){
            int current = 0;
            int target = random.nextInt(entityTypeVariantWeight.get(entity.getType().name()));
            for(MobVariant variant : entityTypeVariants.get(entity.getType().name())){
                current += variant.getWeight();
                if(current > target)
                    if(variant.apply(entity))
                        return variant;
                    else
                        return GetVariant(entity);
            }
        }
        return null;
    }

    @Override
    public boolean saveData(FileConfiguration config) {
        return false;
    }

    private void setDefaults(FileConfiguration config, Plugin plugin){
        config.set("MobVariants.Disabled", true);
        config.set("MobVariants.VariantChance", 40);
        config.set("MobVariants.DisabledTypes", new ArrayList<String>(){{ add(EntityType.CREEPER.name()); }});
        plugin.saveConfig();
    }
}
