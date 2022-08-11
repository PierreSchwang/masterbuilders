package de.pierreschwang.masterbuilders.plot;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import de.pierreschwang.masterbuilders.schematic.ISchematicProvider;
import de.pierreschwang.masterbuilders.schematic.extent.CachingChunkExtent;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlotGrid {

    private static final Logger GRID_LOGGER = Logger.getLogger("MasterBuilders/PlotGrid");
    private final ISchematicProvider schematicProvider;
    private final Plot[][] grid;

    protected PlotGrid(Plot[][] grid, ISchematicProvider schematicProvider) {
        this.grid = grid;
        this.schematicProvider = schematicProvider;
    }

    public void populate(CachingChunkExtent extent) {
        for (Plot[] plots : this.grid) {
            for (Plot plot : plots) {
                if (plot == null) {
                    continue;
                }
                System.out.println("The killer is escaping");
                Operation operation = new ClipboardHolder(schematicProvider.getSchematic().clipboard())
                        .createPaste(extent)
                        .to(plot.center())
                        .build();
                try {
                    Operations.complete(operation);
                    GRID_LOGGER.info("Cached plot @ " + plot.center());
                } catch (WorldEditException exception) {
                    GRID_LOGGER.log(Level.SEVERE, "Failed to cache plot into caching extent", exception);
                }
            }
        }
    }

    public static PlotGrid create(ISchematicProvider schematicProvider, int amount) {
        Preconditions.checkArgument(amount > 0, "amount must be greater than 0");
        int width = 1;
        int height;

        Set<Integer> divisors = new HashSet<>();
        for (int i = 1; i < Math.sqrt(amount); i++) {
            if (amount % i == 0) {
                divisors.add(i);
            }
        }

        if (divisors.size() >= 2) {
            Optional<Integer> max = divisors.stream().max(Integer::compareTo);
            width = max.get();
            height = amount / width;
        } else {
            height = amount;
        }

        final Plot[][] grid = new Plot[width][height];
        int counter = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (++counter > amount) {
                    break;
                }
                BlockVector3 location = BlockVector3.at(
                        x * schematicProvider.getPlotMargin(),
                        50,
                        y * schematicProvider.getPlotMargin()
                );
                grid[x][y] = new Plot(location);
            }
        }
        return new PlotGrid(grid, schematicProvider);
    }

}