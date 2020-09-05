package com.company.model;

public interface Field
{

    boolean isOutOfBounds(Point at);

    Tile tileAt(Point at);

    int numberOfRows();

    int numberOfColumns();

}
