package ninja.kyle.projectchain;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZonedDateTime;

public class PricePoint {
  private final BigDecimal price;
  private final ZonedDateTime time;
  private final Duration delta;

  public PricePoint(BigDecimal price, ZonedDateTime time, Duration delta) {
    this.price = price;
    this.time = time;
    this.delta = delta;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public ZonedDateTime getTime() {
    return time;
  }

  public Duration getDelta() {
    return delta;
  }
}
