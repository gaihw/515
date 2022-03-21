package test.threadTest.thread01;

public class Demo extends Thread{
    public String name;
    Demo(String name){
        this.name = name;
    }
    public void run(){
        for (int i = 0; i < 10; i++) {
            System.out.println("name="+name+"......"+getName());

        }
    }

}
