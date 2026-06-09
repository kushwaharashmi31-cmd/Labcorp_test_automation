Feature: LabCorp Job Apply Flow
  Scenario: Complete job validation and apply flow
    Given I open LabCorp website
    When I click on Careers link
    And I search for "QA Test Automation Developer"
    And I select first job from results
    Then I capture and validate job details
    When I click Apply Now
    Then I verify details on apply page
    And I return to job search

    
   