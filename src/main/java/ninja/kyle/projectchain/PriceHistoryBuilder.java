package ninja.kyle.projectchain;

import com.google.common.collect.ImmutableList;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

public class PriceHistoryBuilder {

  private final ReentrantLock priceListLock = new ReentrantLock();
  private final Deque<PricePoint> priceList = new LinkedList<>();

  private final ZonedDateTime lastTime;

  public PriceHistoryBuilder() {
    this.lastTime = null;
  }

  public PriceHistoryBuilder(ZonedDateTime lastTime) {
    this.lastTime = lastTime;
  }

  public void addPrice(BigDecimal price) {
    ZonedDateTime time = ZonedDateTime.now();

    priceListLock.lock();

    ZonedDateTime lastTime = priceList.isEmpty() ? (this.lastTime == null ? time : this.lastTime) : priceList.getFirst().getTime();

    //We note that the time delta is very unlikely to be ~2^28 seconds
    //If it takes 8 years for a tick, then we have larger problems than
    //The delta being incorrect.
    priceList.addFirst(new PricePoint(price, time, Duration.between(lastTime, time)));

    priceListLock.unlock();
  }

  public ImmutableList<PricePoint> getPriceHistory() {
    priceListLock.lock();
    ImmutableList<PricePoint> immList = ImmutableList.copyOf(priceList);
    priceListLock.unlock();
    return immList;
  }
}
