package com.example.loadbalancer;

public interface LoadBalancer {

    int selectServer();

    void update(int serverIndex, double latency);

    String getName();
}