package cpu;

import java.util.Date;
import java.lang.management.ThreadMXBean;
import java.lang.management.ManagementFactory;
//
// Created by IntelliJ IDEA.
// User: mihai.panaitescu
// Date: 26-Aug-2009
// Time: 14:23:25

//
public class CPUUsage {

    public static void main(String[] args) {

        ThreadMXBean TMB = ManagementFactory.getThreadMXBean();

        long cput = 0;
        double cpuperc = -1;
        long time = new Date().getTime() * 1000000;
        while (true) {

            if (TMB.isThreadCpuTimeSupported()) {
                if (new Date().getTime() * 1000000 - time > 1000000000) //Reset once per second
                {
                    time = new Date().getTime() * 1000000;
                    cput = TMB.getCurrentThreadCpuTime();
                }

                if (!TMB.isThreadCpuTimeEnabled()) {
                    TMB.setThreadCpuTimeEnabled(true);
                }

                if ( (new Date().getTime() * 1000000 - time) != 0) {
                    cpuperc = (TMB.getCurrentThreadCpuTime() - cput) / (new Date().getTime() * 1000000.0 - time) * 100.0;
                }
            } else {
                time = new Date().getTime() * 1000000;
                cpuperc = -2;
                System.out.println("ENd");
                break;
            }
            System.out.println("usage=" + cpuperc);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
