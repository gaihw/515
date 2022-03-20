package test.nodeTest;

public class SingleNode {
    public Object data;
    public SingleNode next;

    public SingleNode(Object data,SingleNode next){
       this.data = data;
       this.next = next;
    }

    public SingleNode(Object data){
        this(data,null);
    }
}
