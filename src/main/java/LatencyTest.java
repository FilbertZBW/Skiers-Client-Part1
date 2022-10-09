import java.util.concurrent.CountDownLatch;

public class LatencyTest {
  static protected CountDownLatch testLatch = new CountDownLatch(1);

  public static void main(String[] args) throws InterruptedException {
    long start = System.currentTimeMillis();
    int numRequests = 10000;
    SkiersClientTestThread skiersClientThread = new SkiersClientTestThread(numRequests);
    skiersClientThread.start();
    testLatch.await();
    long end = System.currentTimeMillis();
    long totalTime = end - start;
    double oneRequestTime = totalTime / (float)numRequests;
//    System.out.println("Total time for one thread to send 1k requests: " + totalTime + " milli seconds");
//    System.out.println("Average latency for one request: " + oneRequestTime + " milli seconds");
    System.out.println("Throughput: " + 20 / (oneRequestTime / 1000) + " requests per second");
  }
}
