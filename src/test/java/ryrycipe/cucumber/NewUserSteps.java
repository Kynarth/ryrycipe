package ryrycipe.cucumber;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.FlowPane;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import ryrycipe.Ryrycipe;
import ryrycipe.model.Plan;

import java.util.concurrent.TimeoutException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

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

    @Given("^No plan has been selected$")
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

    @Given("^No material has been filtered$")
    public void No_material_has_been_filtered() throws Throwable {
        assertThat(materialChooser.getChildren().size(), is(0));
    }

    @When("^the user click on foraged button$")
    public void the_user_click_on_foraged_button() throws Throwable {
        clickOn("#foragedBtn");
    }

    @Then("^Foraged materials are displayed$")
    public void Foraged_materials_are_displayed() throws Throwable {
        assertThat(materialChooser.getChildren().size(), greaterThan(0));
    }

}
