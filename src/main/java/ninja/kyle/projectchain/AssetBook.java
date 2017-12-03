package ninja.kyle.projectchain;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.Function;

import ninja.kyle.projectchain.internallib.Pair;

public class AssetBook {
  private ConcurrentNavigableMap<BigDecimal, Integer> bids = new ConcurrentSkipListMap<>(Comparator.reverseOrder());
  private ConcurrentNavigableMap<BigDecimal, Integer> asks = new ConcurrentSkipListMap<>();

  public Pair<BigDecimal, Integer> getMostReasonable(OrderType type) {
    return new Pair<>(type.getBook.apply(this).firstEntry());
  }

  public void putNumberOfOrders(OrderType type, BigDecimal price, Integer number) {
    ConcurrentNavigableMap<BigDecimal, Integer> book = type.getBook.apply(this);

    if (number == 0) {
      book.remove(price);
      return;
    }

    book.put(price, number);
  }

  public enum OrderType {
    BID(b -> b.bids), ASK(b -> b.asks);

    OrderType(Function<AssetBook, ConcurrentNavigableMap<BigDecimal, Integer>> getBook) {
      this.getBook = getBook;
    }

    private final Function<AssetBook, ConcurrentNavigableMap<BigDecimal, Integer>> getBook;
  }
}
