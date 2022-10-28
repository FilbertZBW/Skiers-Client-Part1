import java.sql.Timestamp;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class SkiersClient {

  final static private int NUM_THREADS_PHASE1 = 32;
  final static private int NUM_THREADS_PHASE2 = 168;

  static protected CountDownLatch latch = new CountDownLatch(1);
  static protected CountDownLatch latch2 = new CountDownLatch(200);

  public static void main(String[] args) throws InterruptedException {
    long start = System.currentTimeMillis();
    //phase 1
    for (int i = 0; i < NUM_THREADS_PHASE1; i++) {
      SkiersClientThread skiersClientThread = new SkiersClientThread();
      skiersClientThread.start();
    }
    latch.await(); //wait until the latch value is zero
    //phase 2
    for (int i = 0; i < NUM_THREADS_PHASE2; i++) {
      SkiersClientThread skiersClientThread = new SkiersClientThread();
      skiersClientThread.start();
    }
    latch2.await();
    long end = System.currentTimeMillis();
    long totalTime = (end - start);
    long throughput = 200000 / (totalTime / 1000);
    System.out.println("Total run time: " + totalTime / 1000.0 + "seconds");
    System.out.println("Throughput: " + throughput + " requests per seconds");
  }
}
