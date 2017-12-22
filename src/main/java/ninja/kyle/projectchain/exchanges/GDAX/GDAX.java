package ninja.kyle.projectchain.exchanges.GDAX;

import com.google.common.collect.ImmutableSet;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketExtension;
import com.neovisionaries.ws.client.WebSocketFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

import ninja.kyle.projectchain.Asset;
import ninja.kyle.projectchain.AssetBook;
import ninja.kyle.projectchain.exchanges.Exchange;
import ninja.kyle.projectchain.exchanges.ExchangePriority;
import ninja.kyle.projectchain.internallib.Pair;

public class GDAX extends Exchange {

  private static GDAX instance;

  private final GDAXJson gdaxJson;
  private final GDAXWSMessageHandler gdaxwsMessageHandler;

  private final WebSocket webSocket;

  private GDAX() throws IOException, WebSocketException {
    super(new GDAXQuerier(), ImmutableSet.<Pair<Asset,Asset>>builder()
            .add(new Pair<>(Asset.BTC, Asset.GBP))
            .build());

    gdaxJson = new GDAXJson();
    gdaxwsMessageHandler = new GDAXWSMessageHandler();

    webSocket = new WebSocketFactory()
            .setConnectionTimeout(5000)
            .createSocket("wss://ws-feed.gdax.com")
            .addListener(new GDAXWSAdapter())
            .addExtension(WebSocketExtension.PERMESSAGE_DEFLATE)
            .connect();

    String subMsg = gdaxJson.genSubscribeMarketJSON(this.getTradingPairs());
    webSocket.sendText(subMsg);
  }

  @Override
  protected boolean shouldAllowQuery(ExchangePriority priority, int queries) {
    throw new UnsupportedOperationException("Unimplemented");
  }

  private class GDAXWSAdapter extends WebSocketAdapter {

    public void onTextMessage(WebSocket webSocket, String message) {
      gdaxJson.handleWSMessage(message, gdaxwsMessageHandler);
    }

  }

  public class GDAXWSMessageHandler {
      public void handleTicker(Pair<Asset, Asset> tradingPair, BigDecimal price, ZonedDateTime time) {
        getPriceMultimapBuilder().addPairPrice(tradingPair, price, time);
      }

      public void handleL2Data(Pair<Asset, Asset> tradingPair, Pair<BigDecimal, BigDecimal>[] bids, Pair<BigDecimal, BigDecimal>[] asks) {
        for (Pair<BigDecimal, BigDecimal> b : bids) {
          getExchangeBook().putNumberOfOrders(tradingPair, AssetBook.OrderType.BID, b.getLeft(), b.getRight());
        }

        for (Pair<BigDecimal, BigDecimal> a : asks) {
          getExchangeBook().putNumberOfOrders(tradingPair, AssetBook.OrderType.ASK, a.getLeft(), a.getRight());
        }
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

}
