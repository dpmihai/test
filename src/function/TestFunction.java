package function;



/**
 * Created by IntelliJ IDEA. User: mihai.panaitescu Date: Jan 25, 2005 Time: 5:25:42 PM To change this template use File
 * | Settings | File Templates.
 */
public class TestFunction implements Function {
    public double f(double x) {
        return 0.01*x*x+10;
        //return 10;
    }
}
