package de.pierreschwang.masterbuilders;

import de.pierreschwang.masterbuilders.api.IHolder;
import de.pierreschwang.masterbuilders.config.MasterBuildersConfiguration;
import de.pierreschwang.masterbuilders.plot.PlotGrid;
import de.pierreschwang.masterbuilders.plot.PlotWorld;
import de.pierreschwang.masterbuilders.schematic.provider.RandomSchematicProvider;
import de.pierreschwang.masterbuilders.state.IGameState;
import de.pierreschwang.masterbuilders.state.StateHolder;
import de.pierreschwang.masterbuilders.state.impl.LobbyState;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * ForwardingAudience includes all players and console.
 */
public class MasterBuilders implements Closeable, Runnable, ForwardingAudience {

    private final MasterBuildersPlugin plugin;
    private Iterable<Audience> allAudiences;
    private final IHolder<IGameState> gameStateHolder = new StateHolder();

    private PlotWorld plotWorld;
    private PlotGrid grid;

    protected MasterBuilders(MasterBuildersPlugin plugin) {
        this.plugin = plugin;
        this.allAudiences = List.of(MasterBuildersPlugin.AUDIENCE_PROVIDER.all());
    }

    public void switchState(IGameState state) {
        this.gameStateHolder.value(state);
    }

    public IGameState state() {
        return this.gameStateHolder.value();
    }

    @Override
    public void run() {
        this.grid = PlotGrid.create(
                RandomSchematicProvider.ofFiles(Arrays.asList(plugin.getDataFolder().listFiles())),
                this.configuration().teams()
        );
        this.plotWorld = new PlotWorld(plugin.getLogger(), this.grid);
        this.plotWorld.run();
        switchState(new LobbyState(this));
    }

    @Override
    public void close() throws IOException {
        this.plotWorld.close();
    }

    public MasterBuildersConfiguration configuration() {

    }

    @Override
    public @NotNull Iterable<? extends Audience> audiences() {
        return allAudiences;
    }
}