/*package ninja.kyle.projectchain;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.function.Predicate;

import ninja.kyle.projectchain.exchanges.Exchange;
import ninja.kyle.projectchain.internallib.Pair;

public class MarketOperator {

  private final MarketOpIteration marketOpIteration;
  private final Set<Predicate<Pair<Exchange, Pair<Asset, Asset>>>> stopChecks;

  private final Exchange exchange;
  private final Pair<Asset, Asset> market;

  private final ExecutorService executorService;

  private boolean toStop = false;

  public MarketOperator(MarketOpIteration marketOpIteration, Set<Predicate<Pair<Exchange, Pair<Asset, Asset>>>> stopChecks, Exchange exchange, Pair<Asset, Asset> market, ExecutorService executorService) {
    this.marketOpIteration = marketOpIteration;
    this.stopChecks = stopChecks;
    this.exchange = exchange;
    this.market = market;
    this.executorService = executorService;
  }

  public final void operate() {
    executorService.submit(this::operationLoop);
  }

  //We execute an iteration of the marketopiter, which will return true if it wants the controller to stop
  //iterating, if it returns false we do post iteration checks which if any pass we stop iterating.
  private void operationLoop() {
    while (!toStop) {
      if (marketOpIteration.executeIteration(exchange, market) || stopChecks.stream().anyMatch(p -> p.test(new Pair<>(exchange, market)))) {
        toStop = true;
      }

      Thread.sleep();
    }
  }

  public final void stop() {
    toStop = true;
  }

}*/
