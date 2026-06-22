package com.siddhika.apiloadgen.report;

import com.siddhika.apiloadgen.engine.MetricsCollector;

public class ReportPrinter {
    private final MetricsCollector metrics;

    public ReportPrinter(MetricsCollector metrics) {
        this.metrics = metrics;
    }

    public void print() {
        System.out.println("\n========== LOAD TEST REPORT ==========");
        System.out.println("Total requests  : " + metrics.getTotalRequests());
        System.out.println("Sucess          : " + metrics.getSuccessCount());
        System.out.println("Error           : " + metrics.getErrorCount());
        System.out.println("Avg Latency     :" + metrics.getAverageLatency() + " ms");
        System.out.println("p50 Latency     :" + metrics.getPercentile(50) + " ms");
        System.out.println("p95 Latency     :" + metrics.getPercentile(95) + " ms");
        System.out.println("p99 Latency     :" + metrics.getPercentile(99) + " ms");
        System.out.println("=====================================\n");
    }
    
}
