Feature: Create a new craft recipe with a new user.

  Scenario: Select a plan in the associated combo box
    Given no plan has been selected
    When the new user clicks the list of plans to pick Axe one
    Then the selected plan in the combo box should be the Axe plan

  Scenario: Display foraged material
    Given no material has been filtered
    When the user click on foraged button
    Then foraged materials are displayed

  Scenario: Add enough units of the first display material into the recipe to fill the corresponding component
    Given the component is empty
    When the user double click the first displayed material
    Then a popup appears to ask how many number of this material to add
    When user push enter button
    Then associated component is filled by selected material