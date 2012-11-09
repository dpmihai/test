package htmlparser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.*;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLEditorKit.Parser;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Feb 16, 2007
 * Time: 9:40:03 AM
 */
public class HtmlParser {
    final Parser parser;

    public static void main(String[] args) throws IOException {
        HtmlParser scanner = new HtmlParser();
        final Map<Tag, Integer> map = scanner.scanHierarchy(new File("D:\\Public\\test\\src\\htmlparser\\bankier.html"));
        //sort keys by values
        List<Tag> list = new ArrayList<Tag>(map.keySet());
        Collections.sort(list,
                new Comparator<Tag>() {
                    public int compare(Tag key1, Tag key2) {
                        return map.get(key2) - map.get(key1);
                    }
                });
        for (Tag t : list) {
            System.out.format("%-10s %s\n", t, map.get(t));
        }
    }

    public HtmlParser() {
        parser = (new ScannerHTMLEditorKit()).getParser();
    }

    public Map<Tag, Integer> scanHierarchy(File file) throws FileNotFoundException, IOException {
        Map<Tag, Integer> map = new HashMap<Tag, Integer>();
        scanHierarchyImpl(file, map);
        return map;
    }

    private void scanHierarchyImpl(File file, Map<Tag, Integer> map) throws FileNotFoundException, IOException {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                scanHierarchyImpl(f, map);
            }
        } else if (file.isFile()) {
            String name = file.getName();
            if (name.endsWith(".html")
                    || name.endsWith(".htm")) {
                scan(file, map);
            }

        }
    }

    public void scan(File file, Map<Tag, Integer> map) throws FileNotFoundException, IOException {
        scan(new BufferedReader(new FileReader(file)), map);
    }

    public Map<Tag, Integer> scan(File file) throws FileNotFoundException, IOException {
        return scan(new BufferedReader(new FileReader(file)));
    }

    public void scan(Reader in, Map<Tag, Integer> map) throws IOException {
        parser.parse(in, new ScannerParserCallback(map), true);
    }

    public Map<Tag, Integer> scan(Reader in) throws IOException {
        Map<Tag, Integer> map = new HashMap<Tag, Integer>();
        scan(in, map);
        return map;
    }
//we need this class only to get the default html parser
//the returned parser creates a new parser on every parse call

    //so one parser is enough
    private static class ScannerHTMLEditorKit extends HTMLEditorKit {
        @Override
        public Parser getParser() {
            return super.getParser();
        }
    }


    HTMLEditorKit.ParserCallback callback =
            new HTMLEditorKit.ParserCallback() {
                public void handleText(char[] data, int pos) {
                    System.out.println(data);
                }
            };


    private static class ScannerParserCallback extends HTMLEditorKit.ParserCallback {
        final Map<Tag, Integer> map;
      
        ScannerParserCallback(Map<Tag, Integer> map) {
            this.map = map;
        }

        @Override
        public void handleStartTag(Tag t, MutableAttributeSet a, int pos) {
            System.out.println("tag =" + t.toString());
            if ((t.toString()).compareTo("table") == 0) {
                System.out.println("TABLE ");
            }
            if ((t.toString()).compareTo("td") == 0) {
                System.out.println("TD ");

            }
            handleSimpleTag(t, a, pos);
        }

        @Override
        public void handleSimpleTag(Tag t, MutableAttributeSet a, int pos) {
            Integer integer = map.get(t);
            int counter = (integer != null) ? integer.intValue() : 0;
            map.put(t, ++counter);
        }

        public void handleText(char[] data, int pos) {
            System.out.println(data);
        }

        public void handleEndTag(Tag t, int pos) {
            if ((t.toString()).compareTo("table") == 0) {
                System.out.println("TABLE END");
            }
            System.out.println("tag end = " + t);
        }
    }
}

