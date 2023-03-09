import java.util.Arrays;
import java.util.LinkedList;

public class ShortestSeekTimeFirst extends Scheduler {

    public ShortestSeekTimeFirst(int maxDiskSpace) {
        super(maxDiskSpace);
    }

    @Override public void executeQueries(DiskQuery[] queries) {
        var toExecute = new LinkedList<>(Arrays.asList(queries));
        while (!toExecute.isEmpty()) {
            var closestQuery = toExecute.stream().min((o1, o2) -> {
                var delta1 = Math.abs(o1.getPosition() - head);
                var delta2 = Math.abs(o2.getPosition() - head);
                return delta1 - delta2;
            }).get();
            executeQuery(closestQuery);
            toExecute.remove(closestQuery);
        }
    }

    @Override public String toString() {
        return "Shortest Seek Time First Scheduler (SSTF)";
    }

}
