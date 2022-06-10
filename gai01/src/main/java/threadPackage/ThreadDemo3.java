package threadPackage;

class Demo3 implements Runnable{
    private String name;
    Demo3(String name){
//        super(name);
        super();
        this.name=name;
    }
    public void run(){
        for (int i = 0; i < 10; i++) {
            System.out.println(name+"......"+i+"......"+Thread.currentThread().getName());
//            System.out.println(name+"......"+i+"......"+getName());
        }
    }
}

public class ThreadDemo3 {
    public static void main(String[] args) {
        Demo3 d1 = new Demo3("测试");
        Thread t1 = new Thread(d1);
        Thread t2 = new Thread(d1);
        //开启线程，调用run方法
        t1.start();
        t2.start();
        System.out.println("main run ...");
    }
}
