package com.example.observability.core;

@FunctionalInterface
public interface SupplierWithException<T> {
    T get() throws Exception;
}