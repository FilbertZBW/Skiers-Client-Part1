import java.sql.Timestamp;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class SkiersClient {

  final static private int NUM_THREADS_PHASE1 = 32;
  final static private int NUM_THREADS_PHASE2 = 168;

  static protected CountDownLatch latch = new CountDownLatch(1);
  static protected CountDownLatch latch2 = new CountDownLatch(200);

  static protected AtomicInteger ghghgh = new AtomicInteger(0);

  public static void main(String[] args) throws InterruptedException {
    long start = System.currentTimeMillis();
//    AtomicInteger scount = new AtomicInteger(0);
//    AtomicInteger ucount = new AtomicInteger(0);
    for (int i = 0; i < NUM_THREADS_PHASE1; i++) {
      SkiersClientThread skiersClientThread = new SkiersClientThread();
      skiersClientThread.start();
    }
    latch.await(); //wait until the latch value is zero
    //phase 2 code here: 168k left(assume 32k has been processed already)
    for (int i = 0; i < NUM_THREADS_PHASE2; i++) {
      SkiersClientThread skiersClientThread = new SkiersClientThread();
      skiersClientThread.start();
    }
    latch2.await();
    long end = System.currentTimeMillis();
    long totalTime = (end - start);
    long throughput = 200000 / (totalTime / 1000);
    System.out.println("Total time: " + totalTime + " milli seconds");

    System.out.println("Throughput: " + throughput + " requests per seconds");

    // deploy to ec2

  }


}
