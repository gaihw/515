package test.t2023072903;

/**
 * 【请填写功能名称】
 *
 * @author gaihw
 * @date 2023/7/29 16:31
 */
public class Child extends Person{
    private String name = "child";
    public Child(){
        System.out.println("this is a child");
//        super();
    }

    public static void main(String[] args) {
        Child child = new Child();
        System.out.println(child.name);
    }
}
