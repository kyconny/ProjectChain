package ninja.kyle.projectchain.exchanges.GDAX;

import com.google.common.collect.ImmutableMap;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import ninja.kyle.projectchain.Asset;
import ninja.kyle.projectchain.internallib.Pair;

public class GDAXJson {

  private final Map<String, BiConsumer<JsonObject, GDAX.GDAXWSMessageHandler>> messageParsers = buildMessageParsers();

  private ImmutableMap<String, BiConsumer<JsonObject, GDAX.GDAXWSMessageHandler>> buildMessageParsers() {
    return ImmutableMap.
            <String, BiConsumer<JsonObject, GDAX.GDAXWSMessageHandler>>builder()
            .put("ticker", GDAXJson::handleTicker)
            .build();
  }

  public String genSubscribeMarketJSON(Set<Pair<Asset, Asset>> monitoredMarkets) {
    JsonObject subJson = Json.object();
    subJson.add("type", "subscribe");

    JsonArray marketPairs = Json.array().asArray();

    for (Pair<Asset, Asset> p : monitoredMarkets) {
      marketPairs.add(p.getLeft() + "-" + p.getRight());
    }

    subJson.add("product_ids", marketPairs);

    JsonArray channels = Json.array().asArray();

    channels.add("level2");

    JsonObject channel = Json.object();
    channel.add("name", "ticker");

    channel.add("product_ids", marketPairs);

    channels.add(channel);

    subJson.add("channels", channels);

    return subJson.toString();
  }

  public void handleWSMessage(String message, GDAX.GDAXWSMessageHandler messageHandler) {
    JsonObject messageObj = Json.parse(message).asObject();
    String type = messageObj.get("type").asString();
    BiConsumer<JsonObject, GDAX.GDAXWSMessageHandler> handler = messageParsers.get(type);

    if (handler == null) {
      Logger.getLogger("GDAX").log(Level.WARNING, "Unknown message from GDAX " + message);
      return;
    }

    handler.accept(messageObj, messageHandler);
  }

  private static void handleTicker(JsonObject object, GDAX.GDAXWSMessageHandler handler) {
    String product = object.get("product_id").asString();
    String[] parts = product.split("-");
    Pair<Asset, Asset> tradingPair = new Pair<>(Asset.valueOf(parts[0]), Asset.valueOf(parts[1]));
    String priceStr = object.get("price").asString();
    BigDecimal price = new BigDecimal(priceStr);
    String timeString = object.get("time").asString();
    handler.handleTicker(tradingPair, price, ZonedDateTime.parse(timeString));
  }

}
