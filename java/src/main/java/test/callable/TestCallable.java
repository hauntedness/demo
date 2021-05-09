package test.callable;

import java.util.concurrent.*;

public class TestCallable {


    public static void main(String[] args) throws ExecutionException, InterruptedException {

        Callable c = new Callable() {
            @Override
            public Object call() throws Exception {
                return "task done";
            }
        };

        ExecutorService service = Executors.newCachedThreadPool();
        Future future = service.submit(c);
        String str1 = (String) future.get();
        System.out.println(str1);
        service.shutdown();
    }

}
