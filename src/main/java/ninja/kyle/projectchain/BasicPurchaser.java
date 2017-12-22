package ninja.kyle.projectchain;

import java.math.BigDecimal;
import java.util.Optional;

import ninja.kyle.projectchain.exchanges.Exchange;
import ninja.kyle.projectchain.exchanges.ExchangePriority;
import ninja.kyle.projectchain.exchanges.ExchangeQuerier;
import ninja.kyle.projectchain.internallib.Pair;

public class BasicPurchaser {

  private final Exchange exchange;
  private final Pair<Asset, Asset> market;

  private final BigDecimal purchaseDelta;
  private final BigDecimal offerDelta;

  private final BookObserver bookObserver;

  private BigDecimal lastOrderPrice = null;
  private String lastOrderID = null;

  public BasicPurchaser(Exchange exchange, Pair<Asset,Asset> market, BigDecimal purchaseDelta, BigDecimal offerDelta) {
    this.exchange = exchange;
    this.market = market;
    this.purchaseDelta = purchaseDelta;
    this.offerDelta = offerDelta;
    bookObserver = new BookObserver(this::onBookUpdate, exchange, market);
  }

  public void startPurchasing() {
    bookObserver.startObserving();
  }

  public void stopPurchasing() {
    bookObserver.stopObserving();
  }

  private void onBookUpdate(AssetBook book) {
    BigDecimal bestOffer = book.getMostReasonable(AssetBook.OrderType.BID).getLeft();

    if (lastOrderPrice == null || lastOrderID == null || lastOrderPrice.compareTo(bestOffer) < 0 || lastOrderPrice.subtract(purchaseDelta).compareTo(bestOffer) > 0) {
      beatBid(bestOffer);
    }
  }

  private void beatBid(BigDecimal bestOffer) {
    BigDecimal ourPrice = bestOffer.add(purchaseDelta);

    Optional<ExchangeQuerier> maybeQuerier = exchange.tryUnlockQuerier(ExchangePriority.HIGH, 2);

    if (lastOrderID != null) {
      maybeQuerier.ifPresent(q -> q.tryCancelOrder(lastOrderID));
    }

    maybeQuerier.flatMap(q -> q.limitOrder(market, AssetBook.OrderType.BID, offerDelta, ourPrice)).ifPresent(s -> {
      lastOrderID = s;
      lastOrderPrice = ourPrice;
    });
  }

}
