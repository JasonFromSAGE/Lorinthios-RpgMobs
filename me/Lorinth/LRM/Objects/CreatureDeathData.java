package me.Lorinth.LRM.Objects;

import org.bukkit.entity.Entity;

public class CreatureDeathData {

    private double experience;
    private Entity entity;

    public CreatureDeathData(double experience, Entity entity){
        this.experience = experience;
        this.entity = entity;
    }

    public double getExperience(){
        return experience;
    }

    public Entity getEntity(){
        return entity;
    }

}
