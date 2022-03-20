package test.nodeTest;

public class SingleNodeTest {

    public static void main(String[] args) {
        MySingleNode mySingleNode = new MySingleNode();
        mySingleNode.addFirst("aaa");
        mySingleNode.addFirst("bbb");
        mySingleNode.addFirst("ddd");
        System.out.println(mySingleNode);
    }
}
