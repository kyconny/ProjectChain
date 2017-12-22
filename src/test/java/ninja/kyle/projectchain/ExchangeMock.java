package ninja.kyle.projectchain;

import java.util.Set;

import ninja.kyle.projectchain.exchanges.Exchange;
import ninja.kyle.projectchain.exchanges.ExchangePriority;
import ninja.kyle.projectchain.exchanges.ExchangeQuerier;
import ninja.kyle.projectchain.internallib.Pair;

public class ExchangeMock extends Exchange {


  private boolean shouldAllowQuery = false;

  protected ExchangeMock(ExchangeQuerier querier, Set<Pair<Asset, Asset>> tradingPairs) {
    super(querier, tradingPairs);
  }

  public void setAllowQuery(boolean value) {
    shouldAllowQuery = value;
  }

  public ExchangeBook getTestExchangeBook() {
    return this.getExchangeBook();
  }

  public PriceMultimapBuilder getTestMultimapBuilder() {
    return this.getPriceMultimapBuilder();
  }

  @Override
  protected boolean shouldAllowQuery(QueryRecorder queryRecorder, ExchangePriority priority, int queries) {
    return shouldAllowQuery;
  }
}
