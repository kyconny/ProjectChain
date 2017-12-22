/*package ninja.kyle.projectchain.marketmaker;

import java.math.BigDecimal;

import ninja.kyle.projectchain.Asset;
import ninja.kyle.projectchain.Purchaser;
import ninja.kyle.projectchain.Seller;
import ninja.kyle.projectchain.exchanges.Exchange;

public class MarketMaker {

  private final Exchange exchange;

  private final Asset target;
  private BigDecimal targetStore;

  private final Asset medium;
  private BigDecimal mediumStore;

  private final Asset finalHold;

  private final BigDecimal sellTrigger;
  private final BigDecimal stopBuyTrigger;

  public MarketMaker(Exchange exchange, Asset target, Asset medium, Asset finalHold, BigDecimal sellTrigger, BigDecimal stopBuyTrigger) {
    this.exchange = exchange;
    this.target = target;
    this.medium = medium;
    this.finalHold = finalHold;
    this.sellTrigger = sellTrigger;
    this.stopBuyTrigger = stopBuyTrigger;

    this.targetStore = exchange.getAmmountOf(target);
    this.mediumStore = exchange.getAmmountOf(medium);
  }


  public void makeMarket(Purchaser purchaser, Seller seller) {

  }
}
*/