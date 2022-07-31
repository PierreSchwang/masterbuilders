package de.pierreschwang.masterbuilders.listener;

import de.pierreschwang.masterbuilders.MasterBuilders;
import de.pierreschwang.masterbuilders.entity.User;
import de.pierreschwang.masterbuilders.state.IGameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerLoginListener implements Listener {

    private final MasterBuilders masterBuilders;

    public PlayerLoginListener(MasterBuilders masterBuilders) {
        this.masterBuilders = masterBuilders;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (this.masterBuilders.state().state() != IGameState.State.LOBBY) {
            // TODO: support spectators
            event.setKickMessage("You can only join the game in the lobby");
            event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            return;
        }
        // populate user registry with new player
        User.wrap(event.getPlayer());
    }

}
