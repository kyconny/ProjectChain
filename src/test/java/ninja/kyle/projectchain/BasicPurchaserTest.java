package ninja.kyle.projectchain;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import ninja.kyle.projectchain.exchanges.ExchangeQuerier;
import ninja.kyle.projectchain.internallib.Pair;

public class BasicPurchaserTest {

  private final Mockery context = new Mockery();

  private final ExchangeQuerier querier = context.mock(ExchangeQuerier.class);
  private final Pair<Asset, Asset> market = new Pair<>(Asset.BTC, Asset.USD);

  private ExchangeMock exchangeMock;

  private final BigDecimal purchaseDelta = new BigDecimal("0.1");
  private final BigDecimal offerDelta = new BigDecimal("0.1");

  private BasicPurchaser purchaser;

  public BasicPurchaserTest() {
    refreshExchangeMock();
  }

  private void refreshExchangeMock() {
    Set<Pair<Asset,Asset>> tradingPairs = new HashSet<>();
    tradingPairs.add(market);
    exchangeMock = new ExchangeMock(querier, tradingPairs);
    purchaser = new BasicPurchaser(exchangeMock, market, purchaseDelta, offerDelta);
  }

  @Test
  public void doesOutbidWhenPurchasing() {
    refreshExchangeMock();
    purchaser.startPurchasing();
    exchangeMock.setAllowQuery(true);

    context.checking(new Expectations() {{
      oneOf(querier).limitOrder(market, AssetBook.OrderType.BID, offerDelta, new BigDecimal("500.1")); will(returnValue(Optional.of("blah")));
    }});

    exchangeMock.getTestExchangeBook().putNumberOfOrders(market, AssetBook.OrderType.BID, new BigDecimal("500"), new BigDecimal(5));
    context.assertIsSatisfied();
  }

  @Test
  public void doesCancelPreviousOrders() {
    doesOutbidWhenPurchasing();

    context.checking(new Expectations() {{
      oneOf(querier).tryCancelOrder("blah");
      oneOf(querier).limitOrder(market, AssetBook.OrderType.BID, offerDelta, new BigDecimal("500.3")); will(returnValue(Optional.of("blahblah")));
    }});

    exchangeMock.getTestExchangeBook().putNumberOfOrders(market, AssetBook.OrderType.BID, new BigDecimal("500.2"), new BigDecimal(5));
    context.assertIsSatisfied();
  }

}