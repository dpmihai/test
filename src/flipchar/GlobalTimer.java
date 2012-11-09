package flipchar;

/**
 *
 * @author hansolo
 */
public enum GlobalTimer implements java.awt.event.ActionListener {

    INSTANCE;
    
    // <editor-fold defaultstate="collapsed" desc="Variable declaration">        
    private static final java.awt.event.ActionEvent FLIP_EVENT = new java.awt.event.ActionEvent("flip", java.awt.event.ActionEvent.ACTION_PERFORMED, "flip");
    private static final java.awt.event.ActionEvent FLIP_SEQUENCE_EVENT = new java.awt.event.ActionEvent("flipSequence", java.awt.event.ActionEvent.ACTION_PERFORMED, "flipSequence");
    private javax.swing.Timer flipTimer = new javax.swing.Timer(120, this);
    private javax.swing.Timer flipSequenceTimer = new javax.swing.Timer(10, this);    
    private int currentSequence = 0;
    private java.util.Set<FlipChar> componentSet = new java.util.HashSet<FlipChar>(32);
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Misc">    
    public void startTimer() {
        flipTimer.start();
    }

    public void stopTimer() {
        flipTimer.stop();
    }

    public void addComponent(FlipChar component) {
        componentSet.add(component);
    }

    public void removeComponent(FlipChar component) {
        componentSet.remove(component);
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Action Listener">    
    @Override
    public void actionPerformed(java.awt.event.ActionEvent event) {
        if (event.getSource().equals(flipTimer)) {
            for (FlipChar component : componentSet) {
                component.actionPerformed(FLIP_EVENT);
            }

            flipSequenceTimer.start();
        }

        if (event.getSource().equals(flipSequenceTimer)) {
            if (currentSequence == 10) {
                currentSequence = 0;
                flipSequenceTimer.stop();
            }

            for (FlipChar component : componentSet) {
                component.actionPerformed(FLIP_SEQUENCE_EVENT);
            }
            currentSequence++;
        }
    }
    // </editor-fold>
}
