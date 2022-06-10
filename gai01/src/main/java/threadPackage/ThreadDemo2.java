package threadPackage;

class Demo2 extends Thread{
    private String name;
    Demo2(String name){
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

public class ThreadDemo2 {
    public static void main(String[] args) {
        Demo2 d1 = new Demo2("测试");
        Demo2 d2 = new Demo2("test");
        //开启线程，调用run方法
        d1.start();
        d2.start();
        System.out.println("main run ...");
    }
}
