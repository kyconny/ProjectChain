package ninja.kyle.projectchain.marketmaker;

import java.util.Set;

import ninja.kyle.projectchain.exchanges.Exchange;

public class MarketMakerBot {

  private final Set<Exchange> exchanges;

  public MarketMakerBot(Set<Exchange> exchanges) {
    this.exchanges = exchanges;
  }

  public static void main(String[] args) {

  }
}
