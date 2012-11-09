package junit;

import java.util.Date;
import java.math.BigDecimal;

import org.junit.*;
import static org.junit.Assert.assertEquals;
import junit.framework.JUnit4TestAdapter;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Aug 28, 2006
 * Time: 4:56:01 PM
 */
public class TestOne {

    private static int test_no = 0;

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(TestOne.class);
    }

    @BeforeClass
    public static void runBeforeAllTests() {
        System.out.println("Init tests ...");
        test_no = 0;
    }

    @AfterClass
    public static void runAfterAllTests() {
        System.out.println("Finish.");
    }

    @Before
    public void runBeforeEachTest() {
        test_no++;
    }

    @After
    public void runAfterEachTest() {
    }

    @Test
    public void testSomething() {

        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        System.out.println(test_no + " : '" +  methodName + "' started at : " + new Date());

        int sum = 0;
        int n = 10000;
        for (int i=0; i<=n; i++) {
             sum += i;
        }
        System.out.println("\tsum = "  +sum);
        assertEquals(sum, (n * (n+1) / 2) );

        System.out.println(test_no + " : '" +  methodName + "' ended at : " + new Date());
    }

    @Test(timeout=3000)
    @Ignore("Not implemented")
    public void testSomething2() {

        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        System.out.println(test_no + " : '" +  methodName + "' started at : " + new Date());

        assertEquals("all", "all");

        System.out.println(test_no + " : '" +  methodName + "' ended at : " + new Date());
    }

    @Test(expected = ArithmeticException.class)
    public void divideByZero() {

        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        System.out.println(test_no + " : '" +  methodName + "' started at : " + new Date());

        BigDecimal.ONE.divide(BigDecimal.ZERO);

        System.out.println(test_no + " : '" +  methodName + "' ended at : " + new Date());
    }

}