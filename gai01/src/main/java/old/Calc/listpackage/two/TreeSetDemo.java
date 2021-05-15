package old.Calc.listpackage.two;

import java.util.Iterator;
import java.util.TreeSet;


public class TreeSetDemo {
    public static void main(String[] args) {

        TreeSet<Person> ts = new TreeSet<Person>(new MyCompareByAge());

        ts.add(new Person("songjiang",33));
        ts.add(new Person("wuyong",31));
        ts.add(new Person("xiaoxuanfeng",45));
        ts.add(new Person("wusong",28));
        ts.add(new Person("wusong",28));

        Iterator it = ts.iterator();

        while (it.hasNext()){
            Person p = (Person)it.next() ;
            System.out.println(p.getAge()+":"+p.getName());
        }

    }
}
