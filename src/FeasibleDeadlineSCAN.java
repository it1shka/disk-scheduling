import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;

public class FeasibleDeadlineSCAN extends Scheduler {

    public FeasibleDeadlineSCAN(int maxDiskSpace) {
        super (maxDiskSpace);
    }

    @Override public void executeQueries(DiskQuery[] queries) {
        var active = new LinkedList<>(Arrays.stream(queries).toList());

        System.out.println(Ansi.YELLOW + "Feasible Deadline Scan: " + Ansi.RESET);

        // fulfilling feasible deadlines
        while (true) {
            var closestFeasible = active.stream().filter(query -> {
                var timeNeeded = getTotalMovement() + Math.abs(query.getPosition() - head);
                return timeNeeded < query.getDeadline();
            }).min(Comparator.comparing(DiskQuery::getDeadline));
            if (closestFeasible.isEmpty()) {
                break;
            }
            var target = closestFeasible.get().getPosition();
            var pos = head;
            while(true) {
                final var position = pos;
                active.stream()
                        .filter(query -> query.getPosition() == position)
                        .forEach(this::executeQuery);
                active.removeIf(query -> query.getPosition() == position);
                if (pos == target) break;
                if (pos < target) pos++;
                else pos--;
            }
        }

        if (active.isEmpty()) return;

        System.out.println(Ansi.YELLOW + "Switching to SSTF for queries without a feasible deadline: " + Ansi.RESET);

        // then, fulfilling not feasible with SSTF
        while (!active.isEmpty()) {
            var closestQuery = active.stream().min((o1, o2) -> {
                var delta1 = Math.abs(o1.getPosition() - head);
                var delta2 = Math.abs(o2.getPosition() - head);
                return delta1 - delta2;
            }).get();
            executeQuery(closestQuery);
            active.remove(closestQuery);
        }
    }

    @Override public String toString() {
        return "Feasible Deadline SCAN (FD-SCAN) Scheduler";
    }

}
