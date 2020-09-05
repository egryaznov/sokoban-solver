import com.company.model.CharModelFactory;
import com.company.model.Move;
import com.company.model.Sokoban;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class AIIntegrationTest
{

    private static final String FULL_PATH_TO_FIELDS_DIR = "C:\\Users\\egryazn\\Home\\untitled\\fields\\";
    private static final double BILLION = 1000000000D;

    @Test
    public void gameIntegrationTest() throws IOException
    {
        Files.walk(Paths.get(FULL_PATH_TO_FIELDS_DIR, "game"))
                .forEach(path ->
                {
                    if (!path.toFile().isFile())
                        return;
                    if (path.getFileName().toString().startsWith("!"))
                    {
                        System.out.println("Skipping " + path.getFileName());
                        return;
                    }
                    System.out.println("Solving " + path.getFileName());
                    final Sokoban sokoban = CharModelFactory.fromPath(path).make();
                    final long time = System.nanoTime();
                    final List<Move> solution = sokoban.solve();
                    final long done = System.nanoTime();
                    solution.forEach(move -> move.perform(sokoban));
                    assertTrue(sokoban.solved());
                    System.out.printf("%s solved with %d moves in %.2f sec\n",
                            path.getFileName(),
                            solution.size(),
                            (done - time) / BILLION);
                });
    }

    @Test
    public void simpleIntractableIntegrationTest() throws IOException
    {
        Files.walk(Paths.get(FULL_PATH_TO_FIELDS_DIR, "intractable"))
                .forEach(path ->
                {
                    if (!path.toFile().isFile())
                        return;
                    final Sokoban sokoban = CharModelFactory.fromPath(path).make();
                    final List<Move> solution = sokoban.solve();
                    assertTrue(solution.isEmpty());
                });
    }

    @Test
    public void simpleSolvableIntegrationTest() throws IOException
    {
        Files.walk(Paths.get(FULL_PATH_TO_FIELDS_DIR, "solvable"))
                .forEach(path ->
                {
                    if (!path.toFile().isFile())
                        return;
                    final Sokoban sokoban = CharModelFactory.fromPath(path).make();
                    final long time = System.nanoTime();
                    final List<Move> solution = sokoban.solve();
                    final long done = System.nanoTime();
                    solution.forEach(move -> move.perform(sokoban));
                    assertTrue(sokoban.solved());
                    System.out.printf("%s solved with %d moves in %.2f sec\n",
                            path.getFileName(),
                            solution.size(),
                            (done - time) / BILLION);
                });
    }

}