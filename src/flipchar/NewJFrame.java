/*
 */

/*
 * NewJFrame.java
 *
 * Created on 02.08.2011, 14:40:21
 */
package flipchar;

/**
 *
 * @author Gerrit Grunwald <han.solo at muenster.de>
 */
public class NewJFrame extends javax.swing.JFrame implements java.awt.event.ActionListener {

    private final java.util.Random RND = new java.util.Random();
    private final javax.swing.Timer TIMER1 = new javax.swing.Timer(5000, this);    
    
    /** Creates new form NewJFrame */
    public NewJFrame() {
        initComponents();
        GlobalTimer.INSTANCE.startTimer();
        TIMER1.start();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        flipChar1 = new FlipChar();
        flipChar2 = new FlipChar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Scalable FlipChar");

        org.jdesktop.layout.GroupLayout flipChar1Layout = new org.jdesktop.layout.GroupLayout(flipChar1);
        flipChar1.setLayout(flipChar1Layout);
        flipChar1Layout.setHorizontalGroup(
            flipChar1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 184, Short.MAX_VALUE)
        );
        flipChar1Layout.setVerticalGroup(
            flipChar1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 297, Short.MAX_VALUE)
        );

        org.jdesktop.layout.GroupLayout flipChar2Layout = new org.jdesktop.layout.GroupLayout(flipChar2);
        flipChar2.setLayout(flipChar2Layout);
        flipChar2Layout.setHorizontalGroup(
            flipChar2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 184, Short.MAX_VALUE)
        );
        flipChar2Layout.setVerticalGroup(
            flipChar2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 297, Short.MAX_VALUE)
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(flipChar1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 184, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(flipChar2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 184, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, flipChar2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, flipChar1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public void actionPerformed(final java.awt.event.ActionEvent EVENT) {        
        if (EVENT.getSource().equals(TIMER1)) {
            flipChar1.setCharacter(Character.valueOf((char) (RND.nextInt(30) + 65)).toString());
            flipChar2.setCharacter(Character.valueOf((char) (RND.nextInt(30) + 65)).toString());
        }               
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                NewJFrame app = new NewJFrame();
                app.setLocationRelativeTo(null);
                app.setVisible(true);                
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private FlipChar flipChar1;
    private FlipChar flipChar2;
    // End of variables declaration//GEN-END:variables
}
