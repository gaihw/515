package test;

import java.util.LinkedList;

public class MySingleNode {
    //头节点
    public SingleNode head;
    //链表元素个数
    public int size;

    public MySingleNode(){
        this.head = null;
        this.size = 0;
    }

    //链表头部添加元素
    public void addFirst(Object data){
        SingleNode s = new SingleNode(data);
        s.next = this.head;
        this.head = s;
        this.size++;
    }

    //向链表尾部插入元素
    public void addList(Object data){
        this.add(data,this.size);
    }

    //添加
    private void add(Object data, int index) {

        if (index < 0 || index > this.size){
            throw new IllegalArgumentException("Index is error");

        }
        if (index == 0){
            this.addFirst(data);
            return;
        }
        SingleNode preNode = this.head;
        //找到要插入节点前一个节点
        for (int i = 0; i < index-1; i++) {
            preNode = preNode.next;
        }

        SingleNode s = new SingleNode(data);

        //要插入的节点下一个节点指向preNode节点的下一个节点
        s.next = preNode.next;
        //preNode的下一个节点指向要插入节点s
        preNode.next = s;

        this.size++;
    }

    //删除
    public void remove(Object data){
        if (head == null){
            return;
        }
        //要删除的元素与头节点的元素相同
        while (head != null && head.data.equals(data)){
            head = head.next;
            this.size--;
        }
        /**
         * 上面已经对头节点判别是否要进行删除
         * 所以要对头节点的下一个节点进行判别
         */
        SingleNode cur = this.head;
        while (cur != null && cur.next != null){
            if (cur.next.data.equals(data)){
                this.size--;
                cur.next = cur.next.next;

            }else {
                cur = cur.next;
            }

        }
    }
}
