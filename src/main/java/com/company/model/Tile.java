package com.company.model;

import java.util.Optional;

public enum Tile
{

    WALL('#', false),
    GRASS('.', true),
    CRATE('O', false),
    MARK('X', true),
    CRATE_ON_MARK('G', false),
    PLAYER('@', true);

    public static Optional<Tile> resolve(final char symbol)
    {
        for (Tile tile : Tile.values())
            if (tile.symbol == symbol)
                return Optional.of(tile);
        //
        return Optional.empty();
    }

    private final char symbol;
    private final boolean walkable;

    Tile(final char symbol, final boolean walkable)
    {
        this.symbol = symbol;
        this.walkable = walkable;
    }

    public char symbol()
    {
        return this.symbol;
    }

    public boolean isWalkable()
    {
        return walkable;
    }

}
