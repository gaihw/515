package threadPackage;

class Demo1{
    private String name;
    Demo1(String name){
        this.name=name;
    }
    public void show(){
        for (int i = 0; i < 10; i++) {
            System.out.println(name+"......."+i);
        }
    }
}

public class ThreadDemo1 {
    public static void main(String[] args) {
        Demo1 d1 = new Demo1("测试");
        Demo1 d2 = new Demo1("test");
        d1.show();
        d2.show();

    }
}
