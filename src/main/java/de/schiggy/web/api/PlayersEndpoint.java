
package de.schiggy.web.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import de.schiggy.web.SchiggyWeb;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public final class PlayersEndpoint implements HttpHandler {

    private final SchiggyWeb plugin;

    public PlayersEndpoint(SchiggyWeb plugin) {
        this.plugin = plugin;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            send(exchange, 405, "{\"error\":\"Method not allowed\"}");
            return;
        }

        var onlinePlayers = Bukkit.getOnlinePlayers();

        String players = onlinePlayers.stream()
                .map(Player::getName)
                .map(this::escape)
                .map(name -> "\"" + name + "\"")
                .collect(Collectors.joining(","));

        String json = """
                {
                    "players": [%s],
                    "count": %d
                }
                """.formatted(players, onlinePlayers.size());

        send(exchange, 200, json);
    }

    private String escape(String input) {
        return input.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private void send(HttpExchange exchange, int status, String response)
            throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.getResponseHeaders().set("Cache-Control", "no-store");
        exchange.sendResponseHeaders(status, bytes.length);

        try (var output = exchange.getResponseBody()) {
            output.write(bytes);
        }
    }
}
