package me.Lorinth.LRM.Command.SpawnPoint;

import me.Lorinth.LRM.Command.Objects.CustomCommandExecutor;
import me.Lorinth.LRM.Command.SpawnPointExecutor;
import org.bukkit.entity.Player;

/**
 * Created by lorinthio on 1/27/2018.
 */
public class ListSpawnPointExecutor extends CustomCommandExecutor{

    private final SpawnPointExecutor parentExecutor;

    public ListSpawnPointExecutor(SpawnPointExecutor parent) {
        super("list", "lists all spawnpoints in your current world", null);
        parentExecutor = parent;
    }

    @Override
    public void execute(Player player, String[] args) {

    }
}
