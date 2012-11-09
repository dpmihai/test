package proxy;

import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.Proxy;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Oct 5, 2007
 * Time: 11:27:15 AM
 */
public class HandlerTest {

    @SuppressWarnings({"unchecked"})
    public static void main(String[] args) {
        HandlerTest ht = new HandlerTest();
        Map map = new HashMap();
        Handler handler = new Handler(map);
        Map mapProxy = (Map) Proxy.newProxyInstance(
                ht.getClass().getClassLoader(), new Class[]{Map.class}, handler);

        handler.setUseSynch(true);
        mapProxy.put("k1", "v1");

        System.out.println("value=" + mapProxy.get("k1"));
    }
}
