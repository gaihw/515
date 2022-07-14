package threadPackage.executorService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

public class Demo2 {
    public static void main(String[] args) {
        List<String> caseList = new ArrayList<String>();
        for (int i = 0; i < 112; i++) {
            caseList.add(""+i);
        }
        List<List<String>> groupList = partition(caseList, 10);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(groupList.size(), groupList.size()+10, 10L, TimeUnit.SECONDS,new PriorityBlockingQueue<Runnable>());
        for (int j = 0; j < groupList.size(); j++) {
            int finalI = j;
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    List<String> caseExecuteChainGroup = groupList.get(finalI);
                    System.out.println(Thread.currentThread().getName()+"..."+caseExecuteChainGroup);
                }
            });
        }

        threadPoolExecutor.shutdown();
    }

    public static <T> List<List<T>> partition(final List<T> list, final int size) {
        if (list == null) {
            throw new IllegalArgumentException("List must not be null");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be greater than 0");
        }
        List<List<T>> result = new ArrayList<>();
        Iterator<T> it = list.iterator();
        List<T> subList = null;
        while (it.hasNext()) {
            if (subList == null) {
                subList = new ArrayList<>();
            }
            T t = it.next();
            subList.add(t);
            if (subList.size() == size) {
                result.add(subList);
                subList = null;
            }
        }
        //补充最后一页
        if (subList != null) {
            result.add(subList);
        }
        return result;

    }
}
