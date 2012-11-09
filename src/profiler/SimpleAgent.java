package profiler;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Mar 15, 2006
 * Time: 10:22:35 AM
 */
public class SimpleAgent {

    public static void premain(String args, Instrumentation inst) {
        inst.addTransformer(new Transformer());
    }
}

class Transformer implements ClassFileTransformer {

    public byte[] transform(ClassLoader loader, String className, Class<?> c,
            ProtectionDomain pd, byte[] b) throws IllegalClassFormatException {
        System.out.print("Loading class: ");
        System.out.println(className);        
        return b;
    }
}
