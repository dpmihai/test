package mathevaluator;

import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.MapContext;
import org.apache.commons.jexl2.Expression;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.util.Calendar;

/**
 * User: mihai.panaitescu
 * Date: 29-Apr-2010
 * Time: 16:36:28
 */
public class JexlTest {

    private static final JexlEngine jexl = new JexlEngine();

    static {
        jexl.setCache(512); // number of expressions cached
        jexl.setLenient(false);
        jexl.setSilent(false);
    }


    public static void main(String[] args) {
        //booleanTest();
        //arithmeticTest();
        stringTest();
        //ifTest();
    	//ifColorTest();
        //functionTest();
        //dateFunctionTest();
    }
    
    private static boolean booleanTest() {
        String expression = "3 > 2";
        Expression e = jexl.createExpression( expression );

        JexlContext context = new MapContext();
        Boolean result = (Boolean)e.evaluate(context);
        System.out.println("result="+result);

        return result;
    }

    private static void arithmeticTest() {
        String calculateTax = "((G1 + G2 + G3) * 0.1) + G4";
        Expression e = jexl.createExpression( calculateTax );

        // populate the context
        JexlContext context = new MapContext();
        context.set("G1", 1560.5);
        context.set("G2", 20);
        context.set("G3", 115.75);
        context.set("G4", 800);

        // work it out
        Double result = (Double) e.evaluate(context);

        System.out.println("result="+result);
    }

    private static void stringTest() {
        String exp = "G1.toUpperCase() + space + G2";
        Expression e = jexl.createExpression( exp );

        // populate the context
        JexlContext context = new MapContext();
        context.set("G1", "Gicu");
        context.set("space", " ");
        context.set("G2", "Bitu");

        String result = (String) e.evaluate(context);

        System.out.println("result="+result);


    }

    private static void ifTest() {
       String exp = "if (((x * 2) == 4) && (G1 > 14)) { y = G1; } else { y = (x*10); }";
        Expression e = jexl.createExpression( exp );

        // populate the context
        JexlContext context = new MapContext();
        context.set("x", 2);
        context.set("G1", 14);
        context.set("G2", 35);

        Integer result = (Integer) e.evaluate(context);

        System.out.println("result="+result);
    }
    
    private static void ifColorTest() {
    	    	
    	// populate the context
    	Map<String, Object> funcs = new HashMap<String, Object>();
        funcs.put("color", new MyColor());
        JexlEngine jexl = new JexlEngine();
        jexl.setFunctions(funcs);
        
        JexlContext context = new MapContext();
        context.set("i", 3);
        context.set("r1", 240);
        context.set("g1", 240);
        context.set("b1", 255);
        context.set("r2", 210);
        context.set("g2", 210);
        context.set("b2", 210);        
        
        String exp = "if ((i % 2) == 0) { color:get(r1,g1,b1); } else { color:get(r2,g2,b2); }";
    	Expression e = jexl.createExpression( exp );
        
        Color result = (Color) e.evaluate(context);        
        System.out.println("color="+result);
    }
    
    public static class MyColor {
    	public Color get(int r, int g, int b) {
    		return new Color(r,g,b);
    	}
    }

    private static void functionTest() {

        Map<String, Object> funcs = new HashMap<String, Object>();
        funcs.put("math", new MyMath());
        JexlEngine jexl = new JexlEngine();
        jexl.setFunctions(funcs);

        JexlContext jc = new MapContext();
        jc.set("pi", Math.PI);

        Expression e = jexl.createExpression("math:cos(pi)");
        Double result = (Double)e.evaluate(jc);
        System.out.println("result="+result);
    }

    public static class MyMath {
        public double cos(double x) {
            return Math.cos(x);
        }
    }

    public static class MyDate {
        public int compare(Date d1, Date d2) {
            return d1.compareTo(d2);
        }

        public Date addDays(Date d, int days) {
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            c.add(Calendar.DAY_OF_YEAR, days);
            return c.getTime();
        }

        public int getHour(Date d){
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            return c.get(Calendar.HOUR_OF_DAY);
        }
    }

    // test : evaluate -> JexlException
    private static void dateFunctionTest() {

        Map<String, Object> funcs = new HashMap<String, Object>();
        funcs.put("date", new MyDate());
        JexlEngine jexl = new JexlEngine();
        jexl.setFunctions(funcs);

        JexlContext jc = new MapContext();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, -1);
        Date d1 = c.getTime();
        jc.set("D1", d1);
        jc.set("D2", new Date());
        jc.set("days", 2);

        Expression e = jexl.createExpression("date:compare(D1, D2)");
        Object result = e.evaluate(jc);
        System.out.println("Compare dates = " + result);
        
        e = jexl.createExpression("date:addDays(D2, days)");
        result = e.evaluate(jc);
        System.out.println("Add days = " + result);

        e = jexl.createExpression("date:getHour(D2)");
        result = e.evaluate(jc);
        System.out.println("Current hour = " + result);

    }
}
