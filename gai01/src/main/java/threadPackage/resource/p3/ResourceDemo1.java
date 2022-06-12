package threadPackage.resource.p2;

class Resource{
    String name;
    String sex;
    boolean flag = false;
}

class Input implements Runnable{
    public Resource r;
    Input(Resource r){
        this.r= r;
    }
    public void run(){
        int x = 0;
        while (true){
            synchronized (r){
                if (r.flag)
                    try {
                        r.wait();
                    }catch (InterruptedException e){

                    }
                if (x == 0){
                    r.name="zhangsan";
                    r.sex="nan";
                }else {
                    r.name="丽丽";
                    r.sex="女女女女女";
                }
                r.flag = true;
                r.notify();
                x = (x+1)%2;
            }
        }
    }
}

class Output implements Runnable{
    public Resource r;
    Output(Resource r){
        this.r = r;
    }
    public void run(){
        while (true){
            synchronized(r){
                if (!r.flag){
                    try {
                        r.wait();
                    }catch (InterruptedException e){

                    }
                }
                System.out.println(r.name+"="+r.sex);
                r.flag = false;
                r.notify();
            }
        }
    }
}


public class ResourceDemo1 {
    public static void main(String[] args) {
        Resource r= new Resource();
        Input i = new Input(r);
        Output o = new Output(r);

        Thread t1 = new Thread(i);
        Thread t2 = new Thread(o);

        t1.start();
        t2.start();
    }
}
