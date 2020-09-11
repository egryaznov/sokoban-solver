package com.company.controller;

import com.company.ai.Solver;
import com.company.model.Sokoban;
import com.company.model.Move;
import com.company.view.View;

import java.util.List;

public class AIController implements Controller
{

    private final View view;
    private final Sokoban sokoban;
    private final int waitTimeInMillis;
    private final Solver solver;

    public AIController(final Sokoban sokoban, final View view, final int waitTimeInMillis, final Solver solver)
    {
        this.view = view;
        this.sokoban = sokoban;
        this.waitTimeInMillis = waitTimeInMillis;
        this.solver = solver;
    }

    @Override
    public void run()
    {
        view.say("AI starts solving using " + solver.getClass().getName());
        final List<Move> solution = solver.solve(sokoban);
        if (solution.isEmpty())
            view.say("Solution not found, exiting");
        else
        {
            view.say("Solution found, playing...");
            sleep();
            view.render();
            for (Move move : solution)
            {
                move.perform(sokoban);
                view.render();
                sleep();
            }
            view.say("Done. Moves are: " + Move.compress(solution));
            view.say("Length: " + solution.size());
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
