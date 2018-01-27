package me.Lorinth.LRM.Command;

import org.bukkit.entity.Player;

/**
 * Created by bnra2 on 1/27/2018.
 */
public abstract class CustomCommandExecutor {
    public abstract String getCommandName();
    public abstract String getCommandDescription();
    public abstract void execute(Player player, String[] args);
}
