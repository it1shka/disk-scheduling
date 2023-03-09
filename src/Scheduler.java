public abstract class Scheduler {

    protected final int maxDiskSpace;
    protected int head;
    protected int totalMovement;

    public Scheduler(int maxDiskSpace) {
        this.maxDiskSpace = maxDiskSpace;
        head = 0;
        totalMovement = 0;
    }

    public abstract void executeQueries(DiskQuery[] queries);

    protected void executeQuery(DiskQuery query) {
        var delta = Math.abs(head - query.getPosition());
        totalMovement += delta;
        head = query.getPosition();
        System.out.println("Executed " + Ansi.GREEN + query + Ansi.RESET + Ansi.CYAN + " (+" + delta + ")" + Ansi.RESET);
    }

    protected void headEnd() {
        var last = maxDiskSpace - 1;
        var delta = Math.abs(head - last);
        totalMovement += delta;
        head = last;
        System.out.format("%sHead moved to the end%s %s(+%d)%s\n",Ansi.GREEN,Ansi.RESET,Ansi.CYAN,delta,Ansi.RESET);
    }

    protected void headStart() {
        totalMovement += head;
        var delta = head;
        head = 0;
        System.out.format("%sHead moved to the start%s %s(+%d)%s\n",Ansi.GREEN,Ansi.RESET,Ansi.CYAN,delta,Ansi.RESET);
    }

    public int getTotalMovement() {
        return totalMovement;
    }

}
