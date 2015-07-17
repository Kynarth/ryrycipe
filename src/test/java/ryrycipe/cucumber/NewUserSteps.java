package ryrycipe.cucumber;

import com.sun.javafx.robot.impl.FXRobotHelper;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import ryrycipe.Ryrycipe;
import ryrycipe.controller.ComponentViewController;
import ryrycipe.mediator.impl.MediateCreatorComponentCtrlers;
import ryrycipe.model.Plan;
import ryrycipe.util.LanguageUtil;

import java.util.ResourceBundle;
import java.util.concurrent.TimeoutException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Functional tests for a new user.
 */
public class NewUserSteps extends FxRobot {

    private static ComboBox<Plan> planCB;
    private static FlowPane materialChooser;

    /**
     * Launch the Ryrycipe application once.
     */
    @Before
    public void setupApplication() {
        if (FxToolkit.toolkitContext().getRegisteredStage() == null) {
            try {
                FxToolkit.registerPrimaryStage();
                FxToolkit.setupApplication(Ryrycipe.class);
            } catch (TimeoutException e) {
                e.printStackTrace();
            }

            // Setup controls
            planCB = nodes().lookup("#planCB").queryFirst();
            materialChooser = nodes().lookup("#materialChooser").queryFirst();
        }
    }

    // Scenario: Select a plan in the associated combo box

    @Given("^no plan has been selected$")
    public void no_plan_has_been_selected() throws Throwable {
        assertThat(planCB.getValue(), is(nullValue()));
    }

    @When("^the new user clicks the list of plans to pick (\\w+) one$")

    public void the_new_user_clicks_the_list_of_plans_to_pick_one(String plan) throws Throwable {
        clickOn("#planCB").clickOn(plan);
    }

    @Then("^the selected plan in the combo box should be the (\\w+) plan$")
    public void the_selected_plan_in_the_combo_box_should_be_the_plan(String plan) throws Throwable {
        assertThat(planCB.getValue().getName(), is(plan));
    }

    // Scenario: Display foraged material

    @Given("^no material has been filtered$")
    public void no_material_has_been_filtered() throws Throwable {
        assertThat(materialChooser.getChildren().size(), is(0));
    }

    @When("^the user click on foraged button$")
    public void the_user_click_on_foraged_button() throws Throwable {
        clickOn("#foragedBtn");
    }

    @Then("^foraged materials are displayed$")
    public void foraged_materials_are_displayed() throws Throwable {
        assertThat(materialChooser.getChildren().size(), greaterThan(0));
    }

    // Scenario: Add enough units of the first display material into the recipe to fill the corresponding component

    @Given("^the component is empty$")
    public void the_component_is_empty() throws Throwable {
        MediateCreatorComponentCtrlers.getInstance().getComponentViewControllerList().stream().filter(
            ComponentViewController::isSelected
        ).forEach(controller -> assertThat(controller.getNeededMaterialNb(), is(controller.getComponent().getAmount())));
    }

    @When("^the user double click the first displayed material$")
    public void the_user_double_click_the_first_displayed_material() throws Throwable {
        doubleClickOn(materialChooser.getChildren().get(0));
    }

    @Then("^a popup appears to ask how many number of this material to add$")
    public void a_popup_appears_to_ask_how_many_number_of_this_material_to_add() throws Throwable {
        assertThat(
            FXRobotHelper.getStages().get(1).getTitle(),
            is(ResourceBundle.getBundle("lang", LanguageUtil.getLocale()).getString("dialog.title"))
        );
    }

    @When("^user push enter button$")
    public void user_push_enter_button() throws Throwable {
        push(KeyCode.ENTER);
    }

    @Then("^associated component is filled by selected material$")
    public void associated_component_is_filled_by_selected_material() throws Throwable {
        MediateCreatorComponentCtrlers.getInstance().getComponentViewControllerList().stream().filter(
            ComponentViewController::isSelected
        ).forEach(controller -> {
            assertThat(controller.getNeededMaterialNb(), is(0));
            assertThat(controller.getMaterialsContainer().getChildren().size(), is(1));
        });
    }
}
