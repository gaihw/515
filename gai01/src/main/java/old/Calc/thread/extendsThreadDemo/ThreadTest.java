package old.Calc.thread.extendsThreadDemo;

public class ThreadTest {


    public static void main(String[] args) {
        Th t = new Th("线程");
        Th t1 = new Th("aaa");

        t.start();
        t1.start();
        for (int i = 0; i < 50; i++) {
            System.out.println("main="+Thread.currentThread().getName());
        }
    }


}
