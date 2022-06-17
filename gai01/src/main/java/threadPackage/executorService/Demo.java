package threadPackage.executorService;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Demo {
    private static final String model = Demo.class.getName();

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 112; i++) {
            list.add(i + ",");
        }
        StringBuffer coupons = getCoupons(list, 5);
        System.out.println(coupons);
    }

    public static StringBuffer getCoupons(List<String> list , final int threadNum){
        int size = list.size();
        if (size == 0 || list == null){
            return null;
        }
        StringBuffer bf = new StringBuffer();
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
        List<Future<String>> futures = new ArrayList<Future<String>>(size);
        int toIndexTmp = 0;
        for (int i = 0; i <= threadNum; i++) {
            toIndexTmp = size / threadNum * (i + 1);
            if (size / threadNum * (i + 1)>size){
                toIndexTmp = size;
            }
            //将数据分成threadNum份，线程同时执行
            final List<String> subList = list.subList(size / threadNum * i, toIndexTmp);
            Callable<String> task = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(Thread.currentThread().getName()+">>>");
                    //对每个线程(线程中的每份数据)的逻辑操作
                    for (String subString : subList) {
//                        System.out.println(subString);
                        buffer.append(subString);
                    }
                    buffer.append("\n");
                    return buffer.toString();
                }
            };
            //添加线程到队列
            futures.add(executorService.submit(task));
        }

        for (int i = 0; i < futures.size(); i++) {
            try {
                bf.append(futures.get(i).get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        //结束线程执行
        executorService.shutdown();
        return bf;
    }
}
