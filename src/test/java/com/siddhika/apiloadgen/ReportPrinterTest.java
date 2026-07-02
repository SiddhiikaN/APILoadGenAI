package com.siddhika.apiloadgen;

import com.siddhika.apiloadgen.engine.MetricsCollector;
import com.siddhika.apiloadgen.report.ReportPrinter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ReportPrinterTest {
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream capturedOut;
    private MetricsCollector metrics;
    private ReportPrinter printer;

    @BeforeEach
    void setup() {
        capturedOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(capturedOut));
        metrics = new MetricsCollector();
        printer = new ReportPrinter(metrics);
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void shouldPrintReportHeader() {
        printer.print();
        String output = capturedOut.toString();
        assertTrue(output.contains("========== LOAD TEST REPORT =========="));
        assertTrue(output.contains("====================================="));
    }

    @Test 
    void shouldPrintCorrectMetrics() {
        
        metrics.record(true, 100);
        metrics.record(true, 200);
        metrics.record(false, 300);
        
        printer.print();
        String output = capturedOut.toString();
        assertTrue(output.contains("Total requests  : 3"));
        assertTrue(output.contains("Success         : 2"));
        assertTrue(output.contains("Error           : 1"));
        assertTrue(output.contains("Avg Latency     : 200 ms"));
        assertTrue(output.contains("p50 Latency     : 200 ms"));
        assertTrue(output.contains("p95 Latency     : 300 ms"));
        assertTrue(output.contains("p99 Latency     : 300 ms"));
    }
    
}
