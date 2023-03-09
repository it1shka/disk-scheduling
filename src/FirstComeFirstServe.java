import java.util.Arrays;

public class FirstComeFirstServe extends Scheduler {

    public FirstComeFirstServe(int maxDiskSpace) {
        super(maxDiskSpace);
    }

    @Override public void executeQueries(DiskQuery[] queries) {
        Arrays.stream(queries).forEach(this::executeQuery);
    }

    @Override public String toString() {
        return "First Come First Serve Scheduler (FCFS)";
    }

}
