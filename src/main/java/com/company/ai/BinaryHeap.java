package com.company.ai;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class BinaryHeap<T extends Comparable<T>> implements Heap<T>
{

    private final List<T> elements;
    private final Comparator<T> customComparator;

    public BinaryHeap()
    {
        this.elements = new ArrayList<>();
        this.customComparator = null;
    }

    public BinaryHeap(final Comparator<T> customComparator)
    {
        this.elements = new ArrayList<>();
        this.customComparator = customComparator;
    }

    private boolean outOfBounds(final int index)
    {
        return index < 0 || index >= elements.size();
    }

    private boolean inBounds(final int index)
    {
        return 0 <= index && index < elements.size();
    }

    private int parent(final int i)
    {
        return ((i + 1) >> 1) - 1;
    }

    private int left(final int i)
    {
        return ((i + 1) << 1) - 1;
    }

    private int right(final int i)
    {
        return (i + 1) << 1;
    }

    @Override
    public Optional<T> top()
    {
        return isEmpty() ? Optional.empty() : Optional.of(elements.get(0));
    }

    @Override
    public void insert(T value)
    {
        this.elements.add(value);
        promote(elements.size() - 1);
    }

    @Override
    public Optional<T> remove()
    {
        if (isEmpty())
            return Optional.empty();
        //
        final T root = at(0);
        final T last = at(elements.size() - 1);
        elements.set(0, last);
        elements.remove(elements.size() - 1);
        ensureConsistency(0);
        return Optional.of(root);
    }

    @Override
    public int size()
    {
        return elements.size();
    }

    private T first()
    {
        return at(0);
    }

    private T last()
    {
        return at(elements.size() - 1);
    }

    @Override
    public boolean isEmpty()
    {
        return elements.isEmpty();
    }

    private T at(final int index)
    {
        return elements.get(index);
    }

    @Override
    public void elevate(final int index, final T promotedValue)
    {
        if (outOfBounds(index))
            throw new IllegalArgumentException("Индекс выходит за границы пирамиды: " + index);
        //
        final T current = elements.get(index);
        if (ge(current, promotedValue))
            throw new IllegalArgumentException("Новое значение *меньше* старого");
        //
        elements.set(index, promotedValue);
        promote(index);
    }

    private void promote(final int index)
    {
        int i = index;
        while (i > 0 && (less(at(parent(i)), at(i))))
        {
            swap(i, parent(i));
            i = parent(i);
        }
    }

    private void swap(final int i, final int j)
    {
        final T iOld = elements.get(i);
        elements.set(i, elements.get(j));
        elements.set(j, iOld);
    }

    private int compare(final T first, final T second)
    {
        return (customComparator != null) ? customComparator.compare(first, second) : first.compareTo(second);
    }

    private boolean greater(final T first, final T second)
    {
        return compare(first, second) > 0;
    }

    private boolean ge(final T first, final T second)
    {
        return compare(first, second) >= 0;
    }

    private boolean less(final T first, final T second)
    {
        return compare(first, second) < 0;
    }

    private void ensureConsistency(final int badIndex)
    {
        final int left = left(badIndex);
        int greatest;
        if (inBounds(left) && greater(at(left), at(badIndex)))
            greatest = left;
        else
            greatest = badIndex;
        final int right = right(badIndex);
        if (inBounds(right) && greater(at(right), at(greatest)))
            greatest = right;
        if (greatest != badIndex)
        {
            swap(badIndex, greatest);
            ensureConsistency(greatest);
        }
    }

    @Override
    public String toString()
    {
        return elements.toString();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BinaryHeap<?> that = (BinaryHeap<?>) o;
        return elements.equals(that.elements);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(elements);
    }
}