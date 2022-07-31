package de.pierreschwang.masterbuilders.state;

import de.pierreschwang.masterbuilders.api.IHolder;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class StateHolder implements IHolder<IGameState> {

    private IGameState currentState;

    @Override
    public @Nullable IGameState value() {
        return this.currentState;
    }

    @Override
    public void value(@NonNull IGameState value) {
        this.currentState.stop();
        (this.currentState = value).start();
    }

}
