package threadPackage.bank.p1;

/**
 * 需求：有一个银行，两个储户，每个储户存三次钱，每次存100元
 */

class Bank{
    private int sum;
    public void add(int num){
        sum = sum +num;
        System.out.println("sum="+sum);
    }
}

class Consutomer implements Runnable{
    private Bank b = new Bank();
    public void run(){
        for (int i = 0; i < 3; i++) {
            b.add(100);
        }
    }
}

public class BankDemo {
    public static void main(String[] args) {
        Consutomer c = new Consutomer();
        Thread t1 = new Thread(c);
        Thread t2 = new Thread(c);
        t1.start();
        t2.start();
    }
}
