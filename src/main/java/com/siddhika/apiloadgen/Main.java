package com.siddhika.apiloadgen;

import com.siddhika.apiloadgen.config.LoadConfig;
import com.siddhika.apiloadgen.engine.LoadRunner;
import com.siddhika.apiloadgen.engine.ChaosGenerator;
import com.siddhika.apiloadgen.engine.MetricsCollector;
import com.siddhika.apiloadgen.report.ReportPrinter;
import com.siddhika.apiloadgen.report.AIAnalyser;

public class Main {

    public static void main(String[] args) throws Exception {
        String url = "http://localhost:8080/api/test";
        int threads = 10;
        int duration = 30;
        int rps = 5;

        LoadConfig config = new LoadConfig(url, threads, duration, rps);
        MetricsCollector metrics = new MetricsCollector();
        ChaosGenerator chaos = new ChaosGenerator();
        LoadRunner runner = new LoadRunner(config, metrics, chaos);

        System.out.println("Starting load test against: " + url);
        runner.run();

        new ReportPrinter(metrics).print();
        new AIAnalyser(metrics).analyse();
    }
    
}
