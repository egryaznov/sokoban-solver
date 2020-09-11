package com.company.controller;

import com.company.model.Sokoban;
import com.company.model.Move;
import com.company.view.View;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ConsoleController implements Controller
{

    private final Sokoban sokoban;
    private final View view;

    public ConsoleController(final Sokoban sokoban, final View view)
    {
        this.sokoban = sokoban;
        this.view = view;
    }

    @Override
    public void run()
    {
        view.say("Sokoban Starts");
        char symbol = '0';
        view.render();
        final List<Move> history = new LinkedList<>();
        while (symbol != 'q')
        {
            final Move move = Move.resolve(symbol);
            if (move != null)
            {
                history.add(move);
                move.perform(sokoban);
                view.render();
                if (sokoban.solved())
                {
                    view.say("YOU WIN!");
                    break;
                }
            }
            try
            {
                symbol = (char) System.in.read();
            }
            catch (IOException io)
            {
                view.say("Не получилось считать команду:");
                throw new IllegalStateException(io);
            }
        }
        view.say("Your moves: " +  Move.compress(history));
    }

}