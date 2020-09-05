package com.company.controller;

import com.company.model.Sokoban;
import com.company.model.Move;
import com.company.view.View;

import java.util.List;

public class AIController implements Controller
{

    private final View view;
    private final Sokoban sokoban;
    private final int waitTimeInMillis;

    public AIController(final Sokoban sokoban, final View view, final int waitTimeInMillis)
    {
        this.view = view;
        this.sokoban = sokoban;
        this.waitTimeInMillis = waitTimeInMillis;
    }

    @Override
    public void run()
    {
        System.out.println("AI starts solving");
        final List<Move> solution = sokoban.solve();
        if (solution.isEmpty())
            System.out.println("Solution not found, exiting");
        else
        {
            System.out.println("Solution found, playing...");
            sleep();
            view.render();
            for (Move move : solution)
            {
                move.perform(sokoban);
                view.render();
                sleep();
            }
            System.out.println("Done. Moves are: " + Move.compress(solution));
        }
    }

    private void sleep()
    {
        try
        {
            Thread.sleep(waitTimeInMillis);
        }
        catch (InterruptedException i)
        {
            throw new IllegalStateException(i);
        }
    }

}
