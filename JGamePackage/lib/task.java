package JGamePackage.lib;

/**A library of time manipulation functions
 * 
 */
public class task {
    
    /**Yields the current thread until {{@code seconds}} seconds have passed
     * 
     * @param seconds : The time, in seconds, that the current thread should yield for
     */
    public static boolean wait(double seconds){
        long t = System.currentTimeMillis()/1000;
        while ((System.currentTimeMillis()/1000)-t <seconds) {}
        return true;
    }


    /**Yields the current thread until {{@code seconds}} seconds have passed
     * 
     * @param seconds : The time, in seconds, that the current thread should yield for
     */
    public static boolean wait(int seconds){
        return wait((double) seconds);
    }

    public static boolean waitMinimum(){
        return task.wait(0.00000000000000001);
    }


    /**Runs the given {{@code task}} in a new, separate thread
     * 
     * @param task : The task to be executed in a new thread
     * 
     * @return The newly created thread
     */
    public static Thread spawn(Runnable t){
        Thread newThread;
        newThread = new Thread(()->{
            t.run(); 
        });
        newThread.start();
        return newThread;
    }

    @SuppressWarnings("removal")
    public static void cancel(Thread t){
        t.stop();
    }

    
    /**Runs the given {{@code task}} after waiting for {{@code seconds}} seconds in a separate thread
     * 
     * @param seconds
     * @param task
     */
    public static void delay(double seconds, Runnable t){
        task.spawn(()->{
            wait(seconds);
            t.run();
        });
    }

}