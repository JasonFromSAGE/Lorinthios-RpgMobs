package me.Lorinth.LRM.Objects;

import me.Lorinth.LRM.LorinthsRpgMobs;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by lorinthio on 1/24/2018.
 */
public class CreatureData extends DirtyObject{

    private EntityType entityType;
    private boolean isDisabled = false;

    //Formulas
    private String damageFormula;
    private String expFormula;
    private String healthFormula;

    private ArrayList<NameData> nameData = new ArrayList<>();
    private ArrayList<String> groupDisabledWorlds = new ArrayList<String>();
    private ArrayList<String> entityDisabledWorlds = new ArrayList<String>();

    /**
     * Creates creature data based on saved config data
     * @param entityType - Type of entity
     * @param prefix - yml path prefix
     * @param config - config file
     */
    public CreatureData(EntityType entityType, String prefix, FileConfiguration config) {
        this.entityType = entityType;
        load(config, prefix);
    }

    /**
     * Creates new creature data based on a creature entity
     * @param creature - creature to base data on
     */
    public CreatureData(Creature creature){
        entityType = creature.getType();

        String type = creature instanceof Monster ? "Monster" : "Animal";
        if(LorinthsRpgMobs.instance.getConfig().getBoolean("Entity." + type + ".Disabled"))
            isDisabled = true;

        healthFormula = ((int) creature.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) + " + ({level} / 3) + ({level} / 5) + rand(5)";
        damageFormula = "rand(3) + ({level} / 10)";
        expFormula = "rand(3) + 1";

        String friendlyName = getUserFriendlyName(entityType);
        nameData.add(new NameData(1, "Weak " + friendlyName, false));
        nameData.add(new NameData(20, friendlyName, false));
        nameData.add(new NameData(40, "Strong " + friendlyName, false));

        this.setNew();
    }

    private String getUserFriendlyName(EntityType type){
        String friendlyTypeName = "";
        String[] split = type.name().split("_");

        for(String word : split){
            friendlyTypeName += word.substring(0,1).toUpperCase() + word.substring(1).toLowerCase() + " ";
        }
        return friendlyTypeName.trim();
    }

    /**
     * Saves all data associated with this object
     * @param config - config file to save to
     * @param prefix - the path prefix we will use
     */
    protected void saveData(FileConfiguration config, String prefix){
        prefix += entityType.toString();
        config.set(prefix + ".Disabled", isDisabled);
        saveDisabledWorlds(config, prefix);
        saveFormulas(config, prefix);
        saveNames(config, prefix);
    }

    /**
     * Saves the disabledWorlds
     * @param config - config file to save to
     * @param prefix - the path prefix we will use
     */
    private void saveDisabledWorlds(FileConfiguration config, String prefix){
        config.set(prefix + ".DisabledWorlds", entityDisabledWorlds);
    }

    /**
     * Saves the formulas associated with this object
     * @param config - config file to save to
     * @param prefix - the path prefix we will use
     */
    private void saveFormulas(FileConfiguration config, String prefix){
        config.set(prefix + ".Health", healthFormula);
        config.set(prefix + ".Damage", damageFormula);
        config.set(prefix + ".Experience", expFormula);
    }

    /**
     * Saves the names associated with this object
     * @param config - config file to save to
     * @param prefix - the path prefix we will use
     */
    private void saveNames(FileConfiguration config, String prefix){
        for(NameData data : nameData){
            data.save(config, prefix);
        }
    }

    /**
     * Loads the data from config file
     * @param config - config file to load from
     * @param prefix - the path prefix we will use
     */
    private void load(FileConfiguration config, String prefix){
        if(config.getBoolean(prefix + "Disabled") || config.getBoolean(prefix + entityType.toString() + ".Disabled")){
            isDisabled = true;
            return;
        }

        loadDisabledWorlds(config, prefix);
        prefix += entityType.toString();

        loadFormulas(config, prefix);
        loadNames(config, prefix);
    }

    /**
     * Loads the formulas associated with this object from config file
     * @param config - config file to load from
     * @param prefix - the path prefix we will use
     */
    private void loadFormulas(FileConfiguration config, String prefix){
        healthFormula = config.getString(prefix + ".Health");
        damageFormula = config.getString(prefix + ".Damage");
        expFormula = config.getString(prefix + ".Experience");
    }

