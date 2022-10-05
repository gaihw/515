package listFilterPackage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 【请填写功能名称】
 *
 * @author gaihw
 * @date 2022/10/5 15:46
 */
public class Test1 {

    public static void main(String[] args) {
        List<Book> bookList = prepareData();
        Book result1 = bookList.stream()
                .filter(b -> "book4111".equals(b.getName()))
                .findAny()
                .orElse(new Book("1","1111"));
        System.out.println(result1.getName());

        System.out.println("=================");

        // 使用map收集
        String name = bookList.stream()
                .filter(b -> "book4".equals(b.getName()))
                .map(Book::getName)
                .findAny()
                .orElse("");
        System.out.println(name);
        System.out.println("---------");

        List<String> names = bookList.stream()
                .filter(b -> "book4".equals(b.getName()) ||"book3".equals(b.getName()) )
                .map(Book::getName)
                .collect(Collectors.toList());
        names.forEach(System.out::println);

    }

    /**
     * 准备书的列表数据
     * @return
     */
    public static List<Book> prepareData() {
        // 准备书的列表，id是从1到10
        List<Book> bookList = new ArrayList<Book>();
        for (int i = 1; i < 11; i++) {
            bookList.add(new Book(String.valueOf(i), "book"+i));
        }
        return bookList;
    }
}
