package com.company;

import com.company.ai.AStarSolver;
import com.company.ai.BFSolver;
import com.company.ai.Solver;
import com.company.controller.AIController;
import com.company.controller.ConsoleController;
import com.company.model.CharSokobanFactory;
import com.company.model.Move;
import com.company.model.Pair;
import com.company.model.Sokoban;
import com.company.view.ConsoleView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Main
{

    private static final String FULL_PATH_TO_FIELDS_DIR = "C:\\Users\\egryazn\\Home\\untitled\\fields\\";
    private static final double MILLION = 1000000D;

    public static void main(String[] args) throws IOException
    {
        run(args);
//        benchmark();
    }

    private static void run(String[] args)
    {
        if (args.length == 0)
        {
            System.out.println("Enter filename as first arg");
            return;
        }
        final Sokoban sokoban = CharSokobanFactory.fromFile(FULL_PATH_TO_FIELDS_DIR + args[0] + ".txt").make();
        if (args.length == 1)
            new ConsoleController(sokoban, new ConsoleView(sokoban)).run();
        else
        {
            final Solver solver = args[1].length() > 1 ? new BFSolver() : new AStarSolver();
            new AIController(sokoban, new ConsoleView(sokoban), 300, solver).run();
        }
    }

    private static void benchmark() throws IOException
    {
        final String[] dirs = new String[]{"game", "solvable"};
        final Solver[] solvers = new Solver[]{new BFSolver(), new AStarSolver()};
        for (String dir : dirs)
        {
            Files.walk(Paths.get(FULL_PATH_TO_FIELDS_DIR, dir))
                    .forEach(path ->
                    {
                        if (!path.toFile().isFile())
                            return;
                        if (path.getFileName().toString().startsWith("!"))
                            return;
                        final Sokoban sokoban = CharSokobanFactory.fromPath(path).make();
                        final List<Pair<Integer, Double>> stats = solve(sokoban, solvers);
                        final int diffLen = stats.get(1).first - stats.get(0).first;
                        final double diffTime = stats.get(1).second - stats.get(0).second;
                        System.out.printf(" %s | %d | %.2f millis\n", path.getFileName(), diffLen, diffTime);
                    });
        }
    }

    private static List<Pair<Integer, Double>> solve(final Sokoban start, final Solver... solvers)
    {
        final List<Pair<Integer, Double>> result = new LinkedList<>();
        final ExecutorService service = Executors.newFixedThreadPool(2);
        final List<Future<Pair<Integer, Double>>> futures = new LinkedList<>();
        for (final Solver solver : solvers)
        {
            futures.add(service.submit(() ->
            {
                final long time = System.nanoTime();
                final List<Move> solution = solver.solve(start);
                final double millis = (System.nanoTime() - time) / MILLION;
                return Pair.pair(solution.size(), millis);
            }));
        }
        try
        {
            service.shutdown();
            service.awaitTermination(2, TimeUnit.MINUTES);
            return futures.stream()
                    .map(future -> {
                        try
                        {
                            return future.get();
                        } catch (InterruptedException | ExecutionException e)
                        {
                            e.printStackTrace();
                            throw new IllegalStateException();
                        }
                    })
                    .collect(Collectors.toList());
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        return result;
    }

}