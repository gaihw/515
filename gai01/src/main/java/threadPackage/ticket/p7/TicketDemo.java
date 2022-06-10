package threadPackage.ticket.p7;

/**
 * 静态函数的锁，不是this，而是该函数哦所属字节码文件对象，可以用Ticket.class或this.getClass()
 */
class Ticket implements Runnable{
    private static int num = 200;
    public boolean flag = true;
    Ticket(){
        super();
    }
    public void run(){
        System.out.println("this:"+this.getClass());
        System.out.println("Ticket:"+Ticket.class);
        if (flag) {
            while (true) {
                synchronized (Ticket.class) {
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
    public static synchronized void sell(){
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
        System.out.println("t:"+t.getClass());
        Thread t1 = new Thread(t);
        Thread t2 = new Thread(t);

        t1.start();
        Thread.sleep(10);
        t.flag = false;
        t2.start();
    }


}
