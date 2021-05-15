package old.Calc.listpackage.one;

import java.util.Iterator;
import java.util.TreeSet;


public class TreeSetDemo {
    public static void main(String[] args) {

        TreeSet<Person> ts = new TreeSet<Person>();

        ts.add(new Person("songjiang",33));
        ts.add(new Person("wuyong",31));
        ts.add(new Person("xiaoxuanfeng",45));
        ts.add(new Person("wusong",28));
        ts.add(new Person("wusong",28));

        Iterator it = ts.iterator();

        while (it.hasNext()){
            System.out.println(it.next());
        }

    }
}
