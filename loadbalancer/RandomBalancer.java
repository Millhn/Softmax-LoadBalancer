package com.example.loadbalancer;

import java.util.Random;

public class RandomBalancer implements LoadBalancer {

    private final int k;
    private final Random random;

    public RandomBalancer(int k) {
        this.k = k;
        this.random = new Random();
    }

    @Override
    public int selectServer() {
        return random.nextInt(k);
    }

    @Override
    public void update(int serverIndex, double latency) {
        // No learning
    }

    @Override
    public String getName() {
        return "Random";
    }
}