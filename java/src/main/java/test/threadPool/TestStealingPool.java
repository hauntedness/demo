package test.threadPool;

import java.util.concurrent.*;

public class TestStealingPool {

    public static void main(String[] args) throws InterruptedException {
        test4();

    }

    public static void test3() throws InterruptedException {
        ExecutorService executor = Executors.newWorkStealingPool(8);
        for (int i = 0; i < 5; i++) {
            final int taskIndex = i;
            executor.execute(() -> {
                System.out.println(taskIndex);
            });
        }
        Thread.sleep(6000);
    }

    public static void test2() throws InterruptedException {
        ExecutorService executor = Executors.newWorkStealingPool(8);
        for (int i = 0; i < 5; i++) {
            final int taskIndex = i;
            executor.execute(() -> {
                System.out.println(taskIndex);
            });
        }
        Thread.sleep(6000);
    }

    public static void test1() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                8, //corePoolSize
                8, //maximumPoolSize
                2, //keepAliveTime
                TimeUnit.MILLISECONDS, //unit
                new LinkedBlockingDeque<>(5));//workQueue

        for (int i = 0; i < 5; i++) {
            final int taskIndex = i;
            executor.execute(() -> {
                System.out.println(taskIndex);
            });
        }
    }

    public static void test4() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                2, //corePoolSize
                5, //maximumPoolSize
                100, //keepAliveTime
                TimeUnit.SECONDS, //unit
                new LinkedBlockingDeque<>(100));//workQueue

        for (int i = 0; i < 10; i++) {
            final int taskIndex = i;
            executor.execute(() -> {
                System.out.println(taskIndex);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
