package old.Calc.thread.implRunnableDemo.two;

public class Producer implements Runnable {

    private Resource r;
    Producer(Resource r){
        this.r = r;
    }
    @Override
    public void run() {
        while (true){
            synchronized (r){
                while (!r.flag){
                    try {
                        r.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                r.num = r.num+1;
                System.out.println(Thread.currentThread().getName()+"....producer..."+r.num);
                r.flag = false;
                r.notify();
            }
        }
    }
}
