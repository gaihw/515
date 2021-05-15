package old.Calc.listpackage.three;

public class Person implements Comparable{

    private String name;
    private int age;

    Person(String name, int age){
        this.name = name;
        this.age = age;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age &&
                name.equals(person.name);
    }

    @Override
    public int hashCode() {
        return age;
    }

    @Override
    public int compareTo(Object o) {
        Person p = (Person)o;
        int temp = this.age - p.age;
        return temp == 0 ? this.name.compareTo(p.name) : temp;
    }
}
