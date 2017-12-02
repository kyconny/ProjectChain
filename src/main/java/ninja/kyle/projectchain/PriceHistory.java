package ninja.kyle.projectchain;

import java.util.Optional;
import java.util.Stack;

public class PriceHistory {
  private final Stack<PricePoint> priceList = new Stack<>();

  public void addPrice(double price) {
    long time = System.currentTimeMillis();

    PricePoint lastTick = priceList.peek();
    long lastTime = lastTick == null ? 0 : lastTick.getTime();

    //We note that the time delta is very unlikely to be ~2^28 seconds
    //If it takes 8 years for a tick, then we have larger problems than
    //The delta being incorrect.
    priceList.add(new PricePoint(price, (int) (time - lastTime), time));
  }

  public Optional<Double> getLastPrice() {
    PricePoint lastTick = priceList.peek();
    return lastTick == null ? Optional.empty() : Optional.of(lastTick.getPrice());
  }
}
