import org.w3c.dom.html.HTMLImageElement;

import java.util.concurrent.*;

/**
 * A type that represents a task to be run in a separate thread, according to a priority parameter.
 */

public class PriorityRunnable implements Runnable, Comparable<PriorityRunnable>{
    /*
    Strategy Pattern:
    1. implement an interface
    2. declare a data member of the same type as the interface
    3. in the constructor/setter method get a variable of the same type that we implement
    4. Dependency injection - in the method override, invoke the data member
     */
    private  Runnable innerRunnable;
    private  int priority;
    private static final int DEFAULT_PRIORITY = 5;

    public PriorityRunnable(Runnable r, int priority){
        this.innerRunnable = r;
        this.priority = priority;
    }

    public PriorityRunnable(Runnable r){
        this(r,DEFAULT_PRIORITY);
    }

    @Override
    public int compareTo(PriorityRunnable o) {
        return Integer.compare(this.priority, o.priority);
    }

    @Override
    public void run() {
        if(innerRunnable!=null){
            innerRunnable.run();
        }
    }

/*    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor =  new ThreadPoolExecutor(2,5,10, TimeUnit.SECONDS, new PriorityBlockingQueue<>());

        PriorityRunnable pr1 = new PriorityRunnable(()-> System.out.println(Runtime.getRuntime().totalMemory()));
        PriorityRunnable pr2 = new PriorityRunnable(()->
                new StringBuilder("ABCDHFGHIJ").reverse().toString(),2);

        Runnable innerRunnable = ()->{
            try{
                Thread.sleep(5000);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        };
        PriorityRunnable pr3 = new PriorityRunnable(innerRunnable,3);

        threadPoolExecutor.execute(pr1);
        threadPoolExecutor.execute(pr2);
        threadPoolExecutor.execute(pr3);


    }*/
}
