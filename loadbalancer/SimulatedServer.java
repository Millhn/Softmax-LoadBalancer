package com.example.loadbalancer;

import java.util.Random;

public class SimulatedServer implements Server {

    private final int id;
    private double baseLatency;
    private final Random random;

    public SimulatedServer(int id, double baseLatency) {
        this.id = id;
        this.baseLatency = baseLatency;
        this.random = new Random();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public double getLatency() {

        // Non-stationary drift
        double drift = (random.nextDouble() - 0.5) * 2.0;

        // Random noise
        double noise = random.nextDouble() * 5.0;

        baseLatency += drift;

        return Math.max(1.0, baseLatency + noise);
    }
}