package me.Lorinth.LRM.Util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

public class TryParse {

    public static boolean parseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean parseDouble(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean parseFloat(String value) {
        try {
            Float.parseFloat(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean parseBoolean(String value){
        try {
            Boolean.parseBoolean(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean parseMaterial(String value){
        try {
            Material mat = Material.valueOf(value);
            return mat != null;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean parseEnchant(String value){
        try{
            Enchantment enchant = Enchantment.getByName(value);
            return enchant != null;
        } catch(Exception e){
            return false;
        }
    }

    public static Enchantment parseEnchantFriendlyName(String value){
        switch(value.toUpperCase()){
            case "AQUA_AFFINITY":
                return Enchantment.WATER_WORKER;
            case "BANE_OF_ARTHROPODS":
                return Enchantment.DAMAGE_ARTHROPODS;
            case "BLAST_PROTECTION":
                return Enchantment.PROTECTION_EXPLOSIONS;
            case "CURSE_OF_BINDING":
                return Enchantment.BINDING_CURSE;
            case "CURSE_OF_VANISHING":
                return Enchantment.VANISHING_CURSE;
            case "DEPTH_STRIDER":
                return Enchantment.DEPTH_STRIDER;
            case "EFFICIENCY":
                return Enchantment.DIG_SPEED;
            case "FEATHER_FALLING":
                return Enchantment.PROTECTION_FALL;
            case "FIRE_ASPECT":
                return Enchantment.FIRE_ASPECT;
            case "FIRE_PROTECTION":
                return Enchantment.PROTECTION_FIRE;
            case "FLAME":
                return Enchantment.ARROW_FIRE;
            case "FORTUNE":
                return Enchantment.LOOT_BONUS_BLOCKS;
            case "FROST_WALKER":
                return Enchantment.FROST_WALKER;
            case "INFINITY":
                return Enchantment.ARROW_INFINITE;
            case "KNOCKBACK":
                return Enchantment.KNOCKBACK;
            case "LOOTING":
                return Enchantment.LOOT_BONUS_MOBS;
            case "LUCK_OF_THE_SEA":
                return Enchantment.LUCK;
            case "LURE":
                return Enchantment.LURE;
            case "MENDING":
                return Enchantment.MENDING;
            case "POWER":
                return Enchantment.ARROW_DAMAGE;
            case "PROJECTILE_PROTECTION":
                return Enchantment.PROTECTION_PROJECTILE;
            case "PROTECTION":
                return Enchantment.PROTECTION_ENVIRONMENTAL;
            case "PUNCH":
                return Enchantment.ARROW_KNOCKBACK;
            case "RESPIRATION":
                return Enchantment.OXYGEN;
            case "SHARPNESS":
                return Enchantment.DAMAGE_ALL;
            case "SILK_TOUCH":
                return Enchantment.SILK_TOUCH;
            case "SMITE":
                return Enchantment.DAMAGE_UNDEAD;
            case "SWEEPING_EDGE":
                return Enchantment.SWEEPING_EDGE;
            case "THORNS":
                return Enchantment.THORNS;
            case "UNBREAKING":
                return Enchantment.DURABILITY;
            default:
                return null;
        }
    }

}
