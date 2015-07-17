Feature: Create a new craft recipe with a new user.

  Background:
    Given User starts the application

  Scenario: Select a plan in the associated combo box
    When the new user clicks the list of plans to pick Axe one
    Then the selected plan in the combo box should be the Axe plan