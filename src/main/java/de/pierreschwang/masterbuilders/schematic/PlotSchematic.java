package de.pierreschwang.masterbuilders.schematic;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.math.BlockVector3;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class PlotSchematic {

    private final Clipboard clipboard;

    private PlotSchematic(Clipboard clipboard) {
        this.clipboard = clipboard;
    }

    public BlockVector3 getDimensions() {
        return this.clipboard.getDimensions();
    }

    public static PlotSchematic load(File file) throws IOException {
        ClipboardFormat format = Preconditions.checkNotNull(
                ClipboardFormats.findByFile(file),
                "%s is not a valid schematic file",
                file.getName()
        );
        try (InputStream stream = Files.newInputStream(file.toPath(), StandardOpenOption.READ)) {
            Clipboard clipboard = format.getReader(stream).read();
            return new PlotSchematic(clipboard);
        }
    }

    public Clipboard clipboard() {
        return clipboard;
    }
}
