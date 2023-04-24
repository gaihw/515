package lambdaPackage;


import com.sun.xml.internal.ws.api.client.WSPortInfo;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 【请填写功能名称】
 *
 * @author gaihw
 * @date 2023/1/10 17:38
 */
public class Test8 {

    private static List<Person> personList = new LinkedList<>();

    public static void main(String[] args) {

        init();

        //List -> Map key是list中对象某一个段，value是整个对象
        //(key1,key2)->key1 是为了避免重复key(重复key会抛出异常)，去前一个值；如果取最后一个值，则为 (key1 ,key2)-> key2
        Map<Integer,Person> map = personList.stream().collect(Collectors.toMap(Person::getId, person -> person,(key1, key2)-> key1));
        map.values().stream().forEach(person -> System.out.println(person.getAge()));

        //List -> Map key是list中对象某一个段，value是某个字段
//        Map<Integer,String> nameMap = personList.stream().collect(Collectors.toMap(Person::getId, person -> person.getName(),(key1, key2)->key1));

        System.out.println("------1-----");

        Map<Integer,Person> map1 = personList.stream().collect(Collectors.toMap(Person::getId, Function.identity()));
        // 如果id有重复值，会报 Duplicate key lambdaPackage.Person@7a07c5b4
        map1.values().stream().forEach(person -> System.out.println(person.getAge()));

        System.out.println("------2-----");
        // 把Person集合按照age分组到map中
        Map<Integer,List<Person>> map2 = personList.stream().collect(Collectors.groupingBy(Person::getAge));
//        map2.values().stream().forEach(people -> show(people));
        map2.values().stream().forEach(people -> {people.stream().forEach(person1 -> System.out.print(person1.getAge()+","));
            System.out.println();});

        System.out.println("------3-----");

        List<Person> list1 = new ArrayList<>();
        List<Person> list2= new ArrayList<>();

        for (int i = 1; i < 4; i++) {
            list1.add(new Person(i,"学生"+i));
        }
        for (int i = 2; i < 5; i++) {
            list2.add(new Person(i,"学生"+i));
        }
        Map<Integer, Person> map3 = list2.stream().collect(Collectors.toMap(Person::getId,Function.identity(),(key1,key2)->key1));
        //把List1和List2中id重复的Student对象的name取出来：
        List<String> strings = list1.stream().map(Person::getId).filter(map3::containsKey).map(map3::get).map(Person::getName).collect(Collectors.toList());
        System.out.println(strings);

    }

    // 按照age分组的列表，打印出来每组的元素
    public static void show(List<Person> people){
        people.stream().forEach(person1 -> System.out.print(person1.getAge()+","));
        System.out.println();
    }

    // 初始化列表
    private static void init(){
        Person person = new Person();
        person.setId(1);
        person.setName("Lisa");
        person.setGender(0);
        person.setAge(24);
        person.setAddr("中国深圳");
        person.setSalary(15000d);
        personList.add(person);
        person = new Person();
        person.setId(2);
        person.setName("Tom");
        person.setGender(1);
        person.setAge(24);
        person.setAddr("中国深圳");
        person.setSalary(18000d);
        personList.add(person);
        person = new Person();
        person.setId(3);
        person.setName("Benny");
        person.setGender(1);
        person.setAge(22);
        person.setAddr("中国北京");
        person.setSalary(22000d);
        personList.add(person);
        person = new Person();
        person.setId(4);
        person.setName("Jenny");
        person.setGender(0);
        person.setAge(29);
        person.setAddr("中国上海");
        person.setSalary(33000d);
        personList.add(person);
        person = new Person();
        person.setId(5);
        person.setName("David");
        person.setGender(1);
        person.setAge(37);
        person.setAddr("中国北京");
        person.setSalary(22000d);
        personList.add(person);
    }

}
