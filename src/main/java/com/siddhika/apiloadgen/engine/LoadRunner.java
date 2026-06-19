package com.siddhika.apiloadgen.engine;

import com.siddhika.apiloadgen.config.LoadConfig;
import com.siddhika.apiloadgen.http.HttpClientWrapper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class LoadRunner {
    private final LoadConfig config;
    private final MetricsCollector metrics;
    private final ChaosGenerator chaosGenerator;

    public LoadRunner(LoadConfig config, MetricsCollector metrics, ChaosGenerator chaosGenerator){
        this.config = config;
        this.metrics = metrics;
        this.chaosGenerator = chaosGenerator;

    }

    public void run() throws InterruptedException {
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        long endTime = System.currentTimeMillis() + (config.getDurationSeconds()) * 1000L;

        while(System.currentTimeMillis() < endTime) {
            executor.submit(() -> {
                HttpClientWrapper http = new HttpClientWrapper();
                String payLoad = chaosGenerator.generate();
                long start = System.currentTimeMillis();
                try{
                    var response = http.post(config.getTargetUrl(), payLoad);
                    long latency = System.currentTimeMillis() - start;
                    boolean success = response.statusCode() >= 200 && response.statusCode() < 500;
                    metrics.record(success , latency);
                } catch (Exception e) {
                    long latency = System.currentTimeMillis() - start;
                    metrics.record(false, latency);
                }
            });

            Thread.sleep(1000L / config.getRequestsPerSecond());
            
        }

        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);

    }
    
}
