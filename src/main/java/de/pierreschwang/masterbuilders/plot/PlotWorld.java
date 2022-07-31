package de.pierreschwang.masterbuilders.plot;

import com.sk89q.worldedit.WorldEditException;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.generator.ChunkGenerator;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlotWorld implements Runnable, Closeable {

    private final World world;
    private final Logger logger;
    private final PlotGrid grid;

    public PlotWorld(Logger logger, PlotGrid grid) {
        this.logger = logger;
        this.grid = grid;
        this.world = Bukkit.createWorld(
                new WorldCreator("masterbuilders_plots").generator(new ChunkGenerator() {
                }).generateStructures(false));
    }

    @Override
    public void run() {
        this.logger.info("Starting plot population - This may take a while");
        try {
            this.grid.populate();
        } catch (WorldEditException e) {
            this.logger.log(Level.SEVERE, "Plot population failed", e);
        }
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

}