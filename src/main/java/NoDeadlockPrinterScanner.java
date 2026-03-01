import java.util.concurrent.locks.ReentrantLock;

public class NoDeadlockPrinterScanner {


    private final Object printer = new Object();
    private final Object scanner=  new Object();

    private final ReentrantLock lockA  = new ReentrantLock();





    public void useDevices(){

        Object firslock = printer;
        Object secondlock = scanner;


        lockA.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " locked printer");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {

            }
        }
        finally {
            lockA.unlock();
        }

            synchronized (secondlock){
                System.out.println(Thread.currentThread().getName() + " locked scanner");
                System.out.println(Thread.currentThread().getName() + " using both devices");
            }




    }

    public static void main(String[] args) {

        NoDeadlockPrinterScanner obj = new NoDeadlockPrinterScanner();
        Thread t1 = new Thread(() -> obj.useDevices() , " Thread-1 ");
        Thread t2 = new Thread(() -> obj.useDevices() , " Thread-2 ");

        t1.start();
        t2.start();


    }

}
