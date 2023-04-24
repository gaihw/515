package lambdaPackage;

/**
 * person实体类
 *
 * @author gaihw
 * @date 2023/1/10 17:37
 */
public class Person implements Comparable<Person>{

    public Person(){}

    public Person(int id, String name, int age, String addr, int gender, double salary,String remark){
        this.id = id;
        this.age = age;
        this.name = name;
        this.addr = addr;
        this.gender = gender;
        this.salary = salary;
        this.remark = remark;
    }
    public Person(int id, String name){
        this.id = id;
        this.name = name;
    }

    public Person(Person person) {
        this.id = person.id;
        this.name = person.name;
        this.age = person.age;
        this.addr = person.addr;
        this.gender = person.gender;
        this.salary = person.salary;
        this.remark = person.remark;
    }

    @Override
    public int compareTo(Person o) {
        if(this.salary > o.getSalary()){
            return 1;
        }else{
            return -1;
        }
    }

    private int id;
    private String name;
    private int age;
    private String addr;
    /**
     * 0-女 1-男
     */
    private int gender;
    private double salary;
    private String remark;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
