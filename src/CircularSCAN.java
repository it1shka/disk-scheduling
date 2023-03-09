import java.util.Arrays;
import java.util.stream.IntStream;

public class CircularSCAN extends Scheduler {

    public CircularSCAN(int maxDiskSpace) {
        super(maxDiskSpace);
    }

    @Override public void executeQueries(DiskQuery[] queries) {
        IntStream.range(0, maxDiskSpace).forEach(position -> Arrays
            .stream(queries)
            .filter(query -> query.getPosition() == position)
            .forEach(this::executeQuery)
        );
        headEnd();
        headStart();
    }

    @Override public String toString() {
        return "Circular SCAN Scheduler";
    }

}
