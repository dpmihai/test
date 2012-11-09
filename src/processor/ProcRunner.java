//package processor;
//
///**
// * Created by IntelliJ IDEA.
// * User: mihai.panaitescu
// * Date: Nov 7, 2008
// * Time: 10:27:53 AM
// */
//
//import java.io.File;
//
//import javax.annotation.processing.Processor;
//import javax.tools.JavaCompiler;
//import javax.tools.JavaFileObject;
//import javax.tools.StandardJavaFileManager;
//import javax.tools.ToolProvider;
//import javax.tools.JavaCompiler.CompilationTask;
//
//import com.sun.tools.javac.util.List;
//
//public class ProcRunner implements Runnable {
//
//    private Processor processor;
//
//    private Iterable<File> getFiles() {
//        return Files.all("test", "*.java");
//    }
//
//    public ProcRunner(Processor processor) {
//        this.processor = processor;
//    }
//
//    public void run() {
//
//        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
//        StandardJavaFileManager fileman = compiler.getStandardFileManager(null, null, null);
//        Iterable<? extends JavaFileObject> units = fileman.getJavaFileObjectsFromFiles(this.getFiles());
//
//        CompilationTask task = compiler.getTask(null, // out
//                fileman, // fileManager
//                null, // diagnosticsListener
//                //List.of("-printsource", "-d", "out"), // options
//                List.of("-d", "bin"), // options
//                null, // classes
//                units);
//
//        task.setProcessors(List.of(processor));
//
//        task.call();
//
//    }
//
//    public static void main(String[] args) {
//        Runnable runner = new ProcRunner(new RomanNumeralProcessor());
//        runner.run();
//    }
//
//
//}
//
