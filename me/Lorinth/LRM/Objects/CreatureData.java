package me.Lorinth.LRM.Objects;

import me.Lorinth.LRM.LorinthsRpgMobs;
import me.Lorinth.LRM.Util.Calculator;
import me.Lorinth.LRM.Util.ConfigHelper;
import me.Lorinth.LRM.Util.OutputHandler;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;

import java.util.ArrayList;

/**
 * Contains formulas, world data and name data for a creature type
 */
public class CreatureData extends DirtyObject{

    private EntityType entityType;
    private EquipmentData equipmentData = new EquipmentData();
    private boolean entityDisabled = false;
    private boolean groupDisabled = false;

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
        load(config, prefix + ".");
    }

    /**
     * Creates new creature data based on a creature entity
     * @param entity - creature to base data on
     */
    public CreatureData(LivingEntity entity){
        entityType = entity.getType();

        String type = entity instanceof Monster ? "Monster" : entity instanceof Creature ? "Animal" : "Misc";
        if(LorinthsRpgMobs.instance.getConfig().getBoolean("Entity." + type + ".Disabled"))
            groupDisabled = true;

        healthFormula = (entity.getAttribute(Attribute.GENERIC_MAX_HEALTH) != null ? (int) entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() : 20) + " + ({level} / 3) + ({level} / 5) + rand(5)";
        damageFormula = (entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) != null ? (int) entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getValue() : 2) + " + rand(3) + ({level} / 12)";
        expFormula = "rand(5) + 1 + rand({level} / 5)";

        String friendlyName = getUserFriendlyName(entityType);
        nameData.add(new NameData(1, friendlyName, false));
        nameData.add(new NameData(40, "Epic " + friendlyName, false));

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
        config.set(prefix + ".Disabled", entityDisabled);
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
        //Backwards compatibility updates
        config.set(prefix + ".Health", null);
        config.set(prefix + ".Damage", null);
        config.set(prefix + ".Experience", null);
        config.set(prefix + ".Exp", null);

        prefix += ".Formulas";
        config.set(prefix + ".Health", healthFormula);
        config.set(prefix + ".Damage", damageFormula);
        config.set(prefix + ".Exp", null);
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
        entityDisabled = config.getBoolean(prefix + "Disabled");
        groupDisabled = config.getBoolean(prefix + entityType.toString() + ".Disabled");
        if(entityDisabled || groupDisabled)
            return; //We don't care about loading disabled entities

        loadDisabledWorlds(config, prefix);
        prefix += entityType.toString();

        loadFormulas(config, prefix);
        loadNames(config, prefix);
        loadEquipmentData(config, prefix);
    }

    /**
     * Loads the formulas associated with this object from config file
     * @param config - config file to load from
     * @param prefix - the path prefix we will use
     */
    private void loadFormulas(FileConfiguration config, String prefix){
        //Backwards Compatibility
        if(config.contains(prefix + ".Health")){
            healthFormula = config.getString(prefix + ".Health");
            damageFormula = config.getString(prefix + ".Damage");
            if(config.contains(prefix + ".Exp"))
                expFormula = config.getString(prefix + ".Exp");
            else
                expFormula = config.getString(prefix + ".Experience");
            this.setDirty();
        }
        else{
            prefix += ".Formulas";
            healthFormula = config.getString(prefix + ".Health");
            damageFormula = config.getString(prefix + ".Damage");
            if(config.contains(prefix + ".Exp"))
                expFormula = config.getString(prefix + ".Exp");
            else
                expFormula = config.getString(prefix + ".Experience");
        }
    }

    /**
     * Loads the names associated with this object from config file
     * @param config - config file to load from
     * @param prefix - the path prefix we will use
     */
    private void loadNames(FileConfiguration config, String prefix){
        if(ConfigHelper.ConfigContainsPath(config, prefix + ".Names")){
            for(String key : config.getConfigurationSection(prefix + ".Names").getKeys(false)){
                try{
                    int level = Integer.parseInt(key);
                    String name = "";
                    boolean overrideFormat = false;

                    //Backwards support for Names
                    if(config.get(prefix + ".Names." + key) != null)
                        name = config.getString(prefix + ".Names." + key).replace("&", "§");
                    if(config.get(prefix + ".Names." + key + ".Name") != null)
                        name = config.getString(prefix + ".Names." + key + ".Name").replace("&", "§");

                    //Check for OverrideFormat
                    if(config.contains(prefix + ".Names." + key + ".OverrideFormat"))
                        overrideFormat = config.getBoolean(prefix + ".Names." + key + ".OverrideFormat");

                    nameData.add(new NameData(level, name, overrideFormat));
                }
                catch(Exception exception){
                    OutputHandler.PrintException("Unable to part key, '" + key + "' as a level/int.", exception);
                }
            }
        }
        else{
            OutputHandler.PrintInfo("No names for entity, " + entityType.name());
        }
    }

    private void loadEquipmentData(FileConfiguration config, String prefix){
        equipmentData.loadData(config, prefix);
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
     * @param regionNameData - regionNameData if applicable
     * @return - name that will be applied
     */
    public String getNameAtLevel(NameData regionNameData, int level){
        if(regionNameData != null)
            return regionNameData.getName(level);

        NameData highest = null;
        for(NameData data : nameData){
            if((highest == null && data.getLevel() <= level ) || (highest != null && data.getLevel() <= level && data.getLevel() >= highest.getLevel())){
                highest = data;
            }
        }

        if(highest != null)
            return highest.getName(level);
        return null;
    }

    public EquipmentData getEquipmentData(){
        return equipmentData;
    }

    /**
     * Get the health this data would give at the given level
     * @param level - level used in the health formula
     * @return - health the entity should have
     */
    public double getHealthAtLevel(Integer level){
        try{
            return (int) Calculator.eval(preParseFormula(healthFormula, level));
        }
        catch(Exception exception){
            OutputHandler.PrintRawError("Got Health Error for, " + entityType.toString());
            OutputHandler.PrintException("Level : " + level + ", Formula : " + healthFormula, exception);
        }
        return 1;
    }

    /**
     * Get the damage this data would give at the given level
     * @param level - level used in the damage formula
     * @return - damage the entity should do
     */
    public double getDamageAtLevel(Integer level){
        try{
            return (int) Calculator.eval(preParseFormula(damageFormula, level));
        }
        catch(Exception exception){
            OutputHandler.PrintRawError("Got Damage Formula Error for, " + entityType.toString());
            OutputHandler.PrintException("Level : " + level + ", Formula : " + damageFormula, exception);
        }
        return 1;
    }

    /**
     * Get the exp this data would give at the given level
     * @param level - level used in the exp formula
     * @return - exp the entity should give
     */
    public int getExperienceAtLevel(Integer level){
        try{
            return (int) Calculator.eval(preParseFormula(expFormula, level));
        }
        catch(Exception exception){
            OutputHandler.PrintRawError("Got Experience Error for, " + entityType.toString());
            OutputHandler.PrintException("Level : " + level + ", Formula : " + expFormula, exception);
        }
        return 1;
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
        return entityDisabled || groupDisabled || entityDisabledWorlds.contains(worldName) || groupDisabledWorlds.contains(worldName);
    }
}
