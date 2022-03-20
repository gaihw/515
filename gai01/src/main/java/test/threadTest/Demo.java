package test.threadTest;

public class Demo extends Thread{
    public String name;
    Demo(String name){
        this.name = name;
    }
    public void run(){
        System.out.println("name="+name+"......"+getName());
    }

}
