package ninja.kyle.projectchain;

import com.google.common.collect.ImmutableMultimap;

import java.util.List;
import java.util.function.Consumer;

import ninja.kyle.projectchain.exchanges.GDAX.GDAX;
import ninja.kyle.projectchain.internallib.Pair;

public class PriceTickerMain {

  private static final Consumer<ImmutableMultimap<Pair<Asset, Asset>, PricePoint>> onGDAXFlush = m -> {
    List<PricePoint> list = m.get(new Pair<>(Asset.BTC, Asset.USD)).asList();
    for (PricePoint p : list) {
      System.out.println(p.getTime() + ": " + list.get(0).getPrice() + " with delta " + p.getDelta() + " with latency " + p.getLatency().toMillis());
    }
  };


  public static void main(String[] args) {
    GDAX gdax = GDAX.connectToGDAX();
    gdax.addPriceObserver(onGDAXFlush);

    while (true) {
      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      gdax.flushPriceData();
    }
  }

}
