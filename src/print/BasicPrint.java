package print;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: May 7, 2007
 * Time: 1:57:00 PM
 */

import java.io.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import javax.print.*;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.Sides;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSize;
import javax.swing.*;

public class BasicPrint {
    public static void main(String[] args) {
        try {

            String fileName = "E:\\temp\\test.doc";

            // Open the image file
            InputStream is = new BufferedInputStream(
                    new FileInputStream(fileName));

            // Find the default service
            DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
            PrintService service = PrintServiceLookup.lookupDefaultPrintService();

            // Create the print job
            final DocPrintJob job = service.createPrintJob();
            Doc doc = new SimpleDoc(is, flavor, null);

//            // build a Set of Objects describing the special handling we want
//            PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
//            // we want 5 copies of each page.
//            aset.add(new Copies(4));
//            // we want ISO standard size A4 paper (slightly smaller than 8.5x11)
//            aset.add(MediaSize.ISO.A4);
//            // we want two-sided printing
//            aset.add(Sides.DUPLEX);
            PrintRequestAttributeSet aset = null;

            // Monitor print job events; for the implementation of PrintJobWatcher,
            // see e702 Determining When a Print Job Has Finished
            PrintJobWatcher pjDone = new PrintJobWatcher(job);

            // Sample code to display a cancel button
//            JFrame frame = new JFrame();
//            if (job instanceof CancelablePrintJob) {
//                JButton btn = new JButton("Cancel Print Job");
//                btn.addActionListener(new ActionListener() {
//                    public void actionPerformed(ActionEvent evt) {
//                        CancelablePrintJob cancelJob = (CancelablePrintJob) job;
//                        try {
//                            cancelJob.cancel();
//                        } catch (PrintException e) {
//                            // Possible reason is job was already finished
//                        }
//                    }
//                });
//
//                frame.getContentPane().add(btn, BorderLayout.CENTER);
//                frame.pack();
//                frame.setVisible(true);
//            }
            

            // Print it
            job.print(doc, aset);

            // Wait for the print job to be done
            pjDone.waitForDone();

            // Remove frame
//            frame.setVisible(false);

            // It is now safe to close the input stream
            is.close();
        } catch (PrintException e) {
            if (e.getCause() instanceof java.awt.print.PrinterAbortException) {
                // Print job was cancelled
            } else {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}