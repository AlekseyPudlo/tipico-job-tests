package com.tipico.common.config;

import com.microsoft.playwright.options.ViewportSize;

import java.io.InputStream;
import java.util.Properties;

public class PlaywrightConfig {

    private static final Properties props = new Properties();

    static {
        try (InputStream input = PlaywrightConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                props.load(input);
            } else {
                throw new RuntimeException("config.properties not found in classpath.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    private static String get(String key, String defaultValue) {
        return System.getProperty(key, props.getProperty(key, defaultValue));
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(get("headless", "true"));
    }

    public static String baseUrl() {
        return get("baseUrl", "https://www.tipico-careers.com/en/jobs/");
    }

    public static int defaultTimeout() {
        return Integer.parseInt(get("timeout", "6000"));
    }

    public static String browserType() {
        return get("browserType", "chromium").toLowerCase();
    }

    public static ViewportSize fullHDViewport() {
        return new ViewportSize(1920, 1080); // Full HD
    }

    public static ViewportSize mobileViewport() {
        return new ViewportSize(390, 844); // iPhone 12 Pro
    }

    public static ViewportSize tabletViewport() {
        return new ViewportSize(768, 1024); // iPad Pro
    }
}
