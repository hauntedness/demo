package test.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class TestCF3 {

    public static void main(String[] args) {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "future: Hello");
        CompletableFuture<String> future1 = future.thenApplyAsync((e) -> {
                    System.out.println("future1: "+ e);
                    return e;
                }
        );
        System.out.println("future: created");
        if (future.isDone()) {
            future.complete("future: impossible , as method complete or get is not called");
        } else {
            System.out.println("future: is not done");
        }
        try {
            System.out.println(future.get());
            boolean future_is_done = future.complete("future: is done");
            if (future_is_done) {
                System.out.println("future: complete success");
            } else {
                System.out.println("future: already done");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}
