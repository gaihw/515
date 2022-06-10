package threadPackage.ticket.p2;

/**
 * 线程安全问题产生的原因：
 * 1、多个线程在操作共享的数据
 * 2、操作共享数据的线程代码有多条
 *
 * 当一个线程在执行操作共享数据的多条代码过程中，其他线程参与了运算，就会导致线程安全问题的产生
 *
 * 解决思路：
 * 就是将多条操作共享数据的线程代码封装起来，当有线程在执行这些代码的时候，其他线程是不可以参与运算的
 * 必须要当前线程把这些代码都执行完毕后，其他线程才可以参与运算
 *
 * 在Java中，用同步代码块就可以解决这个问题
 *
 * 同步代码块的格式：
 * synchronized(对象){
        需要被同步的代码
  }
 */
class Ticket implements Runnable{
    private int num = 100;
    Ticket(){
        super();
    }
    public void run(){
        while (true){
            if (num>0){
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+"......sale......"+num--);
            }
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
