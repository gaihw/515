package test.stringTest;

/**
 * 【请填写功能名称】
 *
 * @author gaihw
 * @date 2022/9/19 11:34
 */
public class Dome {

    static class Perse extends Object {
    }

    static class Student extends Perse {
    }

    //这个方法判断是否是Perse的实例，用instanceof判断。
    void f(Object o) {//Object可以接收任何的类型。
        if (o instanceof Perse) {
            System.out.println("你输入的对象是Perse的实例");
            System.out.println(o);
        }
        else
            System.out.println("你输入的对象不是Perse的实例");
    }
    public static void main (String[]args){
        Dome dome = new Dome();
        Perse perse = new Perse();
        Student student = new Student();
        Object object = new Object();
        dome.f(perse);
        //结果：
        //输入的是student和perse那么instanceof 判断就会是true，if也就执行打印语句System.out.println("你输入的对象是Perse的实例");
        //那么输入object自然instanceof判断就是false。
//            原因我们开头已经说的很清楚啦。
        //instanceof关键字的作用是判断左边对象是否是右边类的实例(通俗易懂的说就是：子类，或者右边类本身的对象)
    }
}
