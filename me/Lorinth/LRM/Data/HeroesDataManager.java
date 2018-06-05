package me.Lorinth.LRM.Data;

import com.herocraftonline.heroes.Heroes;
import com.herocraftonline.heroes.characters.CharacterManager;
import com.herocraftonline.heroes.characters.CharacterTemplate;
import com.herocraftonline.heroes.characters.Hero;
import com.herocraftonline.heroes.characters.Monster;
import com.herocraftonline.heroes.characters.classes.HeroClass;
import me.Lorinth.LRM.Listener.HeroesEventListener;
import me.Lorinth.LRM.Objects.*;
import me.Lorinth.LRM.Util.Calculator;
import me.Lorinth.LRM.Util.ConfigHelper;
import me.Lorinth.LRM.Util.MetaDataConstants;
import me.Lorinth.LRM.Util.OutputHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Used for adding heroes support
 */
public class HeroesDataManager extends Disableable implements DataManager{

    private HashMap<Integer, String> PartyFormulas = new HashMap<>();
    private CharacterManager characterManager;
    private HeroesEventListener heroesEventListener;

    public void loadData(FileConfiguration config, Plugin plugin){
        if(!ConfigHelper.ConfigContainsPath(config, "Heroes.Enabled")){
            OutputHandler.PrintInfo("Heroes options not found, Generating...");
            setDefaults(config, plugin);
        }

        if(Bukkit.getServer().getPluginManager().getPlugin("Heroes") == null)
            this.setDisabled(true);
        else{
            this.setDisabled(!config.getBoolean("Heroes.Enabled"));
            for(String key : config.getConfigurationSection("Heroes.PartyExperienceFormulas").getKeys(false)){
                try{
                    PartyFormulas.put(Integer.parseInt(key), config.getString("Heroes.PartyExperienceFormulas." + key));
                }
                catch(Exception ex){
                    OutputHandler.PrintException("Failed to load Heroes.PartyFormula with key, " + key, ex);
                    this.setDisabled(true);
                }
            }
        }

        if(this.isDisabled())
            OutputHandler.PrintInfo("Heroes Integration is Disabled");
        else {
            OutputHandler.PrintInfo("Heroes Integration is Enabled!");
            heroesEventListener = new HeroesEventListener();
            characterManager = Heroes.getInstance().getCharacterManager();
            Bukkit.getPluginManager().registerEvents(heroesEventListener, plugin);
        }
    }

    //Unused method
    public boolean saveData(FileConfiguration config){
        return false;
    }

    public boolean handleEntityDeathEvent(EntityDeathEvent deathEvent,  Player player, int exp){
        if(this.isDisabled())
            return false;

        Location location = deathEvent.getEntity().getLocation();
        Hero hero = Heroes.getInstance().getCharacterManager().getHero(player);
        if(!hero.hasParty()){
            heroesEventListener.bindExperienceEvent(location, new CreatureDeathData(exp, deathEvent.getEntity()));
            return true;
        }

        Set<Hero> inRangeMembers = new HashSet();
        for (Hero partyMember : hero.getParty().getMembers()) {
            if ((location.getWorld().equals(partyMember.getPlayer().getLocation().getWorld())) &&

                    (location.distanceSquared(partyMember.getPlayer().getLocation()) <= 2500.0D)) {
                if (partyMember.canGain(HeroClass.ExperienceType.KILLING)) {
                    inRangeMembers.add(partyMember);
                }
            }
        }
        int partySize = inRangeMembers.size();

        int highest = 0;
        String formula = "";
        for(Integer key : PartyFormulas.keySet()){
            if(key <= partySize && key > highest){
                highest = key;
                formula = PartyFormulas.get(key);
            }
        }

        if(!formula.equalsIgnoreCase("")){
            formula = formula.replace("{exp}", Integer.toString(exp));
            exp = (int) Calculator.eval(formula);
        }

        heroesEventListener.bindExperienceEvent(deathEvent.getEntity().getLocation(), new CreatureDeathData(exp, deathEvent.getEntity()));

        return true;
    }

    public boolean handleEntityDamageEvent(LivingEntity entity, double damage){
        if(this.isDisabled())
            return false;

        CharacterTemplate template = characterManager.getCharacter(entity);
        if(template instanceof Monster) {
            ((Monster) template).setDamage(damage);
        }
        return true;
    }

    private void setDefaults(FileConfiguration config, Plugin plugin){
        config.set("Heroes.Enabled", false);
        config.set("Heroes.PartyExperienceFormulas.2", "1.5 * {exp} / 2");
        config.set("Heroes.PartyExperienceFormulas.3", "1.8 * {exp} / 3");
        config.set("Heroes.PartyExperienceFormulas.4", "2.0 * {exp} / 4");
        config.set("Heroes.PartyExperienceFormulas.5", "2.2 * {exp} / 5");
        config.set("Heroes.PartyExperienceFormulas.6", "2.4 * {exp} / 6");

        plugin.saveConfig();
    }

}
