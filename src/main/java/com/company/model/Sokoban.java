package com.company.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Sokoban
{

    private final Field field;
    private final Set<Point> crates;
    private final Set<Point> marks;
    private Point player;

    public Sokoban(final Field field, final Set<Point> crates, final Set<Point> marks, final Point player)
    {
        this.field = field;
        this.crates = crates;
        this.marks = marks;
        this.player = player;
    }

    public Tile tile(final Point at)
    {
        if (player.equals(at))
            return Tile.PLAYER;
        //
        if (crates.contains(at))
            return Tile.CRATE;
        //
        return field.tileAt(at);
    }

    public Point getPlayer()
    {
        return player;
    }

    public Set<Point> getCrates()
    {
        return Collections.unmodifiableSet(crates);
    }

    public Set<Point> getMarks()
    {
        return Collections.unmodifiableSet(marks);
    }

    public boolean isMarkAt(final Point at)
    {
        return marks.contains(at);
    }

    public boolean isWalkableTileAt(final Point at)
    {
        return tile(at).isWalkable();
    }

    private boolean canMoveTo(final Point delta)
    {
        final Point translated = player.translate(delta);
        if (field.isOutOfBounds(translated))
            return false;
        if (isWalkableTileAt(translated))
            return true;
        final Tile moveToTile = tile(translated);
        if (moveToTile == Tile.CRATE)
            return isWalkableTileAt(translated.translate(delta));
        //
        return false;
    }

    public boolean isOutOfBounds(final Point at)
    {
        return field.isOutOfBounds(at);
    }


    private void moveCrate(final Point crateAt, final Point crateTo)
    {
        if (crates.remove(crateAt))
            crates.add(crateTo);
    }

    public void move(final Point delta)
    {
        if (!canMoveTo(delta))
            return;
        final Point translated = player.translate(delta);
        final Tile toTile = tile(translated);
        if (isWalkableTileAt(translated))
            this.player = translated;
        else if (toTile == Tile.CRATE)
        {
            final Point crateTo = translated.translate(delta);
            if (isWalkableTileAt(crateTo))
            {
                this.player = translated;
                moveCrate(translated, crateTo);
            }
        }
    }

    public boolean solved()
    {
        return marks.equals(crates);
    }

    public int numberOfFieldRows()
    {
        return field.numberOfRows();
    }

    public int numberOfFieldCols()
    {
        return field.numberOfColumns();
    }

    public Sokoban derive(final Point crateToRemove, final Point crateToInsert)
    {
        final Set<Point> childConfiguration = new HashSet<>(crates);
        childConfiguration.remove(crateToRemove);
        childConfiguration.add(crateToInsert);
        return new Sokoban(this.field, childConfiguration, Collections.unmodifiableSet(this.marks), crateToRemove);
    }

    public boolean isWallAt(final Point wallMaybeAt)
    {
        return field.tileAt(wallMaybeAt) == Tile.WALL;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sokoban that = (Sokoban) o;
        return Objects.equals(crates, that.crates) && Objects.equals(player, that.player);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(crates);
    }

}