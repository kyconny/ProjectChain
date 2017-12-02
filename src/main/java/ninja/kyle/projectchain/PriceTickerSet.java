package ninja.kyle.projectchain;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.util.Optional;

import ninja.kyle.projectchain.internallib.Pair;

public class PriceTickerSet {

  private final ImmutableMap<Pair<Asset, Asset>, PriceHistory> priceHistoryMap;

  public PriceTickerSet(ImmutableSet<Pair<Asset, Asset>> tradingPairs) {
    ImmutableMap.Builder<Pair<Asset, Asset>, PriceHistory> builder = ImmutableMap.builder();
    tradingPairs.forEach(p -> builder.put(p, new PriceHistory()));
    priceHistoryMap = builder.build();
  }

  public final Optional<Double> getLastPrice(Pair<Asset, Asset> tradingPair) {
    return priceHistoryMap.get(tradingPair).getLastPrice();
  }

  final void addPairPrice(Pair<Asset, Asset> tradingPair, double price) {
    priceHistoryMap.get(tradingPair).addPrice(price);
  }

}