    /**
     * Loads the names associated with this object from config file
     * @param config - config file to load from
     * @param prefix - the path prefix we will use
     */
    private void loadNames(FileConfiguration config, String prefix){
        if(config.contains(prefix + ".Names")){
            for(String key : config.getConfigurationSection(prefix + ".Names").getKeys(false)){
                int level = Integer.parseInt(key);

                //Backwards support
                if(config.get(prefix + ".Names." + key) != null) {
                    config.set(prefix + ".Names." + key + ".Name", config.getString(prefix + ".Names." + key));
                    config.set(prefix + ".Names." + key + ".OverrideFormat", false);
                }

                String name = config.getString(prefix + ".Names." + key + ".Name").replaceAll("&", "§");
                boolean overrideFormat = false;

                if(config.contains(prefix + ".Names." + key + ".OverrideFormat"))
                    overrideFormat = config.getBoolean(prefix + ".Names." + key + ".OverrideFormat");

                nameData.add(new NameData(level, name, overrideFormat));
            }
        }
    }

    /**
     * Loads the disabled worlds associated with this object from config file
     * @param config - config file to load from
     * @param prefix - the path prefix we will use
     */
    private void loadDisabledWorlds(FileConfiguration config, String prefix){
        if(config.contains(prefix + "DisabledWorlds"))
            groupDisabledWorlds.addAll(config.getStringList(prefix + "DisabledWorlds"));
        if(config.contains(prefix + entityType.toString() + ".DisabledWorlds"))
            entityDisabledWorlds.addAll(config.getStringList(prefix + entityType.toString() + ".DisabledWorlds"));
    }

    /**
     * Get the health formula assigned to this object
     * @return health formula
     */
    public String getHealthFormula(){
        return healthFormula;
    }

    /**
     * Set the health formula assigned to this object
     * @param formula - the new formula used for calculating health
     */
    public void setHealthFormula(String formula){
        if(formula == null){
            return;
        }

        healthFormula = formula;
        setDirty();
    }

    /**
     * Get the damage formula assigned to this object
     * @return damage formula
     */
    public String getDamagerFormula(){
        return damageFormula;
    }

    /**
     * Set the damage formula assigned to this object
     * @param formula - the new formula used for calculating damage
     */
    public void setDamagerFormula(String formula){
        if(formula == null){
            return;
        }

        damageFormula = formula;
        setDirty();
    }

    /**
     * Get the exp formula assigned to this object
     * @return exp formula
     */
    public String getExpFormula(){
        return expFormula;
    }

    /**
     * Set the exp formula assigned to this object
     * @param formula - the new formula used for calculating exp
     */
    public void setExpFormula(String formula){
        if(formula == null){
            return;
        }

        expFormula = formula;
        setDirty();
    }

    /**
     * Gets the name for a creature at a given level
     * @param level - level to check
     * @return - name that will be applied
     */
    public String getNameAtLevel(String format, int level){
        NameData highest = null;
        for(NameData data : nameData){
            if((highest == null && data.getLevel() < level ) || (highest != null && data.getLevel() < level && data.getLevel() >= highest.getLevel())){
                highest = data;
            }

        }

        if(highest != null)
            return highest.getName(level, format);

        return null;
    }

    /**
     * Get the health this data would give at the given level
     * @param level - level used in the health formula
     * @return - health the entity should have
     */
    public double getHealthAtLevel(Integer level){
        return Calculator.eval(preParseFormula(healthFormula, level));
    }

    /**
     * Get the damage this data would give at the given level
     * @param level - level used in the damage formula
     * @return - damage the entity should do
     */
    public double getDamageAtLevel(Integer level){
        return Calculator.eval(preParseFormula(damageFormula, level));
    }

    /**
     * Get the exp this data would give at the given level
     * @param level - level used in the exp formula
     * @return - exp the entity should give
     */
    public int getExperienceAtLevel(Integer level){
        return (int) Calculator.eval(preParseFormula(expFormula, level));
    }

    /**
     * Converts the string formula to use provided values such as {level}
     * @param formulaBefore - base formula
     * @param level - level to replace {level}
     * @return the formula we can evaluate
     */
    private String preParseFormula(String formulaBefore, Integer level){
        return formulaBefore.replace("{level}", level.toString());
    }

    /**
     * Are leveling features disabled for this entity type
     * @return - isDisabled
     */
    public boolean isDisabled(String worldName){
        return isDisabled || entityDisabledWorlds.contains(worldName) || groupDisabledWorlds.contains(worldName);
    }
}
