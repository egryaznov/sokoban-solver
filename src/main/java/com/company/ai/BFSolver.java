package com.company.ai;

import com.company.model.Direction;
import com.company.model.Move;
import com.company.model.Pair;
import com.company.model.Point;
import com.company.model.Sokoban;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class BFSolver implements Solver
{

    private Map<Point, Point> shortestPathsFromPlayer(final Sokoban model)
    {
        final Set<Point> visited = new HashSet<>();
        final Queue<Point> toProcess = new LinkedList<>();
        final Map<Point, Point> childToParent = new HashMap<>();
        toProcess.add(model.getPlayer());
        while (!toProcess.isEmpty())
        {
            final Point parent = toProcess.remove();
            visited.add(parent);
            //
            for (final Point child : parent.adjacent())
            {
                if (model.isOutOfBounds(child) || !model.isWalkableTileAt(child) || visited.contains(child))
                    continue;
                childToParent.put(child, parent);
                toProcess.add(child);
            }
        }
        return childToParent;
    }

    private boolean canMoveCrate(final Sokoban model,
                                 final Point playerWillStand,
                                 final Point crateWillGo,
                                 final Map<Point, Point> walkablePoints)
    {
        final boolean atLeastOneOutOfBounds = model.isOutOfBounds(playerWillStand) || model.isOutOfBounds(crateWillGo);
        final boolean atLeastOneNotWalkable = !model.isWalkableTileAt(playerWillStand) || !model.isWalkableTileAt(crateWillGo);
        if (atLeastOneNotWalkable || atLeastOneOutOfBounds)
            return false;
        return model.getPlayer().equals(playerWillStand) || walkablePoints.containsKey(playerWillStand);
    }

    private boolean isDeadPosition(final Sokoban model, final Point crateWillGo)
    {
        final boolean blockedHorizontally = model.isWallAt(crateWillGo.left()) || model.isWallAt(crateWillGo.right());
        final boolean blockedVertically = model.isWallAt(crateWillGo.up()) || model.isWallAt(crateWillGo.down());
        final boolean notAtMark = !model.getMarks().contains(crateWillGo);
        return blockedHorizontally && blockedVertically && notAtMark;
    }

    protected List<Pair<Sokoban, List<Direction>>> deriveChildren(final Sokoban parent)
    {
        final Map<Point, Point> walkablePoints = shortestPathsFromPlayer(parent);
        final List<Pair<Sokoban, List<Direction>>> result = new LinkedList<>();
        for (final Point crate : parent.getCrates())
        {
            final Point[][] pairs = new Point[][]{{crate.left(), crate.right()}, {crate.right(), crate.left()},
                    {crate.up(), crate.down()}, {crate.down(), crate.up()}};
            for (Point[] pair : pairs)
            {
                final Point playerWillStand = pair[0];
                final Point crateWillGo = pair[1];
                if (canMoveCrate(parent, playerWillStand, crateWillGo, walkablePoints) && !isDeadPosition(parent, crateWillGo))
                {
                    final LinkedList<Direction> pathToChild = unwindWalk(walkablePoints, playerWillStand);
                    pathToChild.add(crate.derive(crateWillGo));
                    final Sokoban child = parent.derive(crate, crateWillGo);
                    result.add(Pair.pair(child, pathToChild));
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

    @Override
    public List<Move> solve(final Sokoban start)
    {
        final Map<Sokoban, Pair<Sokoban, List<Direction>>> childToParentAndDirection = new HashMap<>();
        final Set<Sokoban> visited = new HashSet<>();
        final Queue<Sokoban> toVisit = new LinkedList<>();
        toVisit.add(start);
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
            for (final Pair<Sokoban, List<Direction>> pair : deriveChildren(parent))
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

    protected List<Move> unwind(final Sokoban root, final Map<Sokoban, Pair<Sokoban, List<Direction>>> childToParentAndDirection)
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
