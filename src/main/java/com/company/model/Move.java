package com.company.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Move
{

    public static Move resolve(final char key)
    {
        return Arrays.stream(Direction.values())
                .filter(direction -> direction.key() == key)
                .findAny()
                .map(Move::new)
                .orElse(null);
    }

    public static String compress(final List<Move> history)
    {
        if (history.isEmpty())
            return "";
        //
        final String moves = history.stream()
                .map(Objects::toString)
                .collect(Collectors.joining());
        final StringBuilder compressed = new StringBuilder();
        int count = 0;
        char prevKey = '0';
        for (int i = 0; i < moves.length(); i++)
        {
            final char key = moves.charAt(i);
            if (prevKey == key)
            {
                count += 1;
            }
            else
            {
                if (count > 0)
                {
                    if (count > 1)
                        compressed.append(count);
                    compressed.append(prevKey);
                    compressed.append(" ");
                }
                count = 1;
                prevKey = key;
            }
        }
        if (count > 1)
            compressed.append(count);
        compressed.append(prevKey);
        return compressed.toString();
    }


    private final Direction direction;

    public Move(final Direction direction)
    {
        this.direction = direction;
    }

    public void perform(Sokoban sokoban)
    {
        sokoban.move(direction.delta());
    }

    @Override
    public String toString()
    {
        return direction.name().substring(0, 1);
    }

}