package com.zmj.demo;


import com.zmj.demo.enums.MessageEnum;

import java.util.concurrent.*;

public class Test {
    boolean finalFlg = false;
    CyclicBarrier cyclicBarrier = new CyclicBarrier(10);

    public static void main(String[] args) {
        new Test().runThread();
    }

    /**
     * CyclicBarrier 适用再多线程相互等待，直到到达一个屏障点。并且CyclicBarrier是可重用的。
     */

    private void runThread() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 20; i++) {
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            executorService.submit(createThread(i));


        }
        executorService.shutdown();
    }

    private Thread createThread(int i) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    cyclicBarrier.await();
                    System.out.println("Thread:" + Thread.currentThread().getName() + "准备完毕,time:" + System.currentTimeMillis());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setName("name" + i);
        return thread;
    }

}
