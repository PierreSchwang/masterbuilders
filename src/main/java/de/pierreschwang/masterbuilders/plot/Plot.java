package de.pierreschwang.masterbuilders.plot;

import com.google.common.base.Preconditions;
import com.sk89q.jnbt.CompoundTag;
import com.sk89q.worldedit.math.BlockVector3;
import de.pierreschwang.masterbuilders.api.IValidatable;
import de.pierreschwang.masterbuilders.entity.User;
import org.bukkit.Location;

public class Plot implements IValidatable {

    private final BlockVector3 center;
    private BlockVector3 spawn;

    private User owner;

    public Plot(BlockVector3 center) {
        this.center = center;
    }

    public BlockVector3 center() {
        return center;
    }

    public User owner() {
        return owner;
    }

    public void owner(User owner) {
        Preconditions.checkArgument(this.owner == null, "Can't reassign plot owner");
        this.owner = owner;
    }

    /**
     * Called when during the paste of the schematic a metadata ({@code [MasterBuilders]}) sign was detected.
     *
     * @param location
     * @param nbtData
     * @return {@code true} if the sign contains a valid metadata identifier, {@code false} otherwise.
     */
    public boolean handleMetadata(BlockVector3 location, CompoundTag nbtData) {
        if (nbtData.getString("Text2").equalsIgnoreCase("Spawn")) {
            this.spawn = location;
            return true;
        }
        return false;
    }

    /**
     * Validate if this plot received all required metadata.
     *
     * @return {@code true} if this plot is valid (all required metadata signs were handled), {@code false} otherwise.
     */
    @Override
    public boolean isValid() {
        return true;
    }
}
