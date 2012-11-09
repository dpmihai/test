package reference.weak;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Aug 30, 2006
 * Time: 12:17:47 PM
 */

/*
   An object is weakly reachable when the garbage collector finds no strong or soft references, but at least
   one path to the object with a weak reference. Weakly reachable objects are finalized some time after their
   weak references have been cleared. The only real difference between a soft reference and a weak reference
   is that the garbage collector uses algorithms to decide whether or not to reclaim a softly reachable object,
   but always reclaims a weakly reachable object.

   Weak references work well in applications that need to, for example, associate extra data with an unchangeable
   object, such as a thread the application did not create. If you make a weak reference to the thread with a
   reference queue, your program can be notified when the thread is no longer strongly reachable. Upon receiving
   this notification, the program can perform any required cleanup of the associated data object.
*/
public class WeakPropertyChangeListener implements PropertyChangeListener{

    private WeakReference<PropertyChangeListener> listenerRef;
    private Object src;

    public WeakPropertyChangeListener(PropertyChangeListener listener, Object src){
        listenerRef = new WeakReference<PropertyChangeListener>(listener);
        this.src = src;
    }

    public void propertyChange(PropertyChangeEvent evt){
        PropertyChangeListener listener = (PropertyChangeListener)listenerRef.get();
        if(listener==null){
            removeListener();
        }else
            listener.propertyChange(evt);
    }

    private void removeListener(){
        try{
            Method method = src.getClass().getMethod("removePropertyChangeListener"
                    , new Class[] {PropertyChangeListener.class});
            method.invoke(src, new Object[]{ this });
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    /* Usage :
       KeyboardFocusManager focusManager =  KeyboardFocusManager.getCurrentKeyboardFocusManager();

       // instead of registering direclty use weak listener
       // focusManager.addPropertyChangeListener(focusOwnerListener);

       focusManager.addPropertyChangeListener(
       new WeakPropertyChangeListener(focusOwnerListener, focusManager));
    */
}
