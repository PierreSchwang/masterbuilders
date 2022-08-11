package de.pierreschwang.masterbuilders.schematic;

import com.sk89q.worldedit.math.BlockVector3;

public interface ISchematicProvider {

    PlotSchematic getSchematic();

    int getPlotMargin();

    BlockVector3 getDimension();

}
