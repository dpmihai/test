package reference.phantom;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Aug 30, 2006
 * Time: 1:28:59 PM
 */
import java.lang.ref.*;
import java.util.*;

public class NativeResourceReclaimer implements Runnable {

    private final ReferenceQueue q;
    private final Collection<Reference> refs;

    public NativeResourceReclaimer() {
        super();
        this.q = new ReferenceQueue();
        this.refs = Collections.synchronizedSet(new HashSet<Reference>());
    }

    public Reference register(Object referent, long handle) {
        Reference ref = new NativeResourceReference(referent, handle, this.q);
        this.refs.add(ref);
        return ref;
    }

    public void run() {
        while (true) {
            try {
                NativeResourceReference ref = (NativeResourceReference)this.q.remove();
                ref.destroy();
                this.refs.remove(ref);
                System.out.println("remove " + ref);
            } catch (InterruptedException exc) {
                Thread.currentThread().interrupt();
            }
        }
    }
}