Feature: Create a new craft recipe with a new user.

  Scenario: Select a plan in the associated combo box
    Given No plan has been selected
    When the new user clicks the list of plans to pick Axe one
    Then the selected plan in the combo box should be the Axe plan

  Scenario: Display foraged material
    Given No material has been filtered
    When the user click on foraged button
    Then Foraged materials are displayed