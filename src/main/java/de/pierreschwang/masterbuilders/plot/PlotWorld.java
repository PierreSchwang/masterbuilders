package de.pierreschwang.masterbuilders.plot;

import com.sk89q.worldedit.math.BlockVector2;
import de.pierreschwang.masterbuilders.generator.PlotChunkGenerator;
import de.pierreschwang.masterbuilders.schematic.extent.CachingChunkExtent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlotWorld implements Runnable, Closeable {

    private final World world;
    private final Logger logger;
    private final CachingChunkExtent cachingChunkExtent;

    public PlotWorld(Logger logger, PlotGrid grid) {
        this.logger = logger;
        this.cachingChunkExtent = new CachingChunkExtent();
        logger.log(Level.INFO, "wtf");
        grid.populate(cachingChunkExtent);
        this.world = Bukkit.createWorld(
                new WorldCreator("masterbuilders_plots").generator(new PlotChunkGenerator(cachingChunkExtent))
        );
        Objects.requireNonNull(world).setAutoSave(false);
    }

    @Override
    public void run() {
        this.logger.log(Level.INFO, "Pre-generating {} chunks - This may take a while", this.cachingChunkExtent.chunks().size());
        for (BlockVector2 chunk : this.cachingChunkExtent.chunks()) {
            world.loadChunk(chunk.getX(), chunk.getZ(), true);
        }
        this.logger.info("Chunk pre-generation finished");
    }

    @Override
    public void close() throws IOException {
        if (this.world == null) {
            return;
        }
        Bukkit.unloadWorld(this.world, false);
        Files.walk(new File(".", this.world.getName()).toPath())
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    public World world() {
        return world;
    }
}