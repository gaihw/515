package old.Calc.thread.implRunnableDemo.two;

public class Consuer implements Runnable{
    private Resource r;
    Consuer(Resource r){
        this.r = r;
    }
    @Override
    public void run() {
        while (true){
            synchronized (r){
                while (r.flag){
                    try {
                        r.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(Thread.currentThread().getName()+"....consuer..............."+r.num);
                r.flag = true;
                r.notify();
            }
        }
    }
}
