package junit;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Aug 29, 2006
 * Time: 11:01:55 AM
 */
//public aspect SwingThreadAspect{
//
//        pointcut swingMethods():call(*javax.swing..*.*(..))
//        ||call(javax.swing..*.new(..));
//
//pointcut extendsSwing():call(*javax.swing.JComponent+.*(..))
//        ||call(*javax.swing..*Model+.*(..))
//        ||call(*javax.swing.text.Document+.*(..));
//
//pointcut safeMethods():call(void JComponent.revalidate())
//        ||call(void JComponent.invalidate(..))
//        ||call(void JComponent.repaint(..))
//        ||call(void add*Listener(EventListener+))
//        ||call(void remove*Listener(EventListener+))
//        ||call(boolean SwingUtilities.isEventDispatchThread())
//        ||call(void SwingUtilities.invokeLater(Runnable))
//        ||call(void SwingUtilities.invokeAndWait(Runnable))
//        ||call(void JTextPane.replaceSelection(..))
//        ||call(void JTextPane.insertComponent(..))
//        ||call(void JTextPane.insertIcon(..))
//        ||call(void JTextPane.setLogicalStyle(..))
//        ||call(void JTextPane.setCharacterAttributes(..))
//        ||call(void JTextPane.setParagraphAttributes(..));
//
//pointcut edtMethods():(swingMethods()||extendsSwing())&&!safeMethods();
//
//before():edtMethods(){
//        if(!SwingUtilities.isEventDispatchThread())
//        throw new AssertionError(thisJoinPointStaticPart.getSignature()
//        +" called from "+Thread.currentThread().getName());
//}
//        }