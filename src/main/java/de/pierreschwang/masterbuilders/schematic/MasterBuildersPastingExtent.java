package de.pierreschwang.masterbuilders.schematic;

import com.google.gson.Gson;
import com.sk89q.jnbt.CompoundTag;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.extent.AbstractDelegateExtent;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BlockCategories;
import com.sk89q.worldedit.world.block.BlockStateHolder;
import de.pierreschwang.masterbuilders.plot.Plot;

import java.util.Map;

public class MasterBuildersPastingExtent extends AbstractDelegateExtent {

    private static final Gson GSON = new Gson();
    private final Plot plot;

    /**
     * Creates a listening extent to retrieve metadata while pasting the masterbuilders schematic.
     *
     * @param plot   The plot affected by the paste operation.
     * @param extent The underlying {@link Extent} to use.
     */
    protected MasterBuildersPastingExtent(Plot plot, Extent extent) {
        super(extent);
        this.plot = plot;
    }

    /**
     * Detect metadata signs during the paste operation and pass through metadata to plot instance.
     *
     * @param location position of the block
     * @param block    block to set
     * @param <T>
     * @return
     * @throws WorldEditException
     */
    @Override
    public <T extends BlockStateHolder<T>> boolean setBlock(BlockVector3 location, T block) throws WorldEditException {
        if (BlockCategories.SIGNS.contains(block)) {
            CompoundTag nbtData = block.toBaseBlock().getNbtData();
            if (nbtData != null) {
                Map<String, String> obj = GSON.fromJson(nbtData.getString("Text1"), Map.class);
                if (obj.get("text").equalsIgnoreCase("[MasterBuilders]") && plot.handleMetadata(location, nbtData)) {
                    return false;
                }
            }
        }
        return super.setBlock(location, block);
    }
}
