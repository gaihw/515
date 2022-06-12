package threadPackage.producer.p1;

class Resource{
    private String name;
    private int count = 1;
    private boolean flag = false;
    public synchronized void set(String name){
        if (flag){
            try {
                this.wait();
            }catch (InterruptedException e){

            }
        }
        this.name = name + this.count;
        this.count = this.count+1;
        System.out.println("......生产者......"+this.name);

        flag = true;
        this.notify();
    }
    public synchronized void out(){
        if (!flag){
            try {
                this.wait();
            }catch (InterruptedException e){

            }
        }
        System.out.println("............消费者............"+this.name);
        flag = false;
        this.notify();
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
        Thread t1 = new Thread(p);
        Thread t2 = new Thread(c);
        t1.start();
        t2.start();
    }
}
