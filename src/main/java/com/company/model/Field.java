package com.company.model;

public class Field
{

    private final Tile[][] tiles;

    public Field(final Tile[][] tiles)
    {
        this.tiles = tiles;
    }

    public boolean isOutOfBounds(final Point at)
    {
        return (at.row() < 0) || (at.row() >= numberOfRows()) || (at.col() < 0) || (at.col() >= numberOfColumns());
    }

    public Tile tileAt(Point at)
    {
        if (isOutOfBounds(at))
            throw new IllegalArgumentException(at.toString());
        return tiles[at.row()][at.col()];
    }

    public int numberOfRows()
    {
        return tiles.length;
    }

    public int numberOfColumns()
    {
        return tiles[0].length;
    }
}