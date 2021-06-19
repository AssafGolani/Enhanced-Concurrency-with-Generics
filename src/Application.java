import java.util.concurrent.*;

public class Application {
    public static void main(String[] args) throws InterruptedException, ExecutionException {

        GenericsAssignmentRevised<PriorityRunnable> service =
                new GenericsAssignmentRevised<>(new PriorityBlockingQueue<>());
        /*
         submit Runnable tasks to to the queue (as PriorityRunnable objects) using
         the apply methods above
         */
        service.submitTask(() -> System.out.println(
                "There are more than 2 design patterns in this class"));

        service.submitTask(() -> System.out.println("a runnable"));

        service.submitTask(new Runnable()  {
            @Override
            public void run() {
                System.out.println("Fun");
            }
        });

        Callable<String> stringCallable= () -> {
            try {
                Thread.sleep(5000);// wait until interrupt
            } catch (InterruptedException e) {
                System.out.println("interrupted");
            }
            return "callable string";
        };
        Future<String> futureString = service.submitTask(stringCallable);
        Future<String> anotherFutureString = service.submitTask(stringCallable);

        System.out.println(futureString.get());

        System.out.println(anotherFutureString.get());

        service.stop(false);
        System.out.println("done");
    }
}
