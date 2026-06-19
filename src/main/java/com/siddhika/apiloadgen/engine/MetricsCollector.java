package com.siddhika.apiloadgen.engine;

import java.util.concurrent.atomic.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class MetricsCollector {

    private final AtomicInteger totalRequests = new AtomicInteger(0);
    private final AtomicInteger successCount = new AtomicInteger(0);
    private final AtomicInteger errorCount= new AtomicInteger(0);
    private final AtomicLong totalLatency = new AtomicLong(0);
    private final List<Long> latencies = Collections.synchronizedList(new ArrayList<>());

    public void record(boolean success, long latencyMs) {
        totalRequests.incrementAndGet();
        totalLatency.addAndGet(latencyMs);
        latencies.add(latencyMs);
        if (success) successCount.incrementAndGet();
        else errorCount.incrementAndGet();
    }

    public int getTotalRequests() {
        return totalRequests.get();

    }

    public int getSuccessCount() {
        return successCount.get();

    }

    public int getErrorCount() {
        return errorCount.get();

    }

    public long getAverageLatency() {
        return totalRequests.get() == 0 ? 0 : totalLatency.get() / totalRequests.get();
    }

    public long getPercentile(int p) {

        List<Long> sorted = new ArrayList<>(latencies);
        Collections.sort(sorted);
        if(sorted.isEmpty()) return 0;
        int index = (int) Math.ceil(p / 100.0 * sorted.size()) -1 ;
        return sorted.get(Math.max(0, index));
    }
}
