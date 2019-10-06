package me.Lorinth.LRM.Objects;

import org.bukkit.entity.Entity;

public class CreatureDeathData {

    private double experience;
    private double money;
    private Entity entity;

    public CreatureDeathData(double experience, double money, Entity entity){
        this.experience = experience;
        this.money = money;
        this.entity = entity;
    }

    public double getExperience(){
        return experience;
    }

    public double getMoney() { return money; }

    public Entity getEntity(){
        return entity;
    }

}
