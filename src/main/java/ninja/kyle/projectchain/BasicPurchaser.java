package ninja.kyle.projectchain;

import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

import ninja.kyle.projectchain.exchanges.Exchange;
import ninja.kyle.projectchain.internallib.Pair;

public class BasicPurchaser {

  private final BigDecimal purchaseDelta;
  private final BigDecimal offerDelta;

  private final BookObserver bookObserver;

  private BigDecimal ourPrice = null;

  public BasicPurchaser(Exchange exchange, Pair<Asset,Asset> market, BigDecimal purchaseDelta, BigDecimal offerDelta) {
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

    if (ourPrice == null || ourPrice.compareTo(bestOffer) < 0 || ourPrice.subtract(purchaseDelta).compareTo(bestOffer) > 0) {
      beatBid(bestOffer);
    }
  }

  private void beatBid(BigDecimal bestOffer) {
    ourPrice = bestOffer.add(purchaseDelta);
    Logger.getLogger("Purchaser").log(Level.INFO, "purchase attempt at " + ourPrice);
  }

}
