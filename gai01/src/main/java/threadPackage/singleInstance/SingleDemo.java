package threadPackage.singleInstance;

/**
 * 单例设计----饿汉式,线程安全
 */
class Single{
    private static final Single s = new Single();
    private Single(){}
    public static Single getInstance(){
        return s;
    }
}

/**
 * 单例设计----懒汉式，线程不安全，需要加同步
 */
class SingleOne{
    private static SingleOne s = null;
    private SingleOne(){};
    public static SingleOne getInstance(){
        if (s == null){//提高效率
            synchronized (SingleOne.class){//保证线程安全
                if (s == null){
                    s = new SingleOne();
                }
            }
        }
        return s;
    }
}

public class SingleDemo {
}
