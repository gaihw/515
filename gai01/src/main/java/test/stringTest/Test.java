package test.stringTest;

/**
 * 【请填写功能名称】
 *
 * @author gaihw
 * @date 2022/9/19 11:20
 */
public class Test {
    public static void main(String[] args) {
        AStringTest aStringTest = new AStringTest();


        System.out.println(toStr(aStringTest,"123"));
    }

    public static String toStr(Object value, String defaultValue)
    {
        if (null == value)
        {
            return defaultValue;
        }
        if (value instanceof String)
        {
//            return (String) value;
            return "是String的实例";
        }
        return value.toString();
    }
}
