package com.company.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Point
{

    public static Point of(final int row, final int col)
    {
        return new Point(row, col);
    }

    private final int row;
    private final int col;

    public Point(final int row, final int col)
    {
        this.row = row;
        this.col = col;
    }

    public int row()
    {
        return row;
    }

    public Point translate(final Point delta)
    {
        return new Point(this.row + delta.row, this.col + delta.col);
    }

    private Point translate(final Direction direction)
    {
        return translate(direction.delta());
    }

    public Point left()
    {
        return translate(Direction.LEFT);
    }

    public Point right()
    {
        return translate(Direction.RIGHT);
    }

    public Point up()
    {
        return translate(Direction.UP);
    }

    public Point down()
    {
        return translate(Direction.DOWN);
    }

    public Direction derive(final Point adjacent)
    {
//        final Point delta = new Point(this.row - adjacent.row, this.col - adjacent.col );
        final Point delta = new Point(adjacent.row - this.row, adjacent.col - this.col);
        for (Direction direction : Direction.values())
            if (direction.delta().equals(delta))
                return direction;
        throw new IllegalArgumentException(String.format("%s to %s", this, adjacent));
    }

    public List<Point> adjacent()
    {
        return Arrays.stream(Direction.values())
                .map(this::translate)
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return row == point.row &&
                col == point.col;
    }

    @Override
    public String toString()
    {
        return String.format("(%d, %d)", row, col);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(row, col);
    }

    public int col()
    {
        return col;
    }
}
