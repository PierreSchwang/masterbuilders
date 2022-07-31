package de.pierreschwang.masterbuilders.config;

import de.pierreschwang.masterbuilders.schematic.ISchematicProvider;

public class MasterBuildersConfiguration {

    private ISchematicProvider schematic;

    private int teams = 12;
    private int playersPerTeam = 2;
    private int minPlayers = 6;

    public ISchematicProvider schematic() {
        return schematic;
    }

    public int teams() {
        return teams;
    }

    public int playersPerTeam() {
        return playersPerTeam;
    }

    public int minPlayers() {
        return minPlayers;
    }
}
