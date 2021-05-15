package old.Calc.thread.implRunnableDemo.three;

public class Producer implements Runnable {

    private Resource r;
    Producer(Resource r){
        this.r = r;
    }
    @Override
    public void run() {
        while (true){
            r.pro();
        }
    }
}
