package threadPackage.ticket.p1;

class Ticket extends Thread{
    private int num = 100;
    Ticket(){
        super();
    }
    public void run(){
        while (true){
            if (num>0){
                System.out.println(Thread.currentThread().getName()+"......sale......"+num--);
            }
        }
    }
}

public class TicketDemo {
    public static void main(String[] args) {
        Ticket t1 = new Ticket();
        Ticket t2 = new Ticket();
        Ticket t3 = new Ticket();

        t1.start();
        t2.start();
        t3.start();
    }


}
