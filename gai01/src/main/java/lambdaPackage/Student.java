package lambdaPackage;

/**
 * 【请填写功能名称】
 *
 * @author gaihw
 * @date 2023/1/11 10:39
 */
public class Student {
    public Integer id;
    public String name;
    public Student(){}
    public Student(Integer id,String name){
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
