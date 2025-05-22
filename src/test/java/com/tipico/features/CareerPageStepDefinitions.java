package com.tipico.features;

import com.microsoft.playwright.Page;
import com.tipico.common.config.PlaywrightConfig;
import com.tipico.common.config.PlaywrightFactory;
import com.tipico.common.pages.CareerPage;
import com.tipico.common.pages.ConsentBanner;
import com.tipico.common.pages.FilterJobsForm;
import io.cucumber.java.en.*;
import io.cucumber.java.After;
import org.junit.jupiter.api.Assertions;
import java.util.List;

public class CareerPageStepDefinitions {
    private Page page;
    private ConsentBanner consentBanner;
    private CareerPage careerPage;
    private FilterJobsForm filterJobsForm;
    private String firstJobTitle;

    @Given("I open the Tipico Careers page")
    public void iOpenTheTipicoCareersPage() {
        PlaywrightFactory.initialize();
        page = PlaywrightFactory.getPage();
        consentBanner = new ConsentBanner(page);
        careerPage = new CareerPage(page);
        filterJobsForm = new FilterJobsForm(page);
        page.navigate(PlaywrightConfig.baseUrl());
        consentBanner.waitAndAcceptIfVisible();
    }

    @Then("I should see a list of job cards")
    public void iShouldSeeAListOfJobCards() {
        List<String> jobTitles = careerPage.getAllJobTitles();
        Assertions.assertFalse(jobTitles.isEmpty(), "No job cards found on the page");
    }

    @Then("each job card should display the team name")
    public void eachJobCardShouldDisplayTheTeamName() {
        int jobCount = careerPage.getJobCardsCount();
        for (int i = 0; i < jobCount; i++) {
            careerPage.verifyTeamName(careerPage.getJobCard(i));
        }
    }

    @Then("each job card should display the position name")
    public void eachJobCardShouldDisplayThePositionName() {
        int jobCount = careerPage.getJobCardsCount();
        for (int i = 0; i < jobCount; i++) {
            careerPage.verifyPositionName(careerPage.getJobCard(i));
        }
    }

    @Then("each job card should display the location")
    public void eachJobCardShouldDisplayTheLocation() {
        int jobCount = careerPage.getJobCardsCount();
        for (int i = 0; i < jobCount; i++) {
            careerPage.verifyLocation(careerPage.getJobCard(i));
        }
    }

    @Then("I should see at most {int} job cards on the current page")
    public void iShouldSeeAtMostNJobCardsOnTheCurrentPage(int maxJobs) {
        careerPage.verifyNumberOfJobsDisplayed(maxJobs);
    }

    @When("I filter jobs by country {string}")
    public void iFilterJobsByCountry(String country) {
        filterJobsForm.selectCountry(country);
        filterJobsForm.submitFilters();
        page.waitForLoadState();
    }

    @Given("I see at least one job card")
    public void iSeeAtLeastOneJobCard() {
        List<String> jobTitles = careerPage.getAllJobTitles();
        Assertions.assertFalse(jobTitles.isEmpty(), "No job cards found");
        firstJobTitle = jobTitles.get(0);
    }

    @When("I mark the first job as favourite")
    public void iMarkTheFirstJobAsFavourite() {
        careerPage.saveJobToFavourites(firstJobTitle);
    }

    @Then("the job card should be marked as favourited")
    public void theJobCardShouldBeMarkedAsFavourited() {
        careerPage.verifyJobSavedToFavourites(firstJobTitle);
    }

    @When("I unmark the same job")
    public void iUnmarkTheSameJob() {
        careerPage.removeJobFromFavourites(firstJobTitle);
    }

    @Then("the job card should no longer be marked as favourited")
    public void theJobCardShouldNoLongerBeMarkedAsFavourited() {
        careerPage.verifyJobRemovedFromFavourites(firstJobTitle);
    }

    @After
    public void tearDown() {
        PlaywrightFactory.cleanup();
    }
} 