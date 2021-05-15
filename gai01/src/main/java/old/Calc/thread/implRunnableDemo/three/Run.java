package old.Calc.thread.implRunnableDemo.three;


public class Run {
    public static void main(String[] args) {
        Resource resource = new Resource();

        Producer producer0 = new Producer(resource);
        Producer producer1 = new Producer(resource);
        Consuer consuer2 = new Consuer(resource);
        Consuer consuer3 = new Consuer(resource);


        Thread t0 = new Thread(producer0);
        Thread t1 = new Thread(producer1);
        Thread t2 = new Thread(consuer2);
        Thread t3 = new Thread(consuer3);

        t0.start();
        t1.start();
        t2.start();
        t3.start();

    }
}
