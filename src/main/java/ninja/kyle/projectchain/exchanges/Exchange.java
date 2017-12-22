package ninja.kyle.projectchain.exchanges;

import com.google.common.collect.ImmutableMultimap;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import ninja.kyle.projectchain.Asset;
import ninja.kyle.projectchain.AssetBook;
import ninja.kyle.projectchain.ExchangeBook;
import ninja.kyle.projectchain.PriceMultimapBuilder;
import ninja.kyle.projectchain.PricePoint;
import ninja.kyle.projectchain.internallib.Pair;

public abstract class Exchange {

  private final ExchangeQuerier querier;
  private final Set<Pair<Asset,Asset>> tradingPairs;

  private final ExchangeBook exchangeBook;
  private final PriceMultimapBuilder priceMultimapBuilder;

  protected Exchange(ExchangeQuerier querier, Set<Pair<Asset, Asset>> tradingPairs) {
    this.querier = querier;
    this.tradingPairs = tradingPairs;

    this.exchangeBook = new ExchangeBook(tradingPairs, 20);
    this.priceMultimapBuilder = new PriceMultimapBuilder(tradingPairs);
  }

  public final Set<Pair<Asset,Asset>> getTradingPairs() {
    return tradingPairs;
  }

  protected final ExchangeBook getExchangeBook() {
    return exchangeBook;
  }

  protected final PriceMultimapBuilder getPriceMultimapBuilder() {
    return priceMultimapBuilder;
  }

  @Deprecated
  public final void addPriceHistoryObserver(Consumer<ImmutableMultimap<Pair<Asset, Asset>, PricePoint>> observer) {
    priceMultimapBuilder.addHistoryObserver(observer);
  }

  public final void addPriceObserver(Pair<Asset, Asset> market, Consumer<BigDecimal> observer) {
    priceMultimapBuilder.addPriceObserver(market, observer);
  }

  public final void removePriceObserver(Pair<Asset, Asset> market, Consumer<BigDecimal> observer) {
    priceMultimapBuilder.removePriceObserver(market, observer);
  }

  public final void addBookObserver(Pair<Asset, Asset> market, Consumer<AssetBook> observer) {
    exchangeBook.addBookObserver(market, observer);
  }

  public final void removeBookObserver(Pair<Asset, Asset> market, Consumer<AssetBook> observer) {
    exchangeBook.removeBookObserver(market, observer);
  }

  public final void flushPriceData() {
    priceMultimapBuilder.flushMultimap();
  }

  public final BigDecimal getSpread(Pair<Asset, Asset> tradingPair) {
    BigDecimal pAsk = exchangeBook.getMostReasonable(tradingPair, AssetBook.OrderType.ASK).getLeft();
    BigDecimal pBid = exchangeBook.getMostReasonable(tradingPair, AssetBook.OrderType.BID).getLeft();
    return pAsk.subtract(pBid);
  }

  protected abstract boolean shouldAllowQuery(ExchangePriority priority);

  public Optional<ExchangeQuerier> tryUnlockQuerier(ExchangePriority priority) {
    if (shouldAllowQuery(priority)) {
      return Optional.of(querier);
    } else {
      return Optional.empty();
    }
  }

}
