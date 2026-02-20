package com.example.loadbalancer;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final int K = 5;
    private static final int REQUESTS = 10000;

    public static void main(String[] args) {

        List<Server> servers = new ArrayList<>();

        for (int i = 0; i < K; i++) {
            servers.add(new SimulatedServer(i, 20 + i * 5));
        }

        List<LoadBalancer> balancers = List.of(
                new SoftmaxLoadBalancer(K, 0.5, 0.1),
                new RoundRobinBalancer(K),
                new RandomBalancer(K)
        );

        for (LoadBalancer balancer : balancers) {
            SimulationResult result =
                    runSimulation(balancer, servers);
            System.out.printf(
                    "%s -> Avg Latency: %.3f ms%n",
                    result.getAlgorithmName(),
                    result.getAverageLatency()
            );
        }
    }

    private static SimulationResult runSimulation(
            LoadBalancer balancer,
            List<Server> servers) {

        double totalLatency = 0.0;

        for (int i = 0; i < REQUESTS; i++) {

            int selected = balancer.selectServer();

            double latency = servers
                    .get(selected)
                    .getLatency();

            totalLatency += latency;

            balancer.update(selected, latency);
        }

        return new SimulationResult(
                balancer.getName(),
                totalLatency / REQUESTS
        );
    }
}