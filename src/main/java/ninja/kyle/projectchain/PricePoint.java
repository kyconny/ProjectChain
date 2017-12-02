package ninja.kyle.projectchain;

public class PricePoint {
  private final double price;
  private final int delta;
  private final long time;

  public PricePoint(double price, int delta, long time) {
    this.price = price;
    this.delta = delta;
    this.time = time;
  }

  public double getPrice() {
    return price;
  }

  public int getDelta() {
    return delta;
  }

  public long getTime() {
    return time;
  }
}
