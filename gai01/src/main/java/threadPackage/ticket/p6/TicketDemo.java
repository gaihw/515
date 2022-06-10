package threadPackage.ticket.p6;

/**
 * 验证同步函数的锁，是this
 *
 * 同步函数和同步代码的区别：
 * 同步函数的锁，是固定的this
 * 同步代码块的锁，是任意的对象
 *
 * 建议使用同步代码块
 */
class Ticket implements Runnable{
    private int num = 200;
    public boolean flag = true;
    Ticket(){
        super();
    }
    public void run(){
        System.out.println("this:"+this);
        if (flag) {
            while (true) {
                synchronized (this) {
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
        System.out.println("t:"+t);
        Thread t1 = new Thread(t);
        Thread t2 = new Thread(t);

        t1.start();
        Thread.sleep(10);
        t.flag = false;
        t2.start();
    }


}
