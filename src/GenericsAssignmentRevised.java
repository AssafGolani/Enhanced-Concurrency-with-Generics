import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;

/**
 * Submit either Callable or Runnable but the blockingQueue maintains only Runnable
 * @param <T>
 */

public class GenericsAssignmentRevised<T extends Runnable> {
    protected boolean stop = false;
    protected boolean stopNow = false;
    protected final BlockingQueue<T> taskQueue; // Blocking Queue type of Runnable
    protected final Thread consumerThread;
    protected final List<Runnable> runnableList = new ArrayList<Runnable>();


    private final ReentrantReadWriteLock readWriteLock=new ReentrantReadWriteLock();

    public GenericsAssignmentRevised(BlockingQueue<T> paramBlockingQueue) {
        throwIfNull(paramBlockingQueue);
        this.taskQueue = paramBlockingQueue;

        this.consumerThread = new Thread(
                () -> {
                    while ((!stop || !this.taskQueue.isEmpty()) &&
                            (!stopNow)) {
                        try {
                            taskQueue.take().run();
                        } catch (InterruptedException e) {
                            //e.printStackTrace();
                        }
                    }
                });

        this.consumerThread.start();
    }

    /**
     * @param objects pass unknown number of arguments passed in run-time
     * @throws NullPointerException
     */
    public static void throwIfNull(Object... objects) throws NullPointerException {
        for (Object argument: objects){
            if(argument == null){
                throw new NullPointerException("one of the arguments is null");
            }
        }
    }

    public void submitTask(final Runnable runnable) throws InterruptedException{
        if(runnable==null) throw new InterruptedException();
        readWriteLock.writeLock().lock();
        taskQueue.offer((T) new PriorityRunnable(runnable)); // adding the runnable task to the BlockingQueue
        readWriteLock.writeLock().unlock();
    }

    public<V> Future<V> submitTask(final Callable<V> callable) throws InterruptedException {
        if (callable == null) throw new InterruptedException();
        readWriteLock.writeLock().lock();
        /**
         * Priority Task implements the interface RunnableFuture (which is Runnable)
         * It can get either Runnable or Callable but return a FutureTask
         * This is how we can run (or transform) a Callable task as a Runnable task and overcome our challenge.
         */
        RunnableFuture<T> fCallable = new PriorityTask(callable,1); // fCallable => futureCallable
        taskQueue.offer((T)new PriorityRunnable(fCallable));// adding the task to the BlockingQueue
        readWriteLock.writeLock().unlock();
        return (Future<V>) fCallable; // we return fCallable as Future by using casting.
    }


    public List<Runnable> drain(){
        this.taskQueue.drainTo(runnableList);
        return runnableList;
    }

    private volatile boolean alreadyStop =false;

    public void stop(boolean wait) throws InterruptedException {
        //writeLock
            if(wait){
                waitUntilDone();
            }
        readWriteLock.writeLock().lock();
        this.stop = true;
        alreadyStop = true;
        readWriteLock.writeLock().unlock();
    }

    public void stopNow(boolean drain) throws InterruptedException {
        //use interrupt() to stop the thread
        //drain = empties the priorityQueue
        consumerThread.interrupt();
        if(drain){
            drain();
        }
    }

    public void waitUntilDone() throws InterruptedException {
        if(consumerThread.isAlive()){
                consumerThread.join();
        }
            //lock
    }

}

