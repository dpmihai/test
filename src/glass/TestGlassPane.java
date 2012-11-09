package glass;


import java.awt.*;

/**
 * Created by IntelliJ IDEA. User: mihai.panaitescu Date: Feb 23, 2005 Time: 11:18:32 AM To change this template use
 * File | Settings | File Templates.
 */
public class TestGlassPane {


    public static void main (String[] args)  {

        // repaint is done as we drag the mouse
        Toolkit.getDefaultToolkit().setDynamicLayout(true);

        TestFrame frame = new TestFrame(TestFrame.GLASS);
        //TestFrame frame = new TestFrame(TestFrame.INF);

//      TestWaitFrame frameanimation = new TestWaitFrame();
        frame.setVisible(true);

//        TestWaitDialog dlg = new TestWaitDialog();
//        dlg.setVisible(true);
    }
}
