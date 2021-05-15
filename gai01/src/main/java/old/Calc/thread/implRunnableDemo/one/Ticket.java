package old.Calc.thread.implRunnableDemo.one;

public class Ticket implements Runnable{

    private int num = 200;
    @Override
    public void run() {
        while (true){
            synchronized (this){
                if (num > 0){
                    try {
                        Thread.sleep(10);
                    }catch (InterruptedException e){

                    }
                    System.out.println(Thread.currentThread().getName()+"....num:"+num--);

                }
            }
        }
    }
}
