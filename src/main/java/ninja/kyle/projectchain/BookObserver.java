package ninja.kyle.projectchain;

import java.util.function.Consumer;

import ninja.kyle.projectchain.exchanges.Exchange;
import ninja.kyle.projectchain.internallib.Pair;

public class BookObserver {

  private final Consumer<AssetBook> observer;
  private final Exchange exchange;
  private final Pair<Asset, Asset> market;

  public BookObserver(Consumer<AssetBook> observer, Exchange exchange, Pair<Asset, Asset> market) {
    this.observer = observer;
    this.exchange = exchange;
    this.market = market;
  }

  public void startObserving() {
    exchange.addBookObserver(market, observer);
  }

  public void stopObserving() {
    exchange.removeBookObserver(market, observer);
  }

}
