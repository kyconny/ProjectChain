package ninja.kyle.projectchain.exchanges.GDAX;

import java.math.BigDecimal;

import ninja.kyle.projectchain.Asset;
import ninja.kyle.projectchain.AssetBook;
import ninja.kyle.projectchain.exchanges.ExchangeQuerier;
import ninja.kyle.projectchain.internallib.Pair;

public class GDAXQuerier extends ExchangeQuerier {
  @Override
  public BigDecimal getAmmountOf(Asset asset) {
    throw new UnsupportedOperationException("Unimplemented");
  }

  @Override
  public String limitOrder(Pair<Asset, Asset> market, AssetBook.OrderType side, BigDecimal ammount, BigDecimal price) {
    throw new UnsupportedOperationException("Unimplemented");
  }

  @Override
  public boolean tryCancelOrder(String id) {
    throw new UnsupportedOperationException("Unimplemented");
  }
}
