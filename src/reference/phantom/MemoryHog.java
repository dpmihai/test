package reference.phantom;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Aug 30, 2006
 * Time: 1:31:45 PM
 */
import java.util.*;

public class MemoryHog extends TimerTask {

    private static final int DEFAULT_CHUNK_SIZE = (1024 * 1024);

    private final int chunkSize;
    private final Collection<byte[]> chunks;

    public MemoryHog() {
        this(DEFAULT_CHUNK_SIZE);
    }

    public MemoryHog(int chunkSize) {
        super();
        this.chunkSize = chunkSize;
        this.chunks = new HashSet<byte[]>();
    }

    @Override
    public void run() {
        this.chunks.add(new byte[this.chunkSize]);
    }
}