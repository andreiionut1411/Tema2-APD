import com.sun.tools.javac.Main;

import java.io.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class MyThread extends Thread {
    static final String a = "LOCK";
    private int id ;
    public static BufferedReader brOrders;
    public static BufferedWriter bwOrdersProducts;
    public static BufferedWriter bwOrders;
    public static Semaphore sem;
    public static File file;

    public MyThread(int id){
        this.id = id;
    }

    @Override
    public void run() {
        String str = null;
        try {
            while ((str = brOrders.readLine()) != null) {
                synchronized (a) {
                    String delims = "[,]";
                    String[] tokens = str.split(delims);

                    int nbThreadsLevel2 = Integer.parseInt(tokens[1]);
                    if (nbThreadsLevel2 != 0) {
                        String displayInFile = str + ",shipped" + '\n';
                        bwOrders.write(displayInFile);
                    }

                    MyThreadLevel2[] t = new MyThreadLevel2[nbThreadsLevel2];
                    MyThreadLevel2.indexOrder = new AtomicInteger(0);

                    FileReader frOrdersProducts = new FileReader(file);
                    BufferedReader brOrdersProducts = new BufferedReader(frOrdersProducts);

                    MyThreadLevel2.brOrdersProducts = brOrdersProducts;
                    MyThreadLevel2.bwOrdersProducts = bwOrdersProducts;
                    MyThreadLevel2.sem = sem;
                    MyThreadLevel2.products = nbThreadsLevel2;

                    //System.out.println(tokens[1]);
                    for (int i = 0; i < nbThreadsLevel2; ++i) {
                        t[i] = new MyThreadLevel2(tokens[0]);
                        t[i].start();
                    }

                    for (int i = 0; i < nbThreadsLevel2; ++i) {
                        try {
                            t[i].join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}