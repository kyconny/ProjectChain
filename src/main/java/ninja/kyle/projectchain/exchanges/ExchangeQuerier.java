package ninja.kyle.projectchain.exchanges;

import java.math.BigDecimal;

import ninja.kyle.projectchain.Asset;
import ninja.kyle.projectchain.AssetBook;
import ninja.kyle.projectchain.internallib.Pair;

public abstract class ExchangeQuerier {
  public abstract BigDecimal getAmmountOf(Asset asset);
  public abstract String limitOrder(Pair<Asset, Asset> market, AssetBook.OrderType side, BigDecimal ammount, BigDecimal price);
  public abstract boolean tryCancelOrder(String id);
}
