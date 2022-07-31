package de.pierreschwang.masterbuilders.api;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface IHolder<T> {

    @Nullable T value();

    void value(@NonNull T value);

}
