import com.company.ai.BinaryHeap;
import com.company.ai.Heap;
import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.*;

public class BinaryHeapTest
{

    @Test
    public void removeFromEmptyTest()
    {
        final Heap<Integer> patient = new BinaryHeap<>();
        assertTrue(patient.remove().isEmpty());
    }

    @Test
    public void removeTwo()
    {
        final Heap<Integer> patient = new BinaryHeap<>();
        final int expected = 1;
        patient.insert(expected);
        patient.insert(expected + 1);
        patient.remove();
        final int actual = patient.remove().orElse(-1);
        assertEquals(expected, actual);
        assertTrue(patient.isEmpty());
    }

    @Test
    public void removeSingle()
    {
        final Heap<Integer> patient = new BinaryHeap<>();
        final int expected = Integer.MAX_VALUE;
        patient.insert(expected);
        final int actual = patient.remove().orElse(-1);
        assertEquals(expected, actual);
        assertTrue(patient.isEmpty());
    }

    @Test
    public void removeNReversedTest()
    {
        final int n = 10;
        final Heap<Integer> patient = new BinaryHeap<Integer>(Comparator.reverseOrder());
        for (int i = 0; i < n; i++)
            patient.insert(i);
        //
        int prev = Integer.MIN_VALUE;
        while (!patient.isEmpty())
        {
            final int cur = patient.remove().orElse(Integer.MIN_VALUE);
            System.out.printf("%d > %d\n", cur, prev);
            assertTrue(cur > prev);
            prev = cur;
        }
    }

    @Test
    public void removeNTest()
    {
        final int n = 10;
        final Heap<Integer> patient = new BinaryHeap<>();
        for (int i = 0; i < n; i++)
            patient.insert(i);
        //
        int prev = Integer.MAX_VALUE;
        while (!patient.isEmpty())
        {
            final int cur = patient.remove().orElse(Integer.MAX_VALUE);
            System.out.printf("%d < %d\n", cur, prev);
            assertTrue(cur < prev);
            prev = cur;
        }
    }

    @Test
    public void elevateTest1()
    {
        final Heap<Integer> patient = new BinaryHeap<>();
        final int expected = 7;
        patient.insert(1);
        patient.insert(2);
        patient.insert(3);
        patient.insert(4);
        patient.insert(5);
        patient.insert(6);
        patient.elevate(patient.size() - 1, expected);
        final long actual = patient.remove().orElse(-1);
        assertEquals(expected, actual);
    }

    @Test
    public void elevateTest2()
    {
        final Heap<Integer> patient = new BinaryHeap<>();
        final int promoted = 7;
        patient.insert(1);
        patient.insert(2);
        patient.insert(3);
        patient.insert(4);
        patient.insert(5);
        final int expected = 9;
        patient.insert(expected);
        patient.elevate(patient.size() - 1, promoted);
        final int actual = patient.remove().orElse(-1);
        assertEquals(expected, actual);
    }

}
