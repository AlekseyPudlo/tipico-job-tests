package com.tipico.common.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.junit.jupiter.api.Assertions;

import java.util.List;

public class CareerPage {
    private final Page page;

    // Main container locators
    private final Locator jobListing;
    private final Locator jobCards;

    // Pagination locators
    private final Locator nextPageButton;

    // Job card element locators
    private final Locator jobCardTeamName;
    private final Locator jobCardPositionName;
    private final Locator jobCardLocation;
    private final Locator jobCardSaveButton;
    private final Locator jobCardRemoveButton;

    public CareerPage(Page page) {
        this.page = page;

        // Main container locators
        this.jobListing = page.locator("#results");
        this.jobCards = jobListing.locator(".card.card-job");

        // Pagination locators
        this.nextPageButton = page.locator(".pagination .page-next a");

        // Job card element locators
        this.jobCardTeamName = page.locator(".job-category a");
        this.jobCardPositionName = page.locator(".card-title a");
        this.jobCardLocation = page.locator(".card-subtitle");
        this.jobCardSaveButton = page.locator(".btn-add-job");
        this.jobCardRemoveButton = page.locator(".btn-remove-job");
    }

    // Actions

    public void saveJobToFavourites(String jobTitle) {
        try {
            Locator jobCard = findJobCardByTitle(jobTitle);
            Locator saveButton = jobCard.locator(jobCardSaveButton).first();
            
            saveButton.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            saveButton.click();
            
            // Wait for the save button to be replaced by remove button
            jobCard.locator(jobCardRemoveButton).first()
                .waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE));
        } catch (Exception e) {
            throw new RuntimeException(String.format("Failed to save job '%s': %s", jobTitle, e.getMessage()), e);
        }
    }

    public void removeJobFromFavourites(String jobTitle) {
        try {
            Locator jobCard = findJobCardByTitle(jobTitle);
            Locator removeButton = jobCard.locator(jobCardRemoveButton).first();
            
            removeButton.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            removeButton.click();
            
            // Wait for the remove button to be replaced by save button
            jobCard.locator(jobCardSaveButton).first()
                .waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE));
        } catch (Exception e) {
            throw new RuntimeException(String.format("Failed to remove job '%s': %s", jobTitle, e.getMessage()), e);
        }
    }

    public void goToNextPage() {
        try {
            nextPageButton.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            nextPageButton.click();
        } catch (Exception e) {
            throw new RuntimeException("Failed to navigate to next page: " + e.getMessage(), e);
        }
    }

    // Verifications

    public void verifyJobListingRequirements(int maxJobs) {
        // Verify number of jobs per page
        int jobCount = verifyNumberOfJobsDisplayed(maxJobs);
        // Verify each job card has required information
        for (int i = 0; i < jobCount; i++) {
            Locator jobCard = getJobCard(i);
            verifyJobCardContent(jobCard);
        }
    }

    public int verifyNumberOfJobsDisplayed(int maxJobs) {
        int jobCount = getJobCardsCount();
        Assertions.assertTrue(jobCount <= maxJobs, 
            String.format("Number of jobs per page (%d) exceeds maximum allowed (%d)", jobCount, maxJobs));
        return jobCount;
    }

    public void verifyJobCardContent(Locator jobCard) {
        verifyTeamName(jobCard);
        verifyPositionName(jobCard);
        verifyLocation(jobCard);
    }

    public void verifyTeamName(Locator jobCard) {
        assertElementVisibleAndNotEmpty(jobCard.locator(jobCardTeamName), "Team name");
    }

    public void verifyPositionName(Locator jobCard) {
        assertElementVisibleAndNotEmpty(jobCard.locator(jobCardPositionName), "Position name");
    }

    public void verifyLocation(Locator jobCard) {
        assertElementVisibleAndNotEmpty(jobCard.locator(jobCardLocation), "Location");
    }

    public void assertElementVisibleAndNotEmpty(Locator element, String elementName) {
        Assertions.assertTrue(element.isVisible(), elementName + " is not visible");
        Assertions.assertFalse(element.textContent().trim().isEmpty(), elementName + " is empty");
    }

    public void verifyJobSavedToFavourites(String jobTitle) {
        try {
            Locator jobCard = findJobCardByTitle(jobTitle);
            Locator removeButton = jobCard.locator(jobCardRemoveButton).first();
            
            // Wait for remove button to be visible
            removeButton.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE));
            
            // Additional verification that save button is not visible
            Locator saveButton = jobCard.locator(jobCardSaveButton).first();
            Assertions.assertFalse(saveButton.isVisible(), 
                String.format("Job '%s' save button is still visible after saving", jobTitle));
                
        } catch (Exception e) {
            throw new RuntimeException(String.format("Failed to verify job '%s' is saved: %s", 
                jobTitle, e.getMessage()), e);
        }
    }

    public void verifyJobRemovedFromFavourites(String jobTitle) {
        try {
            Locator jobCard = findJobCardByTitle(jobTitle);
            Locator saveButton = jobCard.locator(jobCardSaveButton).first();
            
            // Wait for save button to be visible
            saveButton.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE));
            
            // Additional verification that remove button is not visible
            Locator removeButton = jobCard.locator(jobCardRemoveButton).first();
            Assertions.assertFalse(removeButton.isVisible(), 
                String.format("Job '%s' remove button is still visible after removing", jobTitle));
                
        } catch (Exception e) {
            throw new RuntimeException(String.format("Failed to verify job '%s' is removed: %s", 
                jobTitle, e.getMessage()), e);
        }
    }

    // Helper methods

    private Locator findJobCardByTitle(String jobTitle) {
        // Get all job cards that contain the title
        Locator matchingCards = jobCards.filter(new Locator.FilterOptions()
            .setHas(page.locator(".card-title a", new Page.LocatorOptions()
            .setHasText(jobTitle))));
        
        int count = matchingCards.count();
        if (count == 0) {
            throw new RuntimeException(String.format("No job found with title: %s", jobTitle));
        }
        if (count > 1) {
            System.out.printf("Warning: Found %d jobs matching title '%s'. Using the first one.%n", count, jobTitle);
            // Print all matching jobs for debugging
            for (int i = 0; i < count; i++) {
                Locator card = matchingCards.nth(i);
                String fullTitle = card.locator(jobCardPositionName).textContent().trim();
                String team = card.locator(jobCardTeamName).textContent().trim();
                String location = card.locator(jobCardLocation).textContent().trim();
                System.out.printf("Match %d: %s (Team: %s, Location: %s)%n", 
                    i + 1, fullTitle, team, location);
            }
        }
        
        return matchingCards.first();
    }

    // Getter methods for job information

    public List<String> getAllJobTitles() {
        return jobCards.locator(jobCardPositionName).allTextContents();
    }

    public List<String> getAllTeamNames() {
        return jobCards.locator(jobCardTeamName).allTextContents();
    }

    public List<String> getAllLocations() {
        return jobCards.locator(jobCardLocation).allTextContents();
    }

    // Helpers

    public int getJobCardsCount() {
        return jobCards.count();
    }

    public Locator getJobCard(int index) {
        return jobCards.nth(index);
    }
}
