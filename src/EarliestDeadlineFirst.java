import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EarliestDeadlineFirst extends Scheduler {

    public EarliestDeadlineFirst(int maxDiskSpace) {
        super(maxDiskSpace);
    }

    @Override public void executeQueries(DiskQuery[] queries) {
        System.out.println(Ansi.YELLOW + "Executing queries with deadlines: " + Ansi.RESET);
        Arrays.stream(queries)
                .filter(DiskQuery::hasDeadline)
                .sorted(Comparator.comparingInt(DiskQuery::getDeadline))
                .forEach(this::executeQuery);
        System.out.println(Ansi.YELLOW + "Switching to C-LOOK for queries without a deadline: " + Ansi.RESET);
        var withoutDeadline = Arrays.stream(queries)
                .filter(DiskQuery::withoutDeadline)
                .toList();
        IntStream.range(0, maxDiskSpace)
                .forEach(position -> withoutDeadline
                        .stream()
                        .filter(query -> query.getPosition() == position)
                        .forEach(this::executeQuery)
                );
        headStart();
    }

    @Override public String toString() {
        return "Earliest Deadline First (EDF) Scheduler";
    }

}
