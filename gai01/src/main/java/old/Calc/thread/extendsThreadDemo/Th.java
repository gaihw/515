package old.Calc.thread.extendsThreadDemo;

public class Th extends Thread {

    private String name;
    Th(String name){
        this.name = name;
    }

    public void run(){
        for (int i = 0; i < 50; i++) {
            System.out.println("name="+name+"....threadName="+Thread.currentThread().getName());
        }
    }
}
