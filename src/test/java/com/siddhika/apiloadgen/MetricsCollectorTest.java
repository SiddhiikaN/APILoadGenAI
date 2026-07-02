package com.siddhika.apiloadgen;

import com.siddhika.apiloadgen.engine.MetricsCollector;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class MetricsCollectorTest {

    private MetricsCollector metrics;

    @BeforeEach
    void setup() {
        metrics = new MetricsCollector();
    }

    // record tests
    @Test
    void shouldIncreaseSuccessCount() {
        metrics.record(true, 125);
        assertEquals(1, metrics.getSuccessCount());
    }

    @Test
    void shouldIncreaseErrorCount() {
        metrics.record(false, 144);
        assertEquals(1, metrics.getErrorCount());
    }

    @Test
    void shouldIncreaseTotalRequests() {
        metrics.record(true, 100);
        metrics.record(true, 122);
        assertEquals(2, metrics.getTotalRequests());
    }

    // Average latency tests
    @Test
    void shouldReturnCorrectAverageLatency() {
        metrics.record(true, 100);
        metrics.record(true, 125);
        assertEquals(112, metrics.getAverageLatency());
    }
    
    @Test
    void shouldReturnZeroIfTotalRequestsAreZero() {
        assertEquals(0, metrics.getAverageLatency());
    }

    // Percentile tests
    @Test
    void shouldReturnZeroPercentileWhenNoLatenciesExist() {
        assertEquals(0, metrics.getPercentile(95));
    }

    @Test
    void shouldReturnCorrect50thPercentile() {
        addSampleLatencies();
        
        assertEquals(150, metrics.getPercentile(50));
    }

    @Test
    void shouldReturnCorrect95thPercentile() {
        addSampleLatencies();
        
        assertEquals(200, metrics.getPercentile(95));
    }
    
    @Test
    void shouldReturnCorrect99thPercentile() {
        addSampleLatencies();

        assertEquals(200, metrics.getPercentile(99));
    }

    private void addSampleLatencies() {
        metrics.record(true, 150);
        metrics.record(true, 200);
        metrics.record(true, 100);
    }
}
