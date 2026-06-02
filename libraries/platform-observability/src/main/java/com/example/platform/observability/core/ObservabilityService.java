package com.example.platform.observability.core;

public interface ObservabilityService {

    <T> T track(String operation, SupplierWithException<T> supplier);

}