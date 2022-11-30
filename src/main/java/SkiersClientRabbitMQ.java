import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import redis.clients.jedis.Jedis;

public class SkiersClientRabbitMQ {
  final static private int NUM_THREADS = 50;

  static protected CountDownLatch latch = new CountDownLatch(50);

  public static void main(String[] args) throws InterruptedException {
    long start = System.currentTimeMillis();

    for (int i = 0; i < NUM_THREADS; i++) {
      SkiersClientThread skiersClientThread = new SkiersClientThread();
      skiersClientThread.start();
    }

    latch.await();
    long end = System.currentTimeMillis();
    long totalTime = (end - start);
    double throughput = 5000 / (totalTime / 1000.0);

    System.out.println("Total run time: " + totalTime / 1000.0 + "seconds");
    System.out.println("Throughput: " + throughput + " requests per seconds");

    Jedis jedis = new Jedis("127.0.0.1", 6379);
    //TODO:
    // * 1. “For skier N, how many days have they skied this season?” --> key: skier value: day
    // * 2. “For skier N, what are the vertical totals for each ski day?” (calculate vertical as liftID*10) --> key: skiday value: a set of liftIDs
    // * 3. “For skier N, show me the lifts they rode on each ski day”
    // * 4. “How many unique skiers visited resort X on day N?”
    System.out.print("Days the skier skied this season: ");
    System.out.println(jedis.smembers("skierId:1").size());
    System.out.println("=========================================\n=========================================");
    Set<String> days = jedis.smembers("skierId:1");

    for (String day : days) {
      Set<String> liftId = jedis.smembers("dayId:" + day);
      int verticalTotal = liftId.stream().mapToInt(Integer::parseInt).sum() * 10;
      System.out.println("Vertical total for day " + day + " is: " + verticalTotal);
      StringBuilder sb = new StringBuilder();
      for (String lift : liftId) {
        sb.append(lift).append("|");
      }
      System.out.println("The lifts taken:" + sb);
    }
    System.out.println("=========================================\n=========================================");
    System.out.println("Unique skier visited resort 1: " + jedis.smembers("resortId:1").size());
  }
}
