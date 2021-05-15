package old.Calc.thread.implRunnableDemo.three;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Resource {
    int num = 0;
    boolean flag = true;

    Lock lock = new ReentrantLock();

    Condition consumer_con = lock.newCondition();
    Condition producer_con = lock.newCondition();

    public void pro(){
        lock.lock();
        try {
            while (!this.flag) {
                try {
                    producer_con.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.num = this.num + 1;
            System.out.println(Thread.currentThread().getName() + "....producer..." + this.num);
            this.flag = false;
            consumer_con.signal();
        }finally {
            lock.unlock();
        }
    }

    public void con(){
        lock.lock();
        try {
            while (this.flag){
                try {
                    consumer_con.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName()+"....consuer..............."+this.num);
            this.flag = true;
            producer_con.signal();
        }finally {
            lock.unlock();
        }

    }
}
