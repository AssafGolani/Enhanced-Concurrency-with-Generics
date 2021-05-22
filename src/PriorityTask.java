import java.util.concurrent.*;


/**
 *  This class represents and Entity that can be either executed or submitted to a ThreadPool
 *  This type can wrap both Runnable and Callable tasks
 *  It can have a priority
 *  We will use the Strategy Pattern
 *  There will be 2 constructors:
 *  1. for Runnable tasks
 *  2. for Callable tasks
 */

public class PriorityTask<V> implements RunnableFuture<V>, Comparable<PriorityTask<V>> {
    /*
  Strategy Pattern:
  1. implement an interface
  2. declare a data member of the same type as the interface
  3. in the constructor/setter method get a variable of the same type that we implement
  4. Dependency injection - in the method override, invoke the data member
   */

    private RunnableFuture<V> task;
    private int priority;


    public PriorityTask(Callable<V> computation, int priority){
        this.task = new FutureTask<>(computation);
        this.priority = priority;
    }

    public PriorityTask(Runnable r, V result, int priority){
        this.task = new FutureTask<>(r,result);
        this.priority = priority;
    }


    @Override
    public int compareTo(PriorityTask<V> o) {
        return Integer.compare(this.priority, o.priority);
    }


    @Override
    public void run() {
        if(task!= null){
            task.run();
        }
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return task.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        return task.isCancelled();
    }

    @Override
    public boolean isDone() {
        return task.isDone();
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        return task.get();
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return task.get(timeout, unit);
    }


    public static void main(String[] args) {
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(2,5,10,TimeUnit.SECONDS, new PriorityBlockingQueue<>());
        PriorityTask<Void> p1 = new PriorityTask<>(()-> System.out.println(""), null, 3);
        PriorityTask<String> p2 = new PriorityTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "Hello";
            }
        },5);
    }


}
