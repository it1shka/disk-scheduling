public class DiskQuery {

    private final String name;
    private final int position;
    private final int deadline;

    public DiskQuery(String name, int position, int deadline) {
        this.name = name;
        this.position = position;
        this.deadline = deadline;
    }

    public DiskQuery(String name, int position) {
        this(name, position, -1);
    }

    public boolean hasDeadline() {
        return deadline >= 0;
    }

    public boolean withoutDeadline() {
        return deadline < 0;
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    public int getDeadline() {
        return deadline;
    }

    @Override public String toString() {
        if (hasDeadline()) {
            return String.format("%s (at %d, deadline %d)", name, position, deadline);
        }
        return String.format("%s (at %d)", name, position);
    }
}
