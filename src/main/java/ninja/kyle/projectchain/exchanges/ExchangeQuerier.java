package ninja.kyle.projectchain.exchanges;

import java.math.BigDecimal;
import java.util.Optional;

import ninja.kyle.projectchain.Asset;
import ninja.kyle.projectchain.AssetBook;
import ninja.kyle.projectchain.internallib.Pair;

public interface ExchangeQuerier {
  BigDecimal getAmmountOf(Asset asset);
  Optional<String> limitOrder(Pair<Asset, Asset> market, AssetBook.OrderType side, BigDecimal ammount, BigDecimal price);
  boolean tryCancelOrder(String id);
}
