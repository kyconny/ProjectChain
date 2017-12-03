package ninja.kyle.projectchain;

import com.google.common.collect.ImmutableMap;

import java.math.BigDecimal;
import java.util.Set;

import ninja.kyle.projectchain.internallib.Pair;

public class ExchangeBook {

  private final ImmutableMap<Pair<Asset, Asset>, AssetBook> marketBooks;

  public ExchangeBook(Set<Pair<Asset, Asset>> tradingPairs) {
    ImmutableMap.Builder<Pair<Asset, Asset>, AssetBook> builder = ImmutableMap.builder();
    tradingPairs.forEach(p -> builder.put(p, new AssetBook()));
    this.marketBooks = builder.build();
  }

  public Pair<BigDecimal, Integer> getMostReasonable(Pair<Asset, Asset> tradingPair, AssetBook.OrderType type) {
    return getAssetBook(tradingPair).getMostReasonable(type);
  }

  public void putNumberOfOrders(Pair<Asset, Asset> tradingPair, AssetBook.OrderType type, BigDecimal price, Integer number) {
    getAssetBook(tradingPair).putNumberOfOrders(type, price, number);
  }

  private AssetBook getAssetBook(Pair<Asset, Asset> tradingPair) {
    AssetBook book = marketBooks.get(tradingPair);

    if (book == null) {
      throw new UnsupportedOperationException("Attempted to get data for nonexistinant trading pair.");
    }

    return book;
  }
}
