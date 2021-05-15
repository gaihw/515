package old.Calc;

public class IfTest {
    public static void main(String[] args) {
        test(0);
    }

    /**
     * 输出 stop  test
     * @param a
     */
    public static void test(int a){
        if (a == 0){
            System.out.println("stop");
        }
        System.out.println("test");
    }


    /**
     * 输出 stop   return返回的作用
     * @param a
     */
    public static void test_1(int a){
        if (a == 0){
            System.out.println("stop");
            return;
        }
        System.out.println("test");
    }
}
