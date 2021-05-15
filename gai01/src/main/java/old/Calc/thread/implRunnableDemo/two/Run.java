package old.Calc.thread.implRunnableDemo.two;


public class Run {
    public static void main(String[] args) {
        Resource resource = new Resource();

        Consuer consuer = new Consuer(resource);
        Producer producer = new Producer(resource);

        Thread t0 = new Thread(producer);
        Thread t1 = new Thread(consuer);

        t0.start();
        t1.start();

    }
}
