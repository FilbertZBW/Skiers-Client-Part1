import java.util.concurrent.atomic.AtomicInteger;

import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.SkiersApi;
import io.swagger.client.model.LiftRide;

public class SkiersClientTestThread extends Thread {

  protected AtomicInteger successfulCount;
  protected AtomicInteger unsuccessfulCount;
  private final int numReqs;

  public SkiersClientTestThread(int numReqs) {
    this.numReqs = numReqs;
    this.successfulCount = new AtomicInteger(0);
    this.unsuccessfulCount = new AtomicInteger(0);
  }

  @Override
  public void run() {
    for (int i = 0; i < numReqs; i++) {
      SkiersApi skiersApi = new SkiersApi();
      skiersApi.getApiClient().setBasePath("http://18.237.10.252:8080/lab2_war/");
      LiftRide liftRideBody = new LiftRide();
      LiftRideEvent liftRideEvent = new LiftRideEvent();
      liftRideBody.setTime(liftRideEvent.getTime());
      liftRideBody.setLiftID(liftRideEvent.getLiftID());
      try {
        ApiResponse<Void> apiResponse = skiersApi.writeNewLiftRideWithHttpInfo(liftRideBody,
                liftRideEvent.getResortID(), liftRideEvent.getSeasonID(), liftRideEvent.getDayID(),
                liftRideEvent.getSkierID());
        int statusCode = apiResponse.getStatusCode();
//        System.out.println(statusCode);
        int cnt = 0;
        while ((statusCode / 100 == 4 || statusCode / 100 == 5) && cnt < 4) {
          ApiResponse<Void> resp = skiersApi.writeNewLiftRideWithHttpInfo(liftRideBody,
                  liftRideEvent.getResortID(), liftRideEvent.getSeasonID(), liftRideEvent.getDayID(),
                  liftRideEvent.getSkierID());
          statusCode = resp.getStatusCode();
          cnt++;
        }
        if (statusCode == 200) {
          successfulCount.incrementAndGet();
        } else {
          unsuccessfulCount.incrementAndGet();
        }
      } catch (ApiException e) {
        System.err.println("Exception when calling SkiersApi#writeNewLiftRideWithHttpInfo");
        e.printStackTrace();
      }
    }

    LatencyTest.testLatch.countDown();
  }
}
