package de.pierreschwang.masterbuilders.state.impl;

import de.pierreschwang.masterbuilders.MasterBuilders;
import de.pierreschwang.masterbuilders.entity.User;
import de.pierreschwang.masterbuilders.state.IGameState;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;

public class LobbyState implements IGameState {

    private int minPlayers;
    private final MasterBuilders masterBuilders;
    private int maxTime;
    private int currentTime;

    public LobbyState(MasterBuilders masterBuilders) {
        this.minPlayers = masterBuilders.configuration().minPlayers();
        this.masterBuilders = masterBuilders;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void tick() {
        if (User.getPlayers(User::playing).count() < minPlayers) {
            this.currentTime = maxTime;
            return;
        }
        if (--currentTime == 0) {
            stop();
        }
        BossBar bossBar = BossBar.bossBar(
                Component.text(currentTime + "/" + maxTime),
                currentTime / maxTime * 100,
                BossBar.Color.GREEN,
                BossBar.Overlay.PROGRESS
        );
        this.masterBuilders.showBossBar(bossBar);
    }

    @Override
    public State state() {
        return State.LOBBY;
    }
}