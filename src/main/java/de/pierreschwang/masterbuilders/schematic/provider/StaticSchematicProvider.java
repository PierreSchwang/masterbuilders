package de.pierreschwang.masterbuilders.schematic.provider;

import de.pierreschwang.masterbuilders.schematic.ISchematicProvider;
import de.pierreschwang.masterbuilders.schematic.PlotSchematic;

/**
 * Every call to {@link #getSchematic()} returns the same schematic.
 * Therefor every plot will use the same static schematic.
 */
public class StaticSchematicProvider implements ISchematicProvider {

    private final PlotSchematic schematic;
    private final int margin;

    public StaticSchematicProvider(PlotSchematic schematic) {
        this.schematic = schematic;
        this.margin = Math.max(schematic.getDimensions().getX(), schematic.getDimensions().getZ()) + 16;
    }

    @Override
    public PlotSchematic getSchematic() {
        return this.schematic;
    }

    @Override
    public int getPlotMargin() {
        return this.margin;
    }

}