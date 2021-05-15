package old.Calc.listpackage.three;

import java.util.HashSet;
import java.util.Iterator;

public class HashMapDemo {

    public static void main(String[] args) {

        HashSet<Person> hashMap = new HashSet<Person>();

        hashMap.add(new Person("songjiang",32));
        hashMap.add(new Person("lincong",22));
        hashMap.add(new Person("wuyong",36));
        hashMap.add(new Person("yaogai",44));
        hashMap.add(new Person("yaogail",44));

        Iterator it = hashMap.iterator();

        while (it.hasNext()){
            Person p = (Person)it.next();

            System.out.println(p.getAge()+":"+p.getName());
        }

    }
}
