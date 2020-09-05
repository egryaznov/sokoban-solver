package com.company.model;

public class CharField implements Field
{

    private final Tile[][] tiles;

    public CharField(final Tile[][] tiles)
    {
        this.tiles = tiles;
    }

    @Override
    public boolean isOutOfBounds(final Point at)
    {
        return (at.row() < 0) || (at.row() >= numberOfRows()) || (at.col() < 0) || (at.col() >= numberOfColumns());
    }

    @Override
    public Tile tileAt(Point at)
    {
        if (isOutOfBounds(at))
            throw new IllegalArgumentException(at.toString());
        return tiles[at.row()][at.col()];
    }

    @Override
    public int numberOfRows()
    {
        return tiles.length;
    }

    @Override
    public int numberOfColumns()
    {
        return tiles[0].length;
    }
}