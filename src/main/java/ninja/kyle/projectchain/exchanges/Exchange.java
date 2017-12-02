package ninja.kyle.projectchain.exchanges;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import ninja.kyle.projectchain.Asset;
import ninja.kyle.projectchain.internallib.Pair;

public interface Exchange {

  Set<Pair<Asset, Asset>>  getTradingPairs();

  CompletableFuture<Double> getRelativePrice(Pair<Asset, Asset> tradingPair);

  //TODO: Trading functionality

}
