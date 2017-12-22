package ninja.kyle.projectchain;

import com.google.common.collect.ImmutableMultimap;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;

import ninja.kyle.projectchain.exchanges.GDAX.GDAX;
import ninja.kyle.projectchain.internallib.Pair;

public class PriceTickerMain {

  private static final Consumer<ImmutableMultimap<Pair<Asset, Asset>, PricePoint>> onGDAXFlush = m -> {
    List<PricePoint> list = m.get(new Pair<>(Asset.BTC, Asset.GBP)).asList();
    for (PricePoint p : list) {
      System.out.println(p.getTime() + ": " + list.get(0).getPrice() + " with delta " + p.getDelta() + " with latency " + p.getLatency().toMillis());
    }
  };


  public static void main(String[] args) {
    GDAX gdax = GDAX.connectToGDAX();
    //gdax.addPriceHistoryObserver(onGDAXFlush);
    gdax.addPriceObserver(new Pair<>(Asset.BTC, Asset.GBP), bd -> System.out.println("lols " + bd));
    BasicPurchaser purchaser = new BasicPurchaser(gdax, new Pair<>(Asset.BTC, Asset.GBP), new BigDecimal("0.01"), null);

    purchaser.startPurchasing();

    while (true) {
      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      gdax.flushPriceData();
      System.out.println("Spread: " + gdax.getSpread(new Pair<>(Asset.BTC, Asset.GBP)));
    }
  }

}
