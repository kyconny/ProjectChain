package ninja.kyle.projectchain.exchanges;

import java.math.BigDecimal;
import java.util.Optional;

import ninja.kyle.projectchain.Asset;
import ninja.kyle.projectchain.AssetBook;
import ninja.kyle.projectchain.internallib.Pair;

public class LockedQuerier implements ExchangeQuerier {

  private final ExchangeQuerier querier;

  private int avaliableQueries;

  public LockedQuerier(ExchangeQuerier querier, int queries) {
    this.querier = querier;
    this.avaliableQueries = queries;
  }

  public void queryCheck() {
    if (avaliableQueries <= 0) {
      throw new RuntimeException("Tried to execute query more times than allocated by lockedquerier");
    }
  }

  @Override
  public BigDecimal getAmmountOf(Asset asset) {
    queryCheck();
    return querier.getAmmountOf(asset);
  }

  @Override
  public Optional<String> limitOrder(Pair<Asset, Asset> market, AssetBook.OrderType side, BigDecimal ammount, BigDecimal price) {
    queryCheck();
    return querier.limitOrder(market, side, ammount, price);
  }

  @Override
  public boolean tryCancelOrder(String id) {
    queryCheck();
    return querier.tryCancelOrder(id);
  }
}
