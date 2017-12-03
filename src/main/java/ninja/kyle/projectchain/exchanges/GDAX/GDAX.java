package ninja.kyle.projectchain.exchanges.GDAX;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketExtension;
import com.neovisionaries.ws.client.WebSocketFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.function.Consumer;

import ninja.kyle.projectchain.Asset;
import ninja.kyle.projectchain.ExchangeBook;
import ninja.kyle.projectchain.PriceMultimapBuilder;
import ninja.kyle.projectchain.PricePoint;
import ninja.kyle.projectchain.internallib.Pair;

public class GDAX {

  private static GDAX instance;

  private final GDAXJson gdaxJson;
  private final GDAXWSMessageHandler gdaxwsMessageHandler;

  private final WebSocket webSocket;
  private final PriceMultimapBuilder priceMultimapBuilder;

  private final ExchangeBook exchangeBook;

  private GDAX() throws IOException, WebSocketException {
    gdaxJson = new GDAXJson();
    gdaxwsMessageHandler = new GDAXWSMessageHandler();

    webSocket = new WebSocketFactory()
            .setConnectionTimeout(5000)
            .createSocket("wss://ws-feed.gdax.com")
            .addListener(new GDAXWSAdapter())
            .addExtension(WebSocketExtension.PERMESSAGE_DEFLATE)
            .connect();

    ImmutableSet<Pair<Asset, Asset>> monitoredMarkets = ImmutableSet.<Pair<Asset,Asset>>builder()
            .add(new Pair<>(Asset.BTC, Asset.USD))
            .build();

    priceMultimapBuilder = new PriceMultimapBuilder(monitoredMarkets);

    exchangeBook = new ExchangeBook(monitoredMarkets);

    String subMsg = gdaxJson.genSubscribeMarketJSON(monitoredMarkets);
    webSocket.sendText(subMsg);
  }

  private class GDAXWSAdapter extends WebSocketAdapter {

    public void onTextMessage(WebSocket webSocket, String message) {
      gdaxJson.handleWSMessage(message, gdaxwsMessageHandler);
    }

  }

  public class GDAXWSMessageHandler {
      public void handleTicker(Pair<Asset, Asset> tradingPair, BigDecimal price, ZonedDateTime time) {
        priceMultimapBuilder.addPairPrice(tradingPair, price, time);
      }
  }

  public synchronized static GDAX connectToGDAX() {
    if (instance == null) {
      try {
        instance = new GDAX();
      } catch (IOException e) {
        e.printStackTrace();
      } catch (WebSocketException e) {
        e.printStackTrace();
      }
    }
    return instance;
  }

  public void addPriceObserver(Consumer<ImmutableMultimap<Pair<Asset, Asset>, PricePoint>> observer) {
    priceMultimapBuilder.addObserver(observer);
  }

  public void flushPriceData() {
    priceMultimapBuilder.flushMultimap();
  }

}
