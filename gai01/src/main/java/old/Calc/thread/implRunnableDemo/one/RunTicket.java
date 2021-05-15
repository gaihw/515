package old.Calc.thread.implRunnableDemo.one;

public class RunTicket {
    public static void main(String[] args) {
        Ticket ticket = new Ticket();

        Thread t0 = new Thread(ticket);
        Thread t1 = new Thread(ticket);

        t0.start();
        t1.start();

    }
}
