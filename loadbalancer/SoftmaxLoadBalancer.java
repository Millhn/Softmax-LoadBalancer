package com.example.loadbalancer;

import java.util.Random;

public class SoftmaxLoadBalancer implements LoadBalancer {

    private final double[] qValues;
    private final double tau;
    private final double alpha;
    private final Random random;

    public SoftmaxLoadBalancer(int k, double tau, double alpha) {
        this.qValues = new double[k];
        this.tau = tau;
        this.alpha = alpha;
        this.random = new Random();
    }

    @Override
    public int selectServer() {

        double maxQ = Double.NEGATIVE_INFINITY;

        for (double q : qValues) {
            if (q > maxQ) {
                maxQ = q;
            }
        }

        double sumExp = 0.0;
        double[] probabilities = new double[qValues.length];

        // Log-Sum-Exp trick for stability
        for (int i = 0; i < qValues.length; i++) {
            probabilities[i] =
                    Math.exp((qValues[i] - maxQ) / tau);
            sumExp += probabilities[i];
        }

        for (int i = 0; i < probabilities.length; i++) {
            probabilities[i] /= sumExp;
        }

        double r = random.nextDouble();
        double cumulative = 0.0;

        for (int i = 0; i < probabilities.length; i++) {
            cumulative += probabilities[i];
            if (r < cumulative) {
                return i;
            }
        }

        return probabilities.length - 1;
    }

    @Override
    public void update(int serverIndex, double latency) {

        double reward = -latency;

        qValues[serverIndex] =
                qValues[serverIndex] +
                        alpha * (reward - qValues[serverIndex]);
    }

    @Override
    public String getName() {
        return "Softmax";
    }
}