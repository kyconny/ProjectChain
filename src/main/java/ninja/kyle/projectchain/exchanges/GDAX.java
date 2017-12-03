package ninja.kyle.projectchain.exchanges;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketExtension;
import com.neovisionaries.ws.client.WebSocketFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.function.Consumer;

import ninja.kyle.projectchain.Asset;
import ninja.kyle.projectchain.PriceMultimapBuilder;
import ninja.kyle.projectchain.PricePoint;
import ninja.kyle.projectchain.internallib.Pair;

public class GDAX {

  private static GDAX instance;

  private final WebSocket webSocket;
  private final ImmutableSet<Pair<Asset, Asset>> monitoredMarkets;
  private final PriceMultimapBuilder priceMultimapBuilder;

  private GDAX() throws IOException, WebSocketException {
    webSocket = new WebSocketFactory()
            .setConnectionTimeout(5000)
            .createSocket("wss://ws-feed.gdax.com")
            .addListener(new GDAXWSAdapter())
            .addExtension(WebSocketExtension.PERMESSAGE_DEFLATE)
            .connect();

    monitoredMarkets = ImmutableSet.<Pair<Asset,Asset>>builder()
            .add(new Pair<>(Asset.BTC, Asset.USD))
            .build();

    priceMultimapBuilder = new PriceMultimapBuilder(monitoredMarkets);

    subscribeToChannels();
  }

  private void subscribeToChannels() {
    JsonObject subJson = Json.object();
    subJson.add("type", "subscribe");
    subJson.add("product_ids", Json.array());

    JsonArray channels = Json.array().asArray();

    JsonObject channel = Json.object();
    channel.add("name", "ticker");

    JsonArray marketPairs = Json.array().asArray();

    for (Pair<Asset, Asset> p : monitoredMarkets) {
      marketPairs.add(p.getLeft() + "-" + p.getRight());
    }

    channel.add("product_ids", marketPairs);

    channels.add(channel);

    subJson.add("channels", channels);

    webSocket.sendText(subJson.toString());
  }

  private class GDAXWSAdapter extends WebSocketAdapter {

    public void onTextMessage(WebSocket webSocket, String message) {
      JsonObject messageObj = Json.parse(message).asObject();
      String type = messageObj.get("type").asString();

      if (type.equals("ticker")) {
        String product = messageObj.get("product_id").asString();
        String[] parts = product.split("-");
        Pair<Asset, Asset> tradingPair = new Pair<>(Asset.valueOf(parts[0]), Asset.valueOf(parts[1]));
        String priceStr = messageObj.get("price").asString();
        BigDecimal price = new BigDecimal(priceStr);
        String timeString = messageObj.get("time").asString();

        priceMultimapBuilder.addPairPrice(tradingPair, price, ZonedDateTime.parse(timeString));
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

  public void addPriceObserver(Consumer<ImmutableMultimap<Pair<Asset, Asset>, PricePoint>> observer) {
    priceMultimapBuilder.addObserver(observer);
  }

  public void flushPriceData() {
    priceMultimapBuilder.flushMultimap();
  }

}
