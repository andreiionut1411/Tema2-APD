import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.Semaphore;

public class Tema2 {

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println("Usage: Tema2 <folder_input> <nr_max_threads>");
            return;
        }

        File folderInput = new File(args[0]);
        int NUMBER_OF_THREADS = Integer.parseInt(args[1]);
        MyThread[] t = new MyThread[NUMBER_OF_THREADS];

        File[] files = folderInput.listFiles();
        // files[0] -> orders.txt
        // files[1] -> order_products

        FileReader frOrders = new FileReader(folderInput + "/orders.txt");
        BufferedReader brOrders = new BufferedReader(frOrders);
        Semaphore sem = new Semaphore(NUMBER_OF_THREADS);
        FileWriter fwOrders = new FileWriter("orders_out.txt");
        BufferedWriter bwOrders = new BufferedWriter(fwOrders);
        FileWriter fwOrdersProducts = new FileWriter("order_products_out.txt");
        BufferedWriter bwOrdersProducts = new BufferedWriter(fwOrdersProducts);

        MyThread.brOrders = brOrders;
        MyThread.file = new File(folderInput + "/order_products.txt");
        MyThread.sem = sem;
        MyThread.bwOrders = bwOrders;
        MyThread.bwOrdersProducts = bwOrdersProducts;
        for (int i = 0; i < NUMBER_OF_THREADS; ++i) {
            t[i] = new MyThread(i);
            t[i].start();
        }

        for (int i = 0; i < NUMBER_OF_THREADS; ++i) {
            try {
                t[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        bwOrdersProducts.close();
        bwOrders.close();
    }
}
