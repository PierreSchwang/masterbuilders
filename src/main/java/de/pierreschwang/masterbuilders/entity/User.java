package de.pierreschwang.masterbuilders.entity;

import de.pierreschwang.masterbuilders.MasterBuildersPlugin;
import de.pierreschwang.masterbuilders.plot.Plot;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class User implements ForwardingAudience {

    private static final Map<Player, User> REGISTRY = new WeakHashMap<>();

    @Contract("null -> null")
    public static User wrap(Player player) {
        if (player == null) {
            return null;
        }
        return REGISTRY.computeIfAbsent(player, User::new);
    }

    public static Collection<User> getPlayers() {
        return REGISTRY.values();
    }

    public static Stream<User> getPlayers(Predicate<User> userPredicate) {
        return REGISTRY.values().stream().filter(userPredicate);
    }

    private Iterable<Audience> audiences;
    private final Player player;
    private final boolean playing;

    protected User(Player player) {
        this.player = player;
        this.audiences = List.of(MasterBuildersPlugin.AUDIENCE_PROVIDER.player(player.getUniqueId()));
        playing = false;
    }

    public Player player() {
        return player;
    }

    public boolean playing() {
        return playing;
    }

    public @Nullable Plot plot(Collection<Plot> plots) {
        if (!playing) {
            return null;
        }
        return plots.stream().filter(plot -> Objects.equals(plot.owner(), this))
                .findFirst().orElse(null);
    }

    @Override
    public @NotNull Iterable<? extends Audience> audiences() {
        return audiences;
    }
}
