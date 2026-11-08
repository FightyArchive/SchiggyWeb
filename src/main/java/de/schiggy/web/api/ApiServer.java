
package de.schiggy.web.api;

import com.sun.net.httpserver.HttpServer;
import de.schiggy.web.SchiggyWeb;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public final class ApiServer {

    private final SchiggyWeb plugin;
    private HttpServer server;

    public ApiServer(SchiggyWeb plugin) {
        this.plugin = plugin;
    }

    public void start() {
        try {
            int port = plugin.getConfig().getInt("web.port", 8765);
            String host = plugin.getConfig().getString(
                    "web.bind-address", "0.0.0.0"
            );

            server = HttpServer.create(
                    new InetSocketAddress(host, port), 0
            );

            server.createContext("/api/status",
                    new StatusEndpoint(plugin));

            server.createContext("/api/players",
                    new PlayersEndpoint(plugin));

            server.createContext("/api/verify",
                    new VerifyEndpoint(plugin));

            server.setExecutor(
                    Executors.newFixedThreadPool(4)
            );

            server.start();

            plugin.getLogger().info(
                    "SchiggyWeb API läuft auf Port " + port
            );

        } catch (IOException exception) {
            plugin.getLogger().severe(
                    "API konnte nicht gestartet werden: "
                            + exception.getMessage()
            );
        }
    }

    public void stop() {
        if (server != null) {
            server.stop(1);
            plugin.getLogger().info("SchiggyWeb API gestoppt.");
        }
    }
}
