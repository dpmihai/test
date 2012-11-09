package robot;

/**
 * Created by IntelliJ IDEA. User: mihai.panaitescu Date: Mar 9, 2005 Time: 11:15:26 AM To change this template use File
 * | Settings | File Templates.
 */
/*
 * Screenshot.java (requires Java 1.4+)
 */

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class Screenshot {
	public static void main(String[] args) throws Exception {
		// make sure we have exactly two arguments,
		// a waiting period and a file name
		if (args.length != 2) {
			System.err.println("Usage: java Screenshot " +
				"WAITSECONDS OUTFILE.png");
			System.exit(1);
		}
		// check if file name is valid
		String outFileName = args[1];
		if (!outFileName.toLowerCase().endsWith(".png")) {
			System.err.println("Error: output file name must " +
				"end with \".png\".");
			System.exit(1);
		}
		// wait for a user-specified time
		try {
			long time = Long.parseLong(args[0]) * 1000L;
			System.out.println("Waiting " + (time / 1000L) +
				" second(s)...");
			Thread.sleep(time);
		} catch(NumberFormatException nfe) {
			System.err.println(args[0] + " does not seem to be a " +
				"valid number of seconds.");
			System.exit(1);
		}
		// determine current screen size
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		Rectangle screenRect = new Rectangle(screenSize);
		// create screen shot
		Robot robot = new Robot();
		BufferedImage image = robot.createScreenCapture(screenRect);
		// save captured image to PNG file
		ImageIO.write(image, "png", new File(outFileName));
		// give feedback
		System.out.println("Saved screen shot (" + image.getWidth() +
			" x " + image.getHeight() + " pixels) to file \"" +
			outFileName + "\".");
		// use System.exit if the program hangs after writing the file;
		// that's an old bug which got fixed only recently
		// System.exit(0);
	}
}