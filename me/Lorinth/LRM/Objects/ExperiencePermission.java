package me.Lorinth.LRM.Objects;

import org.bukkit.entity.Player;

public class ExperiencePermission {

    private String name;
    private float multiplier;

    public ExperiencePermission(String name, float multiplier){
        this.name = name;
        this.multiplier = multiplier;
    }

    public String getName(){
        return name;
    }

    public String getPermission(){
        return "lrm.experience." + name;
    }

    public float getMultiplier(){
        return multiplier;
    }

    public boolean hasPermission(Player player){
        return player.hasPermission(this.getPermission());
    }

}
