package com.company.model;

public final class Pair<T, U> {

    public static <T, U> Pair<T, U> pair(T first, U second)
    {
        return new Pair<>(first, second);
    }

    public Pair(T first, U second) {
        this.second = second;
        this.first = first;
    }

    public final T first;
    public final U second;

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}
