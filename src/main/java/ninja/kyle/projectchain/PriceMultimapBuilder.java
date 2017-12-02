package ninja.kyle.projectchain;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import ninja.kyle.projectchain.internallib.Pair;

public class PriceMultimapBuilder {

  private final List<Consumer<ImmutableMultimap<Pair<Asset, Asset>, PricePoint>>> observerList = new LinkedList<>();

  private Map<Pair<Asset, Asset>, PriceHistoryBuilder> priceHistoryMap = new HashMap<>();

  public PriceMultimapBuilder(Set<Pair<Asset, Asset>> tradingPairs) {
    clearMap(tradingPairs);
  }

  public void addObserver(Consumer<ImmutableMultimap<Pair<Asset, Asset>, PricePoint>> observer) {
    observerList.add(observer);
  }

  public void addPairPrice(Pair<Asset, Asset> tradingPair, double price) {
    PriceHistoryBuilder builder = priceHistoryMap.get(tradingPair);

    if (builder == null) {
      throw new UnsupportedOperationException("Tried to add price to unsupported trading pair.");
    }

    builder.addPrice(price);
  }

  public void flushMultimap() {
    ImmutableMultimap<Pair<Asset, Asset>, PricePoint> priceMultimap = getPriceMultimap();
    observerList.forEach(c -> c.accept(priceMultimap));
    clearMap(priceHistoryMap.keySet());
  }

  private void clearMap(Set<Pair<Asset, Asset>> tradingPairs) {
    tradingPairs.forEach(p -> priceHistoryMap.put(p, new PriceHistoryBuilder()));
  }

  private ImmutableMultimap<Pair<Asset, Asset>, PricePoint> getPriceMultimap() {
    ImmutableMultimap.Builder<Pair<Asset, Asset>, PricePoint> builder = ImmutableMultimap.builder();
    priceHistoryMap.forEach((k, v) -> builder.putAll(k, v.getPriceHistory()));
    return builder.build();
  }

}
