package com.tipico.common.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class ConsentBanner {
    private final Page page;
    private final Locator acceptAllButton;

    // Constructor
    public ConsentBanner(Page page) {
        this.page = page;
        this.acceptAllButton = page.getByRole(AriaRole.BUTTON,
            new Page.GetByRoleOptions().setName("Accept All"));
    }

    /**
     * Accepts all cookies if the consent banner is visible.
     */
    public void waitAndAcceptIfVisible() {
        try {
            acceptAllButton.waitFor();
            if (acceptAllButton.isVisible()) {
                acceptAllButton.click();
                System.out.println("[ConsentBanner] Consent banner accepted.");
            }
        } catch (Exception e) {
            System.out.println("[ConsentBanner] Consent banner not present. Proceeding.");
        }
    }
}
