Feature: Create a new craft recipe with a new user.

  Scenario: Select a plan in the associated combo box
    Given User starts the application
    When the new user clicks the list of plans to pick Axe one
    Then the selected plan in the combo box should be the Axe plan