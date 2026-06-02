package com.example.platform.observability.core;

@FunctionalInterface
public interface SupplierWithException<T> {
    T get() throws Exception;
}