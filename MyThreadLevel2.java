import com.sun.tools.javac.Main;

import java.io.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class MyThreadLevel2 extends Thread {
    private String order ;
    public static AtomicInteger indexOrder;
    public static BufferedReader brOrdersProducts;
    public static BufferedWriter bwOrdersProducts;
    public static Semaphore sem;
    public static int products;

    public MyThreadLevel2(String order){
        this.order = order;
    }

    @Override
    public void run() {
        String str = null;
        try {
            while ((str = brOrdersProducts.readLine()) != null)
                // && !indexOrder.compareAndSet(products , products))
            {
                String delims = "[,]";
                String[] tokens = str.split(delims);

                if (tokens[0].equals(order)) {
                    sem.acquire();
                    indexOrder.addAndGet(1);
                    String displayInFile = str + ",shipped" + '\n';
                    bwOrdersProducts.write(displayInFile);
                    sem.release();
                    break;
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}