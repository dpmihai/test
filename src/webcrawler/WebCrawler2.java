package webcrawler;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Set;
import java.util.HashSet;
import java.util.Queue;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Nov 4, 2008
 * Time: 12:56:38 PM
 *
 * import java.util.regex.Pattern;
import java.util.regex.Matcher;
 */
public class WebCrawler2 {

    public static void main(String[] args) {

        // timeout connection after 500 miliseconds
        System.setProperty("sun.net.client.defaultConnectTimeout", "500");
        System.setProperty("sun.net.client.defaultReadTimeout",    "1000");

        // initial web page
        String s = args[0];

        // list of web pages to be examined
        Queue<String> q = new LinkedList<String>();
        q.add(s);

        // existence symbol table of examined web pages
        Set<String> set = new HashSet<String>();
        set.add(s);

        // breadth first search crawl of web
        while (!q.isEmpty()) {
            String v = q.remove();
            System.out.println(v);

            In in = new In(v);

            // only needed in case website does not respond
            if (!in.exists()) continue;

            String input = in.readAll();

           /*************************************************************
            *  Find links of the form: http://xxx.yyy.zzz
            *  \\w+ for one or more alpha-numeric characters
            *  \\. for dot
            *  could take first two statements out of loop
            *************************************************************/
            String  regexp  = "http://(\\w+\\.)*(\\w+)";
            Pattern pattern = Pattern.compile(regexp);
            Matcher matcher = pattern.matcher(input);

            // find and print all matches
            while (matcher.find()) {
                String w = matcher.group();
                if (!set.contains(w)) {
                    q.add(w);
                    set.add(w);
                }
            }

        }
   }
}
