package buildPackage;

import buildPackage.build.PeopleBuilder;

/**
 * 【请填写功能名称】
 *
 * @author gaihw
 * @date 2022/10/5 00:07
 */
public class Test {
    public static void main(String[] args) {
        People peopleBuilder = new PeopleBuilder()
                .id(1)
                .name("张三")
                .build();
        System.out.println(peopleBuilder.getName());
//        Car car = new Car.Builder()
//                .price(1.11)
//                .brand("aaa")
//                .build();
//        System.out.println(car.getBrand());
    }
}
