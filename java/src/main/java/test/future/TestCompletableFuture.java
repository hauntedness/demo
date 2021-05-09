package test.future;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;


public class TestCompletableFuture {


    volatile public static String status;


    public static void main(String[] args) throws IOException {

        MyTask task1 = new MyTask("task1", 3, true);
        MyTask task2 = new MyTask("task2", 5, true);
        MyTask task3 = new MyTask("task3", 1, true);

        CompletableFuture async0 = CompletableFuture.supplyAsync(() -> task1.call()).thenAcceptAsync(ret -> callback(ret, task1));
        CompletableFuture async1 = CompletableFuture.supplyAsync(() -> task2.call()).thenAcceptAsync(ret -> callback(ret, task2));
        CompletableFuture async2 = CompletableFuture.supplyAsync(() -> task3.call()).thenAcceptAsync(ret -> callback(ret, task3));

        CompletableFuture.allOf(async0, async1, async2);
    }

    public static void callback(String ret, MyTask task) {
        if ("FAIL".equals(ret)) {
            status = "FAIL";
            task.cancel();
        } else {
            task.report();
        }
    }
}

class MyTask implements Callable {
    private String name;
    private int timeInSeconds;
    private boolean ret;
    volatile private String myStatus;


    public MyTask(String name, int timeInSeconds, boolean ret) {
        this.name = name;
        this.timeInSeconds = timeInSeconds;
        this.ret = ret;
    }

    @Override
    public String call() {
        // 执行过程中要listen 其它任务的状态
        try {
            Thread.sleep(timeInSeconds);
            if (myStatus.equals("FAIL")) {
                return "FAIL";
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        double random = Math.random() * 10;
        if (random < 5) {
            System.out.println(this.name + ": FAIL");
            return "FAIL";
        } else {
            System.out.println(this.name + ": SUCCESS");
            return "SUCCESS";
        }

    }

    public void report() {
        System.out.println(this.name + " completed!");
    }

    public void cancel() {
        System.out.println(this.name + " canceled!");
    }
}