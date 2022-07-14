package threadPackage.producer.p3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 多生产、多消费
 *
 * while 判断标记，解决了线程获取执行权后，是否要运行，而if只判断一次，会导致不该运行的线程运行了，会导致数据错误的情况
 *
 * notifyAll解决了本方线程一定回唤醒对方多线程，而notify只能唤醒一个线程，如果本方唤醒本方，没有意义，并且while判断标记+notify会导致死锁
 */
class Resource{
    private String name;
    private int count = 1;
    private boolean flag = false;
    //创建锁对象
    Lock lock = new ReentrantLock();
    //通过已有的锁获取该锁上的监视器对象
    Condition pro = lock.newCondition();
    Condition con = lock.newCondition();
    public void set(String name){
        lock.lock();
        try {
            while (flag){
                try {
                    pro.await();
//                    pro.wait();
                }catch (InterruptedException e){

                }
            }
            this.name = name + this.count;
            this.count = this.count+1;
            System.out.println(Thread.currentThread().getName()+"......生产者......"+this.name);
            flag = true;
            con.signal();
//            con.notify();
        }finally {
            lock.unlock();
        }

    }
    public void out(){
        lock.lock();
        try {

            while (!flag) {
                try {
                    con.await();
//                    con.wait();
                } catch (InterruptedException e) {

                }
            }
            System.out.println(Thread.currentThread().getName() + "............消费者............" + this.name);
            flag = false;
            pro.signal();
//            pro.notify();
        }finally {
            lock.unlock();

        }
    }
}

class Producer implements Runnable{
    public Resource r;
    Producer(Resource r){
        this.r = r;
    }
    public void run(){
        while (true){
            r.set("土豆");
        }
    }
}

class Consumer implements Runnable{
    public Resource r;
    Consumer(Resource r){
        this.r = r;
    }
    public void run(){
        while (true){
            r.out();
        }
    }
}

public class ProducerConsumerDemo {
    public static void main(String[] args) {
        Resource r= new Resource();
        Producer p = new Producer(r);
        Consumer c = new Consumer(r);
        Thread t0 = new Thread(p);
        Thread t1 = new Thread(c);
        Thread t2 = new Thread(p);
        Thread t3 = new Thread(c);
        t0.start();
        t1.start();
        t2.start();
        t3.start();
    }
}
