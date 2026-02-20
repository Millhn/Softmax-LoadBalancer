package com.example.loadbalancer;

public class SimulationResult {

    private final String algorithmName;
    private final double averageLatency;

    public SimulationResult(String algorithmName, double averageLatency) {
        this.algorithmName = algorithmName;
        this.averageLatency = averageLatency;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public double getAverageLatency() {
        return averageLatency;
    }
}