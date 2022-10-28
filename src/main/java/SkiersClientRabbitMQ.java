import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class SkiersClientRabbitMQ {
  final static private int NUM_THREADS = 50;

  static protected CountDownLatch latch = new CountDownLatch(50);

  public static void main(String[] args) throws InterruptedException {
    long start = System.currentTimeMillis();

    for (int i = 0; i < NUM_THREADS; i++) {
      SkiersClientThread skiersClientThread = new SkiersClientThread();
      skiersClientThread.start();
    }

//    for (int i = 0; i < NUM_THREADS; i++) {
//      SkiersClientThread2 skiersClientThread = new SkiersClientThread2();
//      skiersClientThread.start();
//    }

    latch.await();
    long end = System.currentTimeMillis();
    long totalTime = (end - start);
    double throughput = 2500 / (totalTime / 1000.0);

    System.out.println("Total run time: " + totalTime / 1000.0 + "seconds");
    System.out.println("Throughput: " + throughput + " requests per seconds");
  }
}
