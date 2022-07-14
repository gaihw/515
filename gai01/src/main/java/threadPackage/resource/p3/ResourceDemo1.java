package threadPackage.resource.p3;

class Resource{
    private String name;
    private String sex;
    private boolean flag = false;

    public synchronized void set(String name,String sex){
        if (flag){
            try {
                this.wait();
            }catch (InterruptedException e){

            }
        }
        this.name = name;
        this.sex = sex;
        flag = true;
        this.notify();
    }
    public synchronized void out(){
        if (!flag){
            try {
                this.wait();
            }catch (InterruptedException e){

            }
        }
        System.out.println(this.name+"+"+this.sex);
        flag = false;
        this.notify();
    }
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
                    r.set("zhangsan","nan");
                }else {
                    r.set("丽丽","女女女女女");
                }
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
                r.out();
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
