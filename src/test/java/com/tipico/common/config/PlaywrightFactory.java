package com.tipico.common.config;

import com.microsoft.playwright.*;


public class PlaywrightFactory {

    private static Playwright playwright;
    private static Browser browser;
    private static BrowserContext context;
    private static Page page;

    public static void initialize() {
        if (playwright == null) {
            playwright = Playwright.create();
            browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                .setHeadless(PlaywrightConfig.isHeadless())
                .setTimeout(PlaywrightConfig.defaultTimeout())
            );
            context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(PlaywrightConfig.fullHDViewport())
            );
            page = context.newPage();
        }
    }

    public static Page getPage() {
        if (page == null) {
            throw new IllegalStateException("Playwright was not initialized.");
        }
        return page;
    }

    public static void cleanup() {
        if (context != null) context.close();
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
        playwright = null;
        browser = null;
        context = null;
        page = null;
    }
}
