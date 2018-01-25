package me.Lorinth.LRM.Objects;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.HashMap;

/**
 * Created by lorinthio on 1/24/2018.
 */
public class CreatureData extends DirtyObject{

    private EntityType EntityType;
    private boolean isDisabled = false;

    //Formulas
    private String healthFormula;
    private String damageFormula;
    private String expFormula;

    private HashMap<Integer, String> leveledNames = new HashMap<>();

    protected void SaveData(FileConfiguration config, String prefix){
        prefix += EntityType.toString();
        saveFormulas(config, prefix);
        saveNames(config, prefix);
    }

    private void saveFormulas(FileConfiguration config, String prefix){
        config.set(prefix + ".Health", healthFormula);
        config.set(prefix + ".Damage", damageFormula);
        config.set(prefix + ".Experience", expFormula);
    }

    private void saveNames(FileConfiguration config, String prefix){
        for(Integer key : leveledNames.keySet()){
            config.set(prefix + ".Names." + key, leveledNames.get(key));
        }
    }

    //loads data from file
    public CreatureData(EntityType entityType, String prefix, FileConfiguration config) {
        EntityType = entityType;
        load(config, prefix);
    }

    private void load(FileConfiguration config, String prefix){
        if(config.getBoolean(prefix + "Disabled") || config.getBoolean(prefix + EntityType.toString() + ".Disabled")){
            isDisabled = true;
            return;
        }

        prefix += EntityType.toString();

        loadFormulas(config, prefix);
        loadNames(config, prefix);
    }

    private void loadFormulas(FileConfiguration config, String prefix){
        healthFormula = config.getString(prefix + ".Health");
        damageFormula = config.getString(prefix + ".Damage");
        expFormula = config.getString(prefix + ".Experience");
    }

    private void loadNames(FileConfiguration config, String prefix){
        for(String key : config.getConfigurationSection(prefix + ".Names").getKeys(false)){
            leveledNames.put(Integer.parseInt(key), config.getString(prefix + ".Names." + key));
        }
    }

    //Creates data based on creature
    public CreatureData(Creature creature){
        EntityType = creature.getType();

        healthFormula = ((int) creature.getMaxHealth()) + " + (Level / 3) + (Level / 5) + rand(5)";
        damageFormula = "rand(3) + (Level / 10)";
        expFormula = "rand(3) + 1";

        this.setNew();
    }

    public String getHealthFormula(){
        return healthFormula;
    }

    public String getDamagerFormula(){
        return damageFormula;
    }

    public double getHealthAtLevel(Integer level){
        return Calculator.eval(preParseFormula(healthFormula, level));
    }

    public double getDamageAtLevel(Integer level){
        return Calculator.eval(preParseFormula(damageFormula, level));
    }

    public int getExperienceAtLevel(Integer level){
        return (int) Calculator.eval(preParseFormula(expFormula, level));
    }

    private String preParseFormula(String formulaBefore, Integer level){
        String formulaAfter = healthFormula.replace("level", level.toString());
        formulaAfter = healthFormula.replace("lvl", level.toString());
        return formulaAfter;
    }

    public boolean isDisabled(){
        return isDisabled;
    }
}
