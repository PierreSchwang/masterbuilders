package de.pierreschwang.masterbuilders.state;

public interface IGameState {

    void start();

    void stop();

    void tick();

    State state();

    public enum State {
        LOBBY,
        GAME,
        END
    }

}
