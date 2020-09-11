package com.company.ai;

import com.company.model.Direction;
import com.company.model.Entry;
import com.company.model.Move;
import com.company.model.Pair;
import com.company.model.Point;
import com.company.model.Sokoban;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AStarSolver extends BFSolver
{

    private int h(final Sokoban child)
    {
        int sumOfMinDistances = 0;
        for (Point crate : child.getCrates())
        {
            sumOfMinDistances += child.getMarks().stream()
                    .mapToInt(crate::distance)
                    .min()
                    .orElse(0);
        }
        return sumOfMinDistances;
    }

    @Override
    public List<Move> solve(final Sokoban start)
    {
        final Map<Sokoban, Pair<Sokoban, List<Direction>>> parents = new HashMap<>();
        final Set<Sokoban> visited = new HashSet<>();
        final Heap<Entry> discovered = new BinaryHeap<Entry>(Comparator.reverseOrder());
        final Map<Sokoban, Integer> gScore = new HashMap<>();
        discovered.insert(Entry.of(0, start));
        gScore.put(start, 0);
        boolean found = false;
        Entry last = null;
        while (!discovered.isEmpty())
        {
            last = discovered.remove().orElseThrow();
            final Sokoban parent = last.getConfiguration();
            if (parent.solved())
            {
                found = true;
                break;
            }
            visited.add(parent);
            final int gOfParent = gScore.getOrDefault(parent, Integer.MAX_VALUE);
            for (Pair<Sokoban, List<Direction>> childAndPathFromParentToChild : deriveChildren(parent))
            {
                final Sokoban child = childAndPathFromParentToChild.first;
                final List<Direction> pathFromParentToChild = childAndPathFromParentToChild.second;
                final int tentativeGScore = gOfParent + pathFromParentToChild.size();
                final int childGScore = gScore.getOrDefault(child, Integer.MAX_VALUE);
                if (tentativeGScore < childGScore)
                {
                    gScore.put(child, tentativeGScore);
                    final int score = childGScore + h(child);
                    final Entry parentEntry = Entry.of(score, child);
                    parents.put(child, Pair.pair(parent, pathFromParentToChild));
                    if (!visited.contains(child))
                        discovered.insert(parentEntry);
                }
            }
        }
        return found ? unwind(last.getConfiguration(), parents) : new LinkedList<>();
    }

}
