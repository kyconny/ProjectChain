package ninja.kyle.projectchain;

import com.google.common.collect.ImmutableList;

import java.math.BigDecimal;
import java.util.Deque;
import java.util.LinkedList;

public class PriceHistoryBuilder {
  private final Deque<PricePoint> priceList = new LinkedList<>();
  private final Long lastTime;

  public PriceHistoryBuilder() {
    this.lastTime = null;
  }

  public PriceHistoryBuilder(long initialDelta) {
    this.lastTime = initialDelta;
  }

  public void addPrice(BigDecimal price) {
    long time = System.currentTimeMillis();

    long lastTime = priceList.isEmpty() ? (this.lastTime == null ? time : this.lastTime) : priceList.getFirst().getTime();

    //We note that the time delta is very unlikely to be ~2^28 seconds
    //If it takes 8 years for a tick, then we have larger problems than
    //The delta being incorrect.
    priceList.addFirst(new PricePoint(price, (int) (time - lastTime), time));
  }

  public ImmutableList<PricePoint> getPriceHistory() {
    return ImmutableList.copyOf(priceList);
  }
}
