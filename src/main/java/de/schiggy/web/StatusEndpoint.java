
package de.schiggy.web.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import de.schiggy.web.SchiggyWeb;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public final class StatusEndpoint implements HttpHandler {

    private final SchiggyWeb plugin;

    public StatusEndpoint(SchiggyWeb plugin) {
        this.plugin = plugin;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            send(exchange, 405, "{\"error\":\"Method not allowed\"}");
            return;
        }

        int online = Bukkit.getOnlinePlayers().size();
        int maximum = Bukkit.getMaxPlayers();

        String json = """
                {
                    "server": "SCHIGGYSMP",
                    "online": true,
                    "playersOnline": %d,
                    "maxPlayers": %d,
                    "ip": "schiggysmp.de"
                }
                """.formatted(online, maximum);

        send(exchange, 200, json);
    }

    private void send(HttpExchange exchange, int status, String response)
            throws IOException {

        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().set(
                "Content-Type", "application/json; charset=UTF-8"
        );

        exchange.getResponseHeaders().set(
                "Cache-Control", "no-store"
        );

        exchange.sendResponseHeaders(status, bytes.length);

        try (var output = exchange.getResponseBody()) {
            output.write(bytes);
        }
    }
}
