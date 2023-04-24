package lambdaPackage;

/**
 * 【请填写功能名称】
 *
 * @author gaihw
 * @date 2023/1/9 17:38
 */
public class Test1 {

    public static void main(String[] args) {
        MathOperation mathOperation = (int a,int b)->{
            System.out.println(a + b);
        };
        mathOperation.operation(1,2);
    }

    interface MathOperation{
        void operation(int a, int b);
    }
}
