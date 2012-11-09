//package processor;
//
///**
// * Created by IntelliJ IDEA.
// * User: mihai.panaitescu
// * Date: Nov 7, 2008
// * Time: 10:25:34 AM
// */
//
//import com.sun.source.util.Trees;
//import com.sun.tools.javac.processing.JavacProcessingEnvironment;
//import com.sun.tools.javac.tree.JCTree;
//import com.sun.tools.javac.tree.JCTree.JCIdent;
//import com.sun.tools.javac.tree.TreeMaker;
//import com.sun.tools.javac.tree.TreeTranslator;
//
//import javax.annotation.processing.*;
//import javax.lang.model.SourceVersion;
//import javax.lang.model.element.Element;
//import javax.lang.model.element.ElementKind;
//import javax.lang.model.element.TypeElement;
//import javax.tools.Diagnostic;
//import java.util.Set;
//
//// http://www.iam.unibe.ch/~akuhn/blog/2008/11/roman-numerals-in-your-java/
//
//@SupportedAnnotationTypes("*")
//@SupportedSourceVersion(SourceVersion.RELEASE_6)
//public class RomanNumeralProcessor extends AbstractProcessor {
//
//	private int tally = 0;
//
//    // class Inliner extends TreeTranslator, an internal class of the Java compiler.
//    // Transform visits all statements in the source code, and replaces each variable
//    // whose name matches a Roman numeral with an int literal of the same numeric value.
//
//    private class Inliner extends TreeTranslator {
//		@Override
//		public void visitIdent(JCIdent tree) {
//			String name = tree.getName().toString();
//			if (isRoman(name)) {
//				result = make.Literal(numberize(name));
//				result.pos = tree.pos;
//				tally++;
//			} else {
//				super.visitIdent(tree);
//			}
//		}
//	}
//
//	private Trees trees;
//	private TreeMaker make;
//
//	@Override
//	public synchronized void init(ProcessingEnvironment processingEnv) {
//		make = TreeMaker.instance(((JavacProcessingEnvironment) processingEnv).getContext());
//		trees = Trees.instance(processingEnv);
//		super.init(processingEnv);
//	}
//
//	@Override
//	public boolean process(Set<? extends TypeElement> annotations,	RoundEnvironment roundEnv) {
//		if (!roundEnv.processingOver()) {
//			Set<? extends Element> elements = roundEnv.getRootElements();
//			for (Element each : elements) {
//				if (each.getKind() == ElementKind.CLASS) {
//					JCTree tree = (JCTree) trees.getTree(each);
//					TreeTranslator visitor = new Inliner();
//					tree.accept(visitor);
//				}
//			}
//		} else
//			processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,
//					tally + " roman numerals processed.");
//		return false;
//	}
//
//	private final static String[] LETTERS = { "M", "CM", "D", "CD", "C", "XC",
//			"L", "XL", "X", "IX", "V", "IV", "I" };
//	private final static int[] VALUES = { 1000, 900, 500, 400, 100, 90, 50, 40,
//			10, 9, 5, 4, 1 };
//
//	public static String romanize(int value) {
//		String roman = "";
//		int n = value;
//		for (int i = 0; i < LETTERS.length; i++) {
//			while (n >= VALUES[i]) {
//				roman += LETTERS[i];
//				n -= VALUES[i];
//			}
//		}
//		return roman;
//	}
//
//	public static int numberize(String roman) {
//		int start = 0, value = 0;
//		for (int i = 0; i < LETTERS.length; i++) {
//			while (roman.startsWith(LETTERS[i], start)) {
//				value += VALUES[i];
//				start += LETTERS[i].length();
//			}
//		}
//		return start == roman.length() ? value : -1;
//	}
//
//	public static boolean isRoman(String roman) {
//		return roman.equals(romanize(numberize(roman)));
//	}
//
//}
