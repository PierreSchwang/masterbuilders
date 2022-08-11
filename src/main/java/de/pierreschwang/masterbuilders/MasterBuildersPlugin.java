package de.pierreschwang.masterbuilders;

import io.papermc.lib.PaperLib;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Level;

public class MasterBuildersPlugin extends JavaPlugin {

    public static AudienceProvider AUDIENCE_PROVIDER;
    private MasterBuilders masterBuilders;

    @Override
    public void onLoad() {
    }

    @Override
    public void onEnable() {
        PaperLib.suggestPaper(this);
        AUDIENCE_PROVIDER = BukkitAudiences.create(this);
        this.masterBuilders = new MasterBuilders(this);
        this.masterBuilders.run();
    }

    @Override
    public void onDisable() {
        try {
            this.masterBuilders.close();
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Failed to disable MasterBuilders", e);
        }
    }

}
