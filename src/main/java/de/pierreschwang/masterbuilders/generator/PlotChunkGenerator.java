package de.pierreschwang.masterbuilders.generator;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.entity.BaseEntity;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.block.BlockStateHolder;
import de.pierreschwang.masterbuilders.schematic.extent.CachingChunkExtent;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class PlotChunkGenerator extends ChunkGenerator {

    private final CachingChunkExtent cachingChunkExtent;

    public PlotChunkGenerator(CachingChunkExtent cachingChunkExtent) {
        this.cachingChunkExtent = cachingChunkExtent;
    }

    @Override
    public @NotNull List<BlockPopulator> getDefaultPopulators(@NotNull World world) {
        return List.of(new BlockPopulator() {
            @Override
            public void populate(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull LimitedRegion limitedRegion) {
                Set<CachingChunkExtent.ChunkEntry<BaseEntity>> chunkEntries = cachingChunkExtent.chunkEntities().get(BlockVector2.at(chunkX, chunkZ));
                if (chunkEntries == null) {
                    return;
                }
                // Populate using WorldEdit to preserve nbt data etc
                try (EditSession session = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(world))) {
                    for (CachingChunkExtent.ChunkEntry<BaseEntity> entry : chunkEntries) {
                        session.createEntity(
                                new Location(session, entry.chunkPosition().toVector3(), entry.yaw(), entry.pitch()),
                                entry.data()
                        );
                    }
                    Operations.complete(session.commit());
                } catch (WorldEditException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public void generateSurface(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkData chunkData) {
        Set<CachingChunkExtent.ChunkEntry<BlockStateHolder<?>>> chunkEntries = cachingChunkExtent.chunkBlocks().get(BlockVector2.at(chunkX, chunkZ));
        if (chunkEntries == null) {
            return;
        }
        for (CachingChunkExtent.ChunkEntry<BlockStateHolder<?>> entry : chunkEntries) {
            chunkData.setBlock(
                    entry.chunkPosition().getX(),
                    entry.chunkPosition().getY(),
                    entry.chunkPosition().getZ(),
                    BukkitAdapter.adapt(entry.data().toBaseBlock())
            );
        }
    }

    @Override
    public boolean shouldGenerateSurface() {
        return true;
    }

}
