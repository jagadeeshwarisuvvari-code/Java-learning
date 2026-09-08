
class Task1 extends Thread{
    public void run(){
        System.out.println("\nTask 1 started");

            for(int i=101;i<=199;i++)
                System.out.print(i+" ");
            System.out.println("\nTask1 done");
    }
}

class Task2 implements Runnable{
    @Override
    public void run() {
        System.out.println("\nTask 2 started");


        for (int i = 201; i <= 299; i++)
            System.out.print(i + " ");
        System.out.println("\nTask2 done");
    }
}

public class ThreadBasicRunner{

    public static void main(String[] args) throws InterruptedException{
        Task1 t1 = new Task1();
        t1.setPriority(10);

        t1.start();
        Task2 t2 = new Task2();
        Thread th = new Thread(t2);
        th.start();
        t1.join();
        System.out.println("\nTask3 started");
        for (int i = 301; i <= 399; i++)
            System.out.print(i + " ");
        System.out.println("\nTask3 done");
    }
    }

