package threadPackage.ticket.p5;

/**
 * 验证同步函数的锁，是this
 */

class Ticket implements Runnable{
    Object obj = new Object();
    private int num = 200;
    public boolean flag = true;
    Ticket(){
        super();
    }
    public void run(){
        if (flag) {
            while (true) {
                synchronized (obj) {
                    if (num > 0) {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(Thread.currentThread().getName() + "......obj......" + num--);
                    }
                }
            }
        }else {
            while (true)
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
            System.out.println(Thread.currentThread().getName() + "......function......" + num--);
        }
    }
}

public class TicketDemo {
    public static void main(String[] args) throws InterruptedException {
        Ticket t = new Ticket();
        Thread t1 = new Thread(t);
        Thread t2 = new Thread(t);

        t1.start();
        Thread.sleep(10);
        t.flag = false;
        t2.start();
    }


}
