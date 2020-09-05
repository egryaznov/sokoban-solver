package com.company.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

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

    public boolean isCrateAt(final Point at)
    {
        return crates.contains(at);
    }

    public boolean isMarkAt(final Point at)
    {
        return marks.contains(at);
    }

    private boolean isWalkableTileAt(final Point at)
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

    private Map<Point, Point> shortestPathsFromPlayer()
    {
        final Set<Point> visited = new HashSet<>();
        final Queue<Point> toProcess = new LinkedList<>();
        final Map<Point, Point> childToParent = new HashMap<>();
        toProcess.add(this.player);
        while (!toProcess.isEmpty())
        {
            final Point parent = toProcess.remove();
            visited.add(parent);
            //
            for (final Point child : parent.adjacent())
            {
                if (field.isOutOfBounds(child) || !isWalkableTileAt(child) || visited.contains(child))
                    continue;
                childToParent.put(child, parent);
                toProcess.add(child);
            }
        }
        return childToParent;
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

    private boolean canMoveCrate(final Point playerWillStand, final Point crateWillGo, final Map<Point, Point> walkablePoints)
    {
        final boolean atLeastOneOutOfBounds = field.isOutOfBounds(playerWillStand) || field.isOutOfBounds(crateWillGo);
        final boolean atLeastOneNotWalkable = !isWalkableTileAt(playerWillStand) || !isWalkableTileAt(crateWillGo);
        if (atLeastOneNotWalkable || atLeastOneOutOfBounds)
            return false;
        return this.player.equals(playerWillStand) || walkablePoints.containsKey(playerWillStand);
    }

    private boolean isWallAt(final Point wallMaybeAt)
    {
        return field.tileAt(wallMaybeAt) == Tile.WALL;
    }

    private boolean isDeadPosition(final Point crateWillGo)
    {
        final boolean blockedHorizontally = isWallAt(crateWillGo.left()) || isWallAt(crateWillGo.right());
        final boolean blockedVertically = isWallAt(crateWillGo.up()) || isWallAt(crateWillGo.down());
        final boolean notAtMark = !marks.contains(crateWillGo);
        return blockedHorizontally && blockedVertically && notAtMark;
    }

    private Sokoban deriveChild(final Map<Point, Point> walkablePoints, final Point playerWillStand, final Point crateAt, final Point crateWillGo)
    {
        if (canMoveCrate(playerWillStand, crateWillGo, walkablePoints) && !isDeadPosition(crateWillGo))
        {
            final Set<Point> childConfiguration = new HashSet<>(this.crates);
            childConfiguration.remove(crateAt);
            childConfiguration.add(crateWillGo);
            return new Sokoban(this.field, childConfiguration, Collections.unmodifiableSet(this.marks), crateAt);
        }
        return null;
    }

    private List<Pair<Sokoban, List<Direction>>> derive()
    {
        final Map<Point, Point> walkablePoints = shortestPathsFromPlayer();
        final List<Pair<Sokoban, List<Direction>>> result = new LinkedList<>();
        for (final Point crate : crates)
        {
            final Point[][] pairs = new Point[][]{{crate.left(), crate.right()}, {crate.right(), crate.left()},
                                                  {crate.up(), crate.down()}, {crate.down(), crate.up()}};
            for (Point[] pair : pairs)
            {
                final Point playerWillStand = pair[0];
                final Point crateWillGo = pair[1];
                final Sokoban child = deriveChild(walkablePoints, playerWillStand, crate, crateWillGo);
                if (child != null)
                {
                    final LinkedList<Direction> toChild = unwindWalk(walkablePoints, playerWillStand);
                    toChild.add(crate.derive(crateWillGo));
                    result.add(Pair.pair(child, toChild));
                }
            }
        }
        return result;
    }

    private LinkedList<Direction> unwindWalk(final Map<Point, Point> walkablePoints, final Point playerWillStand)
    {
        final LinkedList<Direction> walk = new LinkedList<>();
        Point key = playerWillStand;
        while (walkablePoints.containsKey(key))
        {
            final Point before = walkablePoints.get(key);
            walk.addFirst(before.derive(key));
            key = before;
        }
        return walk;
    }

    public List<Move> solve()
    {
        final Map<Sokoban, Pair<Sokoban, List<Direction>>> childToParentAndDirection = new HashMap<>();
        final Set<Sokoban> visited = new HashSet<>();
        final Queue<Sokoban> toVisit = new LinkedList<>();
        toVisit.add(this);
        boolean found = false;
        Sokoban parent = null;
        while (!toVisit.isEmpty())
        {
            parent = toVisit.remove();
            if (parent.solved())
            {
                found = true;
                break;
            }
            visited.add(parent);
            for (final Pair<Sokoban, List<Direction>> pair : parent.derive())
            {
                final Sokoban child = pair.first;
                final List<Direction> walkFromParentToChild = pair.second;
                if (!visited.contains(child))
                {
                    childToParentAndDirection.put(child, Pair.pair(parent, walkFromParentToChild));
                    toVisit.add(child);
                }
            }
        }
        return found? unwind(parent, childToParentAndDirection) : new LinkedList<>();
    }

    private List<Move> unwind(final Sokoban root, final Map<Sokoban, Pair<Sokoban, List<Direction>>> childToParentAndDirection)
    {
        final List<Move> result = new LinkedList<>();
        Sokoban child = root;
        final LinkedList<List<Move>> moves = new LinkedList<>();
        while (childToParentAndDirection.containsKey(child))
        {
            final Pair<Sokoban, List<Direction>> pair = childToParentAndDirection.get(child);
            final Sokoban parent = pair.first;
            final List<Direction> fromParentToChild = pair.second;
            moves.addFirst(fromParentToChild.stream()
                    .map(Move::new)
                    .collect(Collectors.toList()));
            child = parent;
        }
        for (List<Move> move : moves)
            result.addAll(move);
        return result;
    }

}