public class ThreadPractice

{
    private final Object lockA = new Object();
    private final Object lockB = new Object();


    public void method1(){
        synchronized(lockA){
            synchronized (lockB){

            }
        }
    }

    public static void main(String[] args) {


    }
}
