package testng;

import org.testng.annotations.Test;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Aug 30, 2006
 * Time: 3:04:54 PM
 */
public class DependentTest {

    // method depends on entire group
    @Test(dependsOnGroups = {"init.*" }, groups = "fast")
    public void method1() {
    }

    @Test(groups = {"init"})
    public void serverStartedOk() {
    }

    @Test(groups = {"init"})
    public void initEnvironment() {
    }



}
