import com.company.model.CharModelFactory;
import com.company.model.Move;
import com.company.model.Sokoban;
import org.junit.Test;

import static org.junit.Assert.*;

public class AIIntegrationTest
{

    @Test
    public void integrationTest1()
    {
        final Sokoban sokoban = CharModelFactory.fromFile("1").make();
        final String actual = Move.compress(sokoban.solve());
        final String expected = "5R";
        assertEquals(expected, actual);
    }

    @Test
    public void integrationTest2()
    {
        final Sokoban sokoban = CharModelFactory.fromFile("2").make();
        final String actual = Move.compress(sokoban.solve());
        final String expected = "2R 4L";
        assertEquals(expected, actual);
    }

    @Test
    public void integrationTest3()
    {
        final Sokoban sokoban = CharModelFactory.fromFile("3").make();
        final String actual = Move.compress(sokoban.solve());
        final String expected = "";
        assertEquals(expected, actual);
        assertFalse(sokoban.solved());
    }

    @Test
    public void integrationTest4()
    {
        final Sokoban sokoban = CharModelFactory.fromFile("4").make();
        final String actual = Move.compress(sokoban.solve());
        final String expected = "";
        assertEquals(expected, actual);
        assertFalse(sokoban.solved());
    }

    @Test
    public void integrationTest5()
    {
        final Sokoban sokoban = CharModelFactory.fromFile("5").make();
        final String actual = Move.compress(sokoban.solve());
        final String expected = "";
        assertEquals(expected, actual);
        assertFalse(sokoban.solved());
    }

    @Test
    public void integrationTest6()
    {
        final Sokoban sokoban = CharModelFactory.fromFile("6").make();
        final String actual = Move.compress(sokoban.solve());
        final String expected = "6L U L R D L R D L";
        assertEquals(expected, actual);
    }

    @Test
    public void integrationTest7()
    {
        final Sokoban sokoban = CharModelFactory.fromFile("7").make();
        final String actual = Move.compress(sokoban.solve());
        final String expected = "2L R D L";
        assertEquals(expected, actual);
    }

    @Test
    public void integrationTest8()
    {
        final Sokoban sokoban = CharModelFactory.fromFile("8").make();
        final String actual = Move.compress(sokoban.solve());
        final String expected = "2U L U 2R 4L D 2R U R D";
        assertEquals(expected, actual);
    }

    @Test
    public void integrationTestA1()
    {
        final Sokoban sokoban = CharModelFactory.fromFile("a1").make();
        final String actual = Move.compress(sokoban.solve());
        final String expected = "2L 2R U R 2L U D R 2D";
        assertEquals(expected, actual);
    }

    @Test
    public void integrationTestA2()
    {
        final Sokoban sokoban = CharModelFactory.fromFile("a2").make();
        final String actual = Move.compress(sokoban.solve());
        final String expected = "4D R 2D 2L U R D R U L 5U 2L D R U R 4D R 2D 2L U R U 3R D R 2U D 4L 2D R U L U 3R D R U 4L 3U 2L D R U R 3D R 2D 2L U R D R U L U 3R";
        assertEquals(expected, actual);
    }

    @Test
    public void integrationTestA3()
    {
        final Sokoban sokoban = CharModelFactory.fromFile("a3").make();
        final String actual = Move.compress(sokoban.solve());
        final String expected = "D R D R 2D 2L 2R 2U L D L R 2U L 2D 4U R 3D L D R U R D";
        assertEquals(expected, actual);
    }

    @Test
    public void integrationTestA4()
    {
        final Sokoban sokoban = CharModelFactory.fromFile("a4").make();
        final String actual = Move.compress(sokoban.solve());
        final String expected = "R 2D U 2R 2D R 2D 2L U 2L D L U 3R D 2L 4R 2U L 2U 2L 2D 2U 2R 2D R 2D 2L U 2L";
        assertEquals(expected, actual);
    }

    @Test
    public void integrationTestA5()
    {
        final Sokoban sokoban = CharModelFactory.fromFile("a5").make();
        final String actual = Move.compress(sokoban.solve());
        final String expected = "";
        assertEquals(expected, actual);
    }

}