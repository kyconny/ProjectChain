package ninja.kyle.projectchain;

import java.math.BigDecimal;

public class PricePoint {
  private final BigDecimal price;
  private final int delta;
  private final long time;

  public PricePoint(BigDecimal price, int delta, long time) {
    this.price = price;
    this.delta = delta;
    this.time = time;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public int getDelta() {
    return delta;
  }

  public long getTime() {
    return time;
  }
}
