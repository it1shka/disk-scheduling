import java.util.Arrays;
import java.util.stream.IntStream;

public class SCAN extends Scheduler {

    private boolean directionFlag; // true means RIGHT, false means LEFT

    public SCAN(int maxDiskSpace) {
        super(maxDiskSpace);
        directionFlag = true;
    }

    @Override public void executeQueries(DiskQuery[] queries) {
        var positions = IntStream.range(0, maxDiskSpace);
        if (!directionFlag) {
            positions = positions.map(i -> maxDiskSpace - i - 1);
        }

        positions.forEach(position -> Arrays
                .stream(queries)
                .filter(query -> query.getPosition() == position)
                .forEach(this::executeQuery));

        if (directionFlag) headEnd();
        else headStart();
        directionFlag = !directionFlag;
    }

    @Override public String toString() {
        return "SCAN (Elevator) Scheduler";
    }

}
