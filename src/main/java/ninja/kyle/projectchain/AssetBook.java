package ninja.kyle.projectchain;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.Function;

import ninja.kyle.projectchain.internallib.Pair;

public class AssetBook {
  private final int limit;

  private ConcurrentNavigableMap<BigDecimal, BigDecimal> bids = new ConcurrentSkipListMap<>(Comparator.reverseOrder());
  private ConcurrentNavigableMap<BigDecimal, BigDecimal> asks = new ConcurrentSkipListMap<>();

  public AssetBook(int limit) {
    this.limit = limit;
  }

  public AssetBook() {
    this(0);
  }

  public Pair<BigDecimal, BigDecimal> getMostReasonable(OrderType type) {

    ConcurrentNavigableMap<BigDecimal, BigDecimal> book = type.getBook.apply(this);

    if (book.size() == 0) {
      throw new RuntimeException("Attempted to get most reasonable order of empty book");
    }

    return new Pair<>(type.getBook.apply(this).firstEntry());
  }

  public void putNumberOfOrders(OrderType type, BigDecimal price, BigDecimal number) {
    ConcurrentNavigableMap<BigDecimal, BigDecimal> book = type.getBook.apply(this);

    if (number.equals(BigDecimal.ZERO)) {
      book.remove(price);
      return;
    }

    book.put(price, number);

    if (limit != 0 && book.size() > limit) {
      book.pollLastEntry();
    }
  }

  public enum OrderType {
    BID(b -> b.bids), ASK(b -> b.asks);

    OrderType(Function<AssetBook, ConcurrentNavigableMap<BigDecimal, BigDecimal>> getBook) {
      this.getBook = getBook;
    }

    private final Function<AssetBook, ConcurrentNavigableMap<BigDecimal, BigDecimal>> getBook;
  }
}
