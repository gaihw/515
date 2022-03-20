package test.threadTest;

public class ThreadTest {
    public static void main(String[] args) {
        Demo d = new Demo("aaa");
        Demo d1 = new Demo("啦啦");
        d.start();
        d1.start();
    }
}
