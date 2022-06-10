package threadPackage.dealLock;

class Test implements Runnable{
    private boolean flag;
    public Test(boolean flag){
        this.flag = flag;
    }
    public void run(){
        if (flag){
            while (true){
                synchronized (MyLock.locka){
                    System.out.println(Thread.currentThread().getName()+"...if...locka");
                    synchronized(MyLock.lockb){
                        System.out.println(Thread.currentThread().getName()+"...if...lockb");
                    }
                }
            }
        }else {
            while (true){
                synchronized(MyLock.lockb){
                    System.out.println(Thread.currentThread().getName()+"...else...lockb");
                    synchronized(MyLock.locka){
                        System.out.println(Thread.currentThread().getName()+"...else...locka");
                    }
                }
            }
        }
    }

}
class MyLock{
    public static final MyLock locka = new MyLock();
    public static final MyLock lockb = new MyLock();
}

public class DealLockDemo {
    public static void main(String[] args) {

    Test t1 = new Test(true);
    Test t2 = new Test(false);
    Thread tt1 = new Thread(t1);
    Thread tt2 = new Thread(t2);
    tt1.start();
    tt2.start();
    }

}
