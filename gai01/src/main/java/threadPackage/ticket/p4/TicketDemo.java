package threadPackage.ticket.p4;

class Ticket implements Runnable{
    Object obj = new Object();
    private int num = 100;
    Ticket(){
        super();
    }
    public void run(){
        while (true) {
            sell();
        }
    }
    public synchronized void sell(){
        if (num > 0) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "......sale......" + num--);
        }
    }
}

public class TicketDemo {
    public static void main(String[] args) {
        Ticket t = new Ticket();
        Thread t1 = new Thread(t);
        Thread t2 = new Thread(t);
        Thread t3 = new Thread(t);

        t1.start();
        t2.start();
        t3.start();
    }


}
