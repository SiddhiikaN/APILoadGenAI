package com.siddhika.apiloadgen.config;

public class LoadConfig {

    private String targetUrl;
    private int threads;
    private int durationSeconds;
    private int requestsPerSecond;

    public LoadConfig(String targetUrl, int threads, int durationSeconds, int requestsPerSecond){
        this.targetUrl = targetUrl;
        this.threads = threads;
        this.durationSeconds = durationSeconds;
        this.requestsPerSecond = requestsPerSecond;
    }

    public String getTargetUrl(){
        return targetUrl;
    }

    public int getThreads(){
        return threads;
    }
    public int getDurationSeconds(){
        return durationSeconds;
    }

    public int getRequestsPerSecond(){
        return requestsPerSecond;
    }
    
}
