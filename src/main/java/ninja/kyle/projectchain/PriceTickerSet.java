package ninja.kyle.projectchain;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import ninja.kyle.projectchain.internallib.Pair;

public class PriceTickerSet {

  private final ImmutableSet<Pair<Asset, Asset>> tradingPairs;
  private final ImmutableMap<Pair<Asset, Asset>, PriceHistory> priceHistoryMap;

  public PriceTickerSet(ImmutableSet<Pair<Asset, Asset>> tradingPairs) {
    this.tradingPairs = tradingPairs;
    
    ImmutableMap.Builder<Pair<Asset, Asset>, PriceHistory> builder = ImmutableMap.builder();
    ImmutableSet<Pair<Asset, Asset>> pairs = getTradingPairs();
    pairs.forEach(p -> builder.put(p, new PriceHistory()));
    
    priceHistoryMap = builder.build();
  }

  public final ImmutableSet<Pair<Asset, Asset>> getTradingPairs() {
    return tradingPairs;
  }

  public final double getLastPrice(Pair<Asset, Asset> tradingPair) {
    return 0;
  }

  final void addPairPrice(Pair<Asset, Asset> tradingPair, double price) {
    priceHistoryMap.get(tradingPair);
  }

  //TODO: Trading functionality

}
