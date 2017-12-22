package ninja.kyle.projectchain.exchanges.GDAX;

import java.math.BigDecimal;
import java.util.Optional;

import ninja.kyle.projectchain.Asset;
import ninja.kyle.projectchain.AssetBook;
import ninja.kyle.projectchain.exchanges.ExchangeQuerier;
import ninja.kyle.projectchain.internallib.Pair;

public class GDAXQuerier implements ExchangeQuerier {
  @Override
  public BigDecimal getAmmountOf(Asset asset) {
    throw new UnsupportedOperationException("Unimplemented");
  }

  @Override
  public Optional<String> limitOrder(Pair<Asset, Asset> market, AssetBook.OrderType side, BigDecimal ammount, BigDecimal price) {
    throw new UnsupportedOperationException("Unimplemented");
  }

  @Override
  public boolean tryCancelOrder(String id) {
    throw new UnsupportedOperationException("Unimplemented");
  }
}
