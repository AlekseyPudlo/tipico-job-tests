package com.tipico.common.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.junit.jupiter.api.Assertions;

public class FilterJobsForm {
    private final Page page;
    private final Locator form;

    private final Locator keywordInput;
    private final Locator locationSelect;
    private final Locator countrySelect;
    private final Locator departmentSelect;
    private final Locator applyFiltersButton;

    public FilterJobsForm(Page page) {
        this.page = page;
        this.form = page.locator("form.job-search");

        this.keywordInput = form.getByLabel("Keywords");
        this.locationSelect = form.getByLabel("Location");
        this.countrySelect = form.getByLabel("Country");
        this.departmentSelect = form.locator("#l-department");
        this.applyFiltersButton = form.locator("button[type='submit']");
    }

    // Actions

    public void enterKeyword(String keyword) {
        keywordInput.fill(keyword);
        verifyFilterValue("Keyword", keywordInput, keyword, true);
    }

    public void selectLocation(String location) {
        locationSelect.selectOption(location);
        verifyFilterValue("Location", locationSelect, location, false);
    }

    public void selectCountry(String country) {
        countrySelect.selectOption(country);
        verifyFilterValue("Country", countrySelect, country, false);
    }

    public void selectDepartment(String department) {
        departmentSelect.selectOption(department);
        verifyFilterValue("Department", departmentSelect, department, false);
    }

    public void submitFilters() {
        applyFiltersButton.click();
    }

    // Verifications

    private void verifyFilterValue(String filterName, Locator element, String expectedValue, boolean isInput) {
        String actualValue = isInput 
            ? element.inputValue()
            : element.evaluate("select => select.value").toString();
            
        Assertions.assertEquals(expectedValue, actualValue,
            String.format("%s filter value mismatch. Expected: %s, Actual: %s", 
                filterName, expectedValue, actualValue));
    }
}
