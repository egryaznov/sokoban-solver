package com.company.ai;

import java.util.Optional;

/**
 * Интерфейс пирамиды, хранящей элементы, которые можно сравнивать между собой.
 * @param <T> тип элементов пирамиды, можно сравнивать друг с другом.
 */
public interface Heap<T extends Comparable<T>>
{

    /**
     * @return Возвращает корень пирамиды, не удаляя его.
     * Вернёт пустой Optional только если пирамида пуста.
     */
    Optional<T> top();

    /**
     * Вставляет новый элемент в пирамиду.
     * @param value элемент для вставки
     */
    void insert(T value);

    int size();

    /**
     * @return Возвращает корень пирамиды, удалив его.
     * Вернёт пустой Optional только если пирамида пуста.
     */
    Optional<T> remove();

    boolean isEmpty();

    void elevate(final int index, final T promotedValue);

}