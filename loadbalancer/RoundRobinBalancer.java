package com.example.loadbalancer;

public class RoundRobinBalancer implements LoadBalancer {

    private int currentIndex = 0;
    private final int k;

    public RoundRobinBalancer(int k) {
        this.k = k;
    }

    @Override
    public int selectServer() {
        int selected = currentIndex;
        currentIndex = (currentIndex + 1) % k;
        return selected;
    }

    @Override
    public void update(int serverIndex, double latency) {
        // No learning
    }

    @Override
    public String getName() {
        return "RoundRobin";
    }
}