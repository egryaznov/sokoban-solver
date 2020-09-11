package com.company.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class CharSokobanFactory implements SokobanFactory
{

    public static CharSokobanFactory fromPath(final Path pathToFile)
    {
        try
        {
            return new CharSokobanFactory(Files.readAllLines(pathToFile));
        }
        catch (IOException io)
        {
            System.out.println("Не получилось прочитать из файла");
            throw new IllegalStateException(io);
        }
    }

    public static CharSokobanFactory fromFile(final String fullPathToFile)
    {
        return fromPath(Paths.get(fullPathToFile));
    }

    private final List<String> charLines;

    public CharSokobanFactory(final List<String> charLines)
    {
        this.charLines = charLines;
    }

    @Override
    public Sokoban make()
    {
        int atRow = 0;
        int atCol = 0;
        final Tile[][] tiles = new Tile[charLines.size()][charLines.get(0).length()];
        Point player = null;
        final Set<Point> crates = new HashSet<>();
        final Set<Point> marks = new HashSet<>();
        for (String line : charLines)
        {
            for (int i = 0; i < line.length(); i++)
            {
                final char tileSymbol = line.charAt(i);
                final Tile tile = Tile.resolve(tileSymbol).orElseThrow(IllegalArgumentException::new);
                final Point at = new Point(atRow, atCol);
                switch (tile)
                {
                    case PLAYER:
                        player = at;
                        tiles[atRow][atCol] = Tile.GRASS;
                        break;
                    case CRATE:
                        crates.add(at);
                        tiles[atRow][atCol] = Tile.GRASS;
                        break;
                    case CRATE_ON_MARK:
                        crates.add(at);
                        marks.add(at);
                        tiles[atRow][atCol] = Tile.CRATE;
                    case MARK:
                        marks.add(at);
                        tiles[atRow][atCol] = Tile.MARK;
                        break;
                    default:
                        tiles[atRow][atCol] = tile;
                }
                atCol++;
            }
            atRow++;
            atCol = 0;
        }
        return new Sokoban(new Field(tiles), crates, marks, player);
    }
}
