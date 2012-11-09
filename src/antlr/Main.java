package antlr;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: Apr 7, 2005 Time: 4:02:47 PM
 */

import java.io.*;

class Main {
    public static void main(String[] args) {
        try {
            L lexer = new L(new DataInputStream(System.in));
            P parser = new P(lexer);
            parser.startRule();
        } catch (Exception e) {
            System.err.println("exception: " + e);
        }
    }
}

