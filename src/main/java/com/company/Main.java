package com.company;

import com.company.controller.AIController;
import com.company.controller.ConsoleController;
import com.company.model.CharModelFactory;
import com.company.model.Sokoban;
import com.company.view.ConsoleView;

public class Main
{
    public static void main(String[] args)
    {
        final Sokoban sokoban = CharModelFactory.fromFile(args[0]).make();
        if (args.length <= 1)
            new ConsoleController(sokoban, new ConsoleView(sokoban)).run();
        else
            new AIController(sokoban, new ConsoleView(sokoban), 100).run();
    }

}