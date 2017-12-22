package ninja.kyle.projectchain;

import com.google.common.collect.ImmutableMultimap;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import ninja.kyle.projectchain.internallib.Pair;

public class PriceMultimapBuilder {

  private final List<Consumer<ImmutableMultimap<Pair<Asset, Asset>, PricePoint>>> historyObserverList = new CopyOnWriteArrayList<>();
  private final List<Pair<Pair<Asset, Asset>, Consumer<BigDecimal>>> priceObserverList = new CopyOnWriteArrayList<>();

  private final Map<Pair<Asset, Asset>, PriceHistoryBuilder> priceHistoryMap = new ConcurrentHashMap<>();

  public PriceMultimapBuilder(Set<Pair<Asset, Asset>> tradingPairs) {
    tradingPairs.forEach(p -> priceHistoryMap.put(p, new PriceHistoryBuilder()));
  }

  public void addHistoryObserver(Consumer<ImmutableMultimap<Pair<Asset, Asset>, PricePoint>> observer) {
    historyObserverList.add(observer);
  }

  public void addPriceObserver(Pair<Asset, Asset> market, Consumer<BigDecimal> observer) {
    priceObserverList.add(new Pair<>(market, observer));
  }

  public void removePriceObserver(Pair<Asset, Asset> market, Consumer<BigDecimal> observer) {
    priceObserverList.remove(new Pair<>(market, observer));
  }

  public void addPairPrice(Pair<Asset, Asset> tradingPair, BigDecimal price, ZonedDateTime time) {
    PriceHistoryBuilder builder = priceHistoryMap.get(tradingPair);

    if (builder == null) {
      throw new UnsupportedOperationException("Tried to add price to unsupported trading pair.");
    }

    builder.addPrice(price, time);

    for (Pair<Pair<Asset, Asset>, Consumer<BigDecimal>> priceObserverPair : priceObserverList) {
        if (priceObserverPair.getLeft().equals(tradingPair)) {
          priceObserverPair.getRight().accept(price);
        }
    }

    historyObserverList.forEach(c -> c.accept(getPriceMultimap()));
  }

  public void flushMultimap() {
    clearMap(priceHistoryMap.keySet());
  }

  private void clearMap(Set<Pair<Asset, Asset>> tradingPairs) {
    for (Pair<Asset, Asset> p : tradingPairs) {
      List<PricePoint> history = priceHistoryMap.get(p).getPriceHistory();
      if (!history.isEmpty()) {
        priceHistoryMap.put(p, new PriceHistoryBuilder(history.get(0).getTime()));
      } else {
        priceHistoryMap.put(p, new PriceHistoryBuilder());
      }
    }
  }

  private ImmutableMultimap<Pair<Asset, Asset>, PricePoint> getPriceMultimap() {
    ImmutableMultimap.Builder<Pair<Asset, Asset>, PricePoint> builder = ImmutableMultimap.builder();
    priceHistoryMap.forEach((k, v) -> builder.putAll(k, v.getPriceHistory()));
    return builder.build();
  }
}
