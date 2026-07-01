package com.siddhika.apiloadgen;

import com.siddhika.apiloadgen.config.LoadConfig;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class LoadConfigTest {

    LoadConfig config;

    @BeforeEach
    void setup() {
        config = new LoadConfig("https://idk.com", 10, 15, 5);
    }

    @Test
    void shouldReturnTargetUrl() {
        assertEquals("https://idk.com", config.getTargetUrl());
    }

    @Test
    void shouldReturnThreads() {
        assertEquals(10, config.getThreads());
    }

    @Test
    void shouldReturnDuration() {
        assertEquals(15, config.getDurationSeconds());
    }

    @Test
    void shouldReturnRps() {
        assertEquals(5, config.getRequestsPerSecond());
    }
    
}
