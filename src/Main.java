import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();

    public static void main(String[] args) {
        var diskSpace = getInteger("Disk space: ");
        var stringsAmount = getInteger("Query strings amount: ");
        var stringSize = getInteger("Query string size: ");
        var randomize = askForRandomization();

        var queries = new DiskQuery[stringsAmount][stringSize];
        if (randomize) {
            randomizeQueries(queries, diskSpace);
            printQueries(queries);
        } else {
            getQueriesFromInput(queries, diskSpace);
        }

        // here goes the main part
        var schedulers = new Scheduler[] {
                new FirstComeFirstServe(diskSpace),
                new ShortestSeekTimeFirst(diskSpace),
                new SCAN(diskSpace),
                new CircularSCAN(diskSpace),
                new EarliestDeadlineFirst(diskSpace),
                new FeasibleDeadlineSCAN(diskSpace),
        };

        Arrays.stream(schedulers).forEach(scheduler -> {
            System.out.println(Ansi.YELLOW + "Testing " + scheduler + ": " + Ansi.RESET);
            Arrays.stream(queries).forEach(scheduler::executeQueries);
            var total = scheduler.getTotalMovement();
            System.out.println(Ansi.RED + "Total head movement: " + total + Ansi.RESET);
            var average = (double)total / (double)(stringsAmount * stringSize);
            System.out.println(Ansi.RED + "Average head movement per process: " + average + Ansi.RESET);
            System.out.println();
        });
    }

    private static void printQueries(DiskQuery[][] queries) {
        System.out.println();
        var height = queries.length;
        var width = height > 0 ? queries[0].length : 0;
        for (var i = 0; i < height; i++) {
            System.out.println(Ansi.CYAN + "Query string " + (i + 1) + ":" + Ansi.RESET);
            for (var j = 0; j < width; j++) {
                var query = queries[i][j];
                System.out.println(query);
            }
            System.out.println();
        }
    }

    private static void getQueriesFromInput(DiskQuery[][] queries, int diskSpace) {
        var height = queries.length;
        var width = height > 0 ? queries[0].length : 0;

        for (var i = 0; i < height; i++) {
            System.out.println(Ansi.YELLOW + "Entering query string " + (i + 1) + ": " + Ansi.RESET);
            for (var j = 0; j < width; j++) {
                System.out.print("Enter query " + (j + 1) + ": ");
                queries[i][j] = getQueryFromInput(diskSpace, i * width + j);
            }
        }
    }

    private static DiskQuery getQueryFromInput(int diskSpace, int processIndex) {
        while (true) {
            var input = scanner.nextLine().trim().split(" ");
            if (!Arrays.stream(input).allMatch(Main::matchesInteger)) {
                System.out.print("Enter only integers: ");
                continue;
            }
            if (input.length != 1 && input.length != 2) {
                System.out.print("Enter exactly either one or two integers: ");
                continue;
            }
            var position = Integer.parseInt(input[0]);
            if (position >= diskSpace) {
                System.out.print("Position should be less than max disk space: ");
                continue;
            }
            var name = "Query " + (processIndex + 1);
            if (input.length == 2) {
                var deadline = Integer.parseInt(input[1]);
                return new DiskQuery(name, position, deadline);
            }
            return new DiskQuery(name, position);
        }
    }

    private static final int deadlineStep = 1000;
    private static void randomizeQueries(DiskQuery[][] queries, int diskSpace) {
        var height = queries.length;
        var width = height > 0 ? queries[0].length : 0;

        for (var i = 0; i < height; i++) {
            for (var j = 0; j < width; j++) {
                var name = "Query " + (i * width + j + 1);
                var position = random.nextInt(diskSpace);
                var withDeadline = random.nextBoolean();
                DiskQuery query;
                if (withDeadline) {
                    var deadline = random.nextInt(deadlineStep * (i + 1)) + deadlineStep * i;
                    query = new DiskQuery(name, position, deadline);
                } else {
                    query = new DiskQuery(name, position);
                }
                queries[i][j] = query;
            }
        }
    }

    private static int getInteger(String prompt) {
        System.out.print(prompt);
        while (true) {
            var input = scanner.nextLine();
            if (matchesInteger(input)) {
                return Integer.parseInt(input);
            }
            System.out.println("Provide an integer: ");
        }
    }

    private static boolean askForRandomization() {
        boolean randomize;
        while (true) {
            System.out.print("Do you want to randomize processes? [Y/n] ");
            var input = scanner.nextLine().toLowerCase();
            if (input.equals("") || input.equals("y") || input.equals("yes")) {
                randomize = true;
                break;
            }
            if (input.equals("n") || input.equals("no")) {
                randomize = false;
                break;
            }
        }
        return randomize;
    }

    private static boolean matchesInteger(String checking) {
        return checking.matches("^[0-9]+$");
    }

}
