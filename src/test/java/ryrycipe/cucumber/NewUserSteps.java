package ryrycipe.cucumber;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import javafx.scene.control.ComboBox;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import ryrycipe.Ryrycipe;
import ryrycipe.model.Plan;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Functional tests for a new user.
 */
public class NewUserSteps extends FxRobot {

    @Given("^User starts the application$")
    public void User_starts_the_application() throws Throwable {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(Ryrycipe.class);
    }

    @When("^the new user clicks the list of plans to pick (\\w+) one$")
    public void the_new_user_clicks_the_list_of_plans_to_pick_one(String plan) throws Throwable {
        clickOn("#planCB").clickOn(plan);
    }

    @Then("^the selected plan in the combo box should be the (\\w+) plan$")
    public void the_selected_plan_in_the_combo_box_should_be_the_chosen_one(String plan) throws Throwable {
        ComboBox<Plan> planCB = nodes().lookup("#planCB").queryFirst();
        assertThat(planCB.getValue().getName(), is(plan));
    }
}
