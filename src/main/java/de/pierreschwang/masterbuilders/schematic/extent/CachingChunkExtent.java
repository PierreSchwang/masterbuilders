package de.pierreschwang.masterbuilders.schematic.extent;

import com.sk89q.worldedit.entity.BaseEntity;
import com.sk89q.worldedit.entity.Entity;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockStateHolder;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CachingChunkExtent implements Extent {

    private final Map<BlockVector2, Set<ChunkEntry<BlockStateHolder<?>>>> chunkBlocks;
    private final Map<BlockVector2, Set<ChunkEntry<BaseEntity>>> chunkEntities;


    public CachingChunkExtent() {
        this.chunkBlocks = new HashMap<>();
        this.chunkEntities = new HashMap<>();
    }

    @Override
    public BlockVector3 getMinimumPoint() {
        return null;
    }

    @Override
    public BlockVector3 getMaximumPoint() {
        return null;
    }

    @Override
    public List<? extends Entity> getEntities(Region region) {
        return null;
    }

    @Override
    public List<? extends Entity> getEntities() {
        return null;
    }

    @Nullable
    @Override
    public Entity createEntity(Location location, BaseEntity entity) {
        BlockVector2 chunk = toChunk(location.toVector().toBlockPoint());
        Set<ChunkEntry<BaseEntity>> entries = chunkEntities.computeIfAbsent(chunk, x -> new HashSet<>());
        entries.add(new ChunkEntry<>(
                toChunkPosition(location.toVector().toBlockPoint()),
                location.getYaw(), location.getPitch(), entity
        ));
        return null;
    }

    @Override
    public BlockState getBlock(BlockVector3 position) {
        return null;
    }

    @Override
    public BaseBlock getFullBlock(BlockVector3 position) {
        return null;
    }

    @Override
    public <T extends BlockStateHolder<T>> boolean setBlock(BlockVector3 position, T block) {
        BlockVector2 chunk = toChunk(position);
        Set<ChunkEntry<BlockStateHolder<?>>> entries = chunkBlocks.computeIfAbsent(chunk, x -> new HashSet<>());
        entries.add(new ChunkEntry<>(
                toChunkPosition(position),
                0f, 0f, block
        ));
        return true;
    }

    @Nullable
    @Override
    public Operation commit() {
        return null;
    }

    public Set<BlockVector2> chunks() {
        return chunkBlocks.keySet();
    }

    public Map<BlockVector2, Set<ChunkEntry<BlockStateHolder<?>>>> chunkBlocks() {
        return chunkBlocks;
    }

    public Map<BlockVector2, Set<ChunkEntry<BaseEntity>>> chunkEntities() {
        return chunkEntities;
    }

    private BlockVector2 toChunk(BlockVector3 position) {
        return BlockVector2.at(position.getX() >> 4, position.getZ() >> 4);
    }

    private BlockVector3 toChunkPosition(BlockVector3 position) {
        return BlockVector3.at(position.getX() & 15, position.getY(), position.getZ() & 15);
    }

    public record ChunkEntry<T>(BlockVector3 chunkPosition, float yaw, float pitch, T data) {
    }

}