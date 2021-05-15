package old.Calc.listpackage.three;

import old.Calc.listpackage.two.Person;

import java.util.Comparator;

public class MyCompareByAge implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        Person p1 = (Person)o1;
        Person p2 = (Person)o2;
        int temp = p1.getAge() - p2.getAge();
        return temp == 0 ? p1.getName().compareTo(p2.getName()) : temp;
    }
}
