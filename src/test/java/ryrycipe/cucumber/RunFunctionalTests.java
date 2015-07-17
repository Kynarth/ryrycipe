package ryrycipe.cucumber;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * Run functional tests.
 */
@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty"})
public class RunFunctionalTests {

}
