package test.threadTest.thread;

public class ThreadTest {
    public static void main(String[] args) {
        test.threadTest.thread.Demo d = new test.threadTest.thread.Demo("aaa");
        test.threadTest.thread.Demo d1 = new test.threadTest.thread.Demo("啦啦");
        d.start();
        d1.start();
    }
}
