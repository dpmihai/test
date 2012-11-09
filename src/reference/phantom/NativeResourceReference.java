package reference.phantom;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Aug 30, 2006
 * Time: 1:22:41 PM
 * To change this template use File | Settings | File Templates.
 */
import java.lang.ref.*;

/*
  An object is phantomly reachable when the garbage collector finds no strong, soft, or weak references,
  but at least one path to the object with a phantom reference. Phantomly reachable objects are objects
  that have been finalized, but not reclaimed.

   When the garbage collector finds only phantom references to an object, it enqueues the phantom reference.
   The program polls the reference queue and upon notification that the phantom reference is enqueued, performs
   post-finalization cleanup processing that requires the object to be unreachable (such as the deallocation of
   resources outside the Java heap). At the end of the post-finalization cleanup code, the program should call
   the clear() method on the phantom reference object to set the reference field to null to make the referent eligible
   for garbage collection.

*/

public class NativeResourceReference extends PhantomReference {

    private final long handle;

    @SuppressWarnings("unchecked")
    public NativeResourceReference(Object referent, long handle, ReferenceQueue q) {
        super(referent, q);
        this.handle = handle;
    }

    public void destroy() {
        try {
            // use handle to do actual native resource reclamation
            System.out.println("destroy " + this);
        } finally {
            clear();
        }
    }

    @Override
    public void clear() {
        try {
            System.out.println("clear " + this);
        } finally {
            super.clear();
        }
    }

    @Override
    public String toString() {
        return (super.toString() + " [" + this.handle + "]");
    }
}