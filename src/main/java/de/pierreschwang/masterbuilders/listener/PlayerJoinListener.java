package de.pierreschwang.masterbuilders.listener;

import de.pierreschwang.masterbuilders.MasterBuilders;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final MasterBuilders masterBuilders;

    public PlayerJoinListener(MasterBuilders masterBuilders) {
        this.masterBuilders = masterBuilders;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().teleport(masterBuilders.plotWorld().world().getSpawnLocation());
    }

}
