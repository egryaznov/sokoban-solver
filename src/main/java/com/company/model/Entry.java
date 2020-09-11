package com.company.model;

public class Entry implements Comparable<Entry>
{
    public static Entry of(int key, Sokoban value)
    {
        return new Entry(key, value);
    }

    private final int key;
    private final Sokoban configuration;

    public Entry(int key, Sokoban configuration)
    {
        this.key = key;
        this.configuration = configuration;
    }

    public int getKey()
    {
        return key;
    }

    public Sokoban getConfiguration()
    {
        return configuration;
    }

    @Override
    public int compareTo(Entry o)
    {
        return o.key - this.key;
    }
}