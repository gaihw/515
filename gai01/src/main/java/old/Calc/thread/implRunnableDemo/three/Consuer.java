package old.Calc.thread.implRunnableDemo.three;

public class Consuer implements Runnable{
    private Resource r;
    Consuer(Resource r){
        this.r = r;
    }
    @Override
    public void run() {
        while (true){
            r.con();
        }
    }
}
