package threadPackage.resource.p1;

class Resource{
    String name;
    String sex;
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
                if (x == 0){
                    r.name="zhangsan";
                    r.sex="nan";
                }else {
                    r.name="丽丽";
                    r.sex="女女女女女";
                }
            }
            x = ++x%2;
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
                System.out.println(r.name+"="+r.sex);
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
