package ninja.kyle.projectchain.exchanges;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import ninja.kyle.projectchain.Asset;
import ninja.kyle.projectchain.internallib.Pair;

public abstract class Exchange extends Submitter {

  public Exchange(ExecutorService executorService) {
    super(executorService);
  }

  public abstract Set<Pair<Asset, Asset>> getTradingPairs();

  public CompletableFuture<Double> askRelativePrice(Pair<Asset, Asset> tradingPair) {
    return CompletableFuture.supplyAsync(() -> getRelativePrice(tradingPair),
            super.getExecutorService());
  }

  abstract double getRelativePrice(Pair<Asset, Asset> tradingPair);

  //TODO: Trading functionality

}
