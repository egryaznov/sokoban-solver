package com.company.model;

public enum Direction
{

    UP(-1, 0, 'k'),
    DOWN(1, 0, 'j'),
    RIGHT(0, 1, 'l'),
    LEFT(0, -1, 'h');

    private final Point delta;
    private final char key;

    Direction(final int row, final int col, final char key)
    {
        this.delta = new Point(row, col);
        this.key = key;
    }

    public Point delta()
    {
        return this.delta;
    }

    public char key()
    {
        return this.key;
    }

}
