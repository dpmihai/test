package misc;

public class PaddingTest {
    public static void main(String[] args) {
        System.out.println("Left Padding: " + leftPad("abcd", 8));
        System.out.println("Right Padding: " + rightPad("abcd", 8));
    }

    public static String leftPad(String s, int width) {
        return String.format("%" + width + "s", s).replace(' ', '0');
    }

    public static String rightPad(String s, int width) {
        return String.format("%-" + width + "s", s).replace(' ', '0');
    }
}
