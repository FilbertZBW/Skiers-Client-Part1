import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class LatencyTest {
  static protected CountDownLatch testLatch = new CountDownLatch(1);

  public static void main(String[] args) throws InterruptedException {
    long start = System.currentTimeMillis();
    int numRequests = 1000;
    SkiersClientTestThread skiersClientThread = new SkiersClientTestThread(numRequests,
            new AtomicInteger(0), new AtomicInteger(0));
    skiersClientThread.start();
    testLatch.await();
    long end = System.currentTimeMillis();
    long totalTime = end - start;
//    double oneRequestTime = totalTime / (float)numRequests;

    System.out.println("totalTime: " + totalTime / 1000 + "seconds");
    System.out.println("Throughput: " + numRequests / totalTime * 1000 + " requests per second");

  }
}
