import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class GenericsAssignmentRevised<T extends Runnable> {
    protected boolean stop = false;
    protected boolean stopNow = false;
    protected final BlockingQueue<T> taskQueue;
    protected final Thread consumerThread;

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

    }

    public<V> Future<V> submitTask(final Callable<V> callable) throws InterruptedException {
        return null;
    }


    public List<Runnable> drain(){
        return null;
    }

    private volatile boolean alreadyStop =false;

    public void stop(boolean wait) throws InterruptedException {

    }

    public void stopNow(boolean drain) throws InterruptedException {

    }


    public void waitUntilDone() throws InterruptedException {

    }

}

