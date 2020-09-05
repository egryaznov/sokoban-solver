package com.company.view;

import com.company.model.Sokoban;
import com.company.model.Point;
import com.company.model.Tile;

import java.io.IOException;

public class ConsoleView implements View
{

    private final Sokoban sokoban;

    public ConsoleView(final Sokoban sokoban)
    {
        this.sokoban = sokoban;
    }

    private void clearConsole()
    {
        try
        {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (InterruptedException | IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void render()
    {
        clearConsole();
        for (int i = 0; i < sokoban.numberOfFieldRows(); i++)
        {
            for (int j = 0; j < sokoban.numberOfFieldCols(); j++)
            {
                final Point at = new Point(i, j);
                final Tile tileAt = sokoban.tile(at);
                final char symbolToPrint = tileAt == Tile.CRATE && sokoban.isMarkAt(at) ? 'G' : tileAt.symbol();
                System.out.print(symbolToPrint);
            }
            System.out.println();
        }
    }
}
