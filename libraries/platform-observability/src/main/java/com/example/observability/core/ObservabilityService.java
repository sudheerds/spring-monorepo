package com.example.observability.core;

public interface ObservabilityService {

    <T> T track(String operation, SupplierWithException<T> supplier);

}