package de.pierreschwang.masterbuilders.schematic.provider;

import de.pierreschwang.masterbuilders.schematic.ISchematicProvider;
import de.pierreschwang.masterbuilders.schematic.PlotSchematic;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Calling {@link #getSchematic()} will return a random schematic based on {@link #possibleSchematics}.
 * The margin is determined by the biggest dimension of all possible schematics.
 */
public class RandomSchematicProvider implements ISchematicProvider {

    private static final Random RANDOM = new Random(System.currentTimeMillis());
    private final Collection<PlotSchematic> possibleSchematics;
    private final int margin;

    public RandomSchematicProvider(Collection<PlotSchematic> possibleSchematics) {
        this.possibleSchematics = possibleSchematics;
        this.margin = this.possibleSchematics.stream()
                .mapToInt(value -> Math.max(value.getDimensions().getX(), value.getDimensions().getZ()))
                .max().orElseThrow() + 16;
    }

    @Override
    public PlotSchematic getSchematic() {
        return this.possibleSchematics.stream()
                .skip(RANDOM.nextInt(this.possibleSchematics.size()))
                .findFirst()
                .orElseThrow();
    }

    @Override
    public int getPlotMargin() {
        return this.margin;
    }

    public static RandomSchematicProvider ofFiles(Collection<File> files) {
        return new RandomSchematicProvider(files.stream().map(file -> {
            try {
                return PlotSchematic.load(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList()));
    }

}
