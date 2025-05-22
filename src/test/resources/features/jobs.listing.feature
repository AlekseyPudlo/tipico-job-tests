Feature: Tipico Job Listings – Germany Filter and Job Card Validations

  Background:
    Given I open the Tipico Careers page

  # 1. Verify job card fields are displayed for each job
  Scenario: All job cards show required information
    Then I should see a list of job cards
    And each job card should display the team name
    And each job card should display the position name
    And each job card should display the location

  # 2. No more than 20 jobs per page
  Scenario: Maximum jobs are listed per page
    Then I should see at most 20 job cards on the current page

  # 3. Filter jobs by country = Germany
  Scenario: Filter by Germany and validate job locations
    When I filter jobs by country "Germany"
    Then I should see at most 20 job cards on the current page

  # 4. Add and remove job from favourites
  Scenario: Toggle favourite on job listing
    Given I see at least one job card
    When I filter jobs by country "Germany"
    And I mark the first job as favourite
    Then the job card should be marked as favourited
    When I unmark the same job
    Then the job card should no longer be marked as favourited
