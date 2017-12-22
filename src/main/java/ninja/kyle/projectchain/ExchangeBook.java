package ninja.kyle.projectchain;

import com.google.common.collect.ImmutableMap;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import ninja.kyle.projectchain.internallib.Pair;

public class ExchangeBook {

  private final ImmutableMap<Pair<Asset, Asset>, AssetBook> marketBooks;
  private final int limit;

  private final List<Pair<Pair<Asset,Asset>, Consumer<AssetBook>>> observers = new CopyOnWriteArrayList<>();

  public ExchangeBook(Set<Pair<Asset, Asset>> tradingPairs) {
    this(tradingPairs, 0);
  }

  public ExchangeBook(Set<Pair<Asset, Asset>> tradingPairs, int limit) {
    ImmutableMap.Builder<Pair<Asset, Asset>, AssetBook> builder = ImmutableMap.builder();
    tradingPairs.forEach(p -> builder.put(p, new AssetBook(limit)));
    this.marketBooks = builder.build();

    this.limit = limit;
  }

  public void addBookObserver(Pair<Asset,Asset> tradingPair, Consumer<AssetBook> observer) {
    observers.add(new Pair<>(tradingPair, observer));
  }

  public void removeBookObserver(Pair<Asset, Asset> tradingPair, Consumer<AssetBook> observer) {
    observers.remove(new Pair<>(tradingPair, observer));
  }

  public Pair<BigDecimal, BigDecimal> getMostReasonable(Pair<Asset, Asset> tradingPair, AssetBook.OrderType type) {
    return getAssetBook(tradingPair).getMostReasonable(type);
  }

  public void putNumberOfOrders(Pair<Asset, Asset> tradingPair, AssetBook.OrderType type, BigDecimal price, BigDecimal number) {
    AssetBook assetBook = getAssetBook(tradingPair);

    assetBook.putNumberOfOrders(type, price, number);

    for (Pair<Pair<Asset, Asset>, Consumer<AssetBook>> observer : observers) {
      if (observer.getLeft().equals(tradingPair)) {
        observer.getRight().accept(assetBook);
      }
    }
  }

  private AssetBook getAssetBook(Pair<Asset, Asset> tradingPair) {
    AssetBook book = marketBooks.get(tradingPair);

    if (book == null) {
      throw new UnsupportedOperationException("Attempted to get data for nonexistinant trading pair.");
    }

    return book;
  }


}
