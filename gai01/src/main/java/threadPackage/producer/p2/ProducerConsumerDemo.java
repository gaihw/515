package threadPackage.producer.p2;

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
    public synchronized void set(String name){
        while (flag){
            try {
                this.wait();
            }catch (InterruptedException e){

            }
        }
        this.name = name + this.count;
        this.count = this.count+1;
        System.out.println(Thread.currentThread().getName()+"......生产者......"+this.name);

        flag = true;
        this.notifyAll();
    }
    public synchronized void out(){
        while (!flag){
            try {
                this.wait();
            }catch (InterruptedException e){

            }
        }
        System.out.println(Thread.currentThread().getName()+"............消费者............"+this.name);
        flag = false;
        this.notifyAll();
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
