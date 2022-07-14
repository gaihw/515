package threadPackage.executorService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Demo1 {
    public static void main(String[] args) {
        List<String> caseList = new ArrayList<String>();
        for (int i = 0; i < 112; i++) {
            caseList.add(""+i);
        }
        List<List<String>> groupList = partition(caseList, 10);
        CountDownLatch countDownLatch = new CountDownLatch(groupList.size());
        ExecutorService executorService = Executors.newFixedThreadPool(groupList.size());
        // 根据分组长度循环处理
        for (int j = 0; j < groupList.size(); j++) {
            int finalI = j;
            executorService.execute(() -> {
                List<String> caseExecuteChainGroup = groupList.get(finalI);
                System.out.println(Thread.currentThread().getName()+"..."+caseExecuteChainGroup);
                countDownLatch.countDown();
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();   //关闭线程池
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
