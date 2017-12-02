package ninja.kyle.projectchain;

import com.google.common.collect.ImmutableList;

import java.util.Deque;
import java.util.LinkedList;

public class PriceHistoryBuilder {
  private final Deque<PricePoint> priceList = new LinkedList<>();

  public void addPrice(double price) {
    long time = System.currentTimeMillis();

    PricePoint lastTick = priceList.getFirst();
    long lastTime = lastTick == null ? 0 : lastTick.getTime();

    //We note that the time delta is very unlikely to be ~2^28 seconds
    //If it takes 8 years for a tick, then we have larger problems than
    //The delta being incorrect.
    priceList.addFirst(new PricePoint(price, (int) (time - lastTime), time));
  }

  public ImmutableList<PricePoint> getPriceHistory() {
    return ImmutableList.copyOf(priceList);
  }
}
