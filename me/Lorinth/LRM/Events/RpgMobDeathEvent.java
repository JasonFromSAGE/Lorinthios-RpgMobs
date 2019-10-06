package me.Lorinth.LRM.Events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RpgMobDeathEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean isCancelled = false;
    private Player killer;
    private Entity entity;
    private Integer level;
    private int exp;
    private double currencyValue;

    public RpgMobDeathEvent(Player killer, Entity entity, Integer level, int exp, double currencyValue){
        this.killer = killer;
        this.entity = entity;
        this.level = level;
        this.exp = exp;
        this.currencyValue = currencyValue;
    }

    /**
     * Gets a list of handlers handling this event.
     *
     * @return A list of handlers handling this event.
     */
    @Override
    public final HandlerList getHandlers(){
        return handlers;
    }

    /**
     * Gets a list of handlers handling this event.
     *
     * @return A list of handlers handling this event.
     */
    public final static HandlerList getHandlerList(){
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        isCancelled = b;
    }

    public Player getKiller(){
        return killer;
    }

    public Entity getEntity(){
        return entity;
    }

    public Integer getLevel(){
        return level;
    }

    public int getExp(){
        return exp;
    }

    public void setExp(int exp){
        this.exp = exp;
    }

    public double getCurrencyValue(){
        return currencyValue;
    }

    public void setCurrencyValue(double currencyValue){
        this.currencyValue = currencyValue;
    }
}
