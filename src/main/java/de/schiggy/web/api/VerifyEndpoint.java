
package de.schiggy.web.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import de.schiggy.web.SchiggyWeb;
import de.schiggy.web.auth.VerificationSession;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class VerifyEndpoint implements HttpHandler {

    private final SchiggyWeb plugin;

    public VerifyEndpoint(SchiggyWeb plugin) {
        this.plugin = plugin;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            ApiResponse.send(exchange, 405,
                    "{\"error\":\"Method not allowed\"}");
            return;
        }

        byte[] body = exchange.getRequestBody().readNBytes(1025);

        if (body.length > 1024) {
            ApiResponse.send(exchange, 413,
                    "{\"error\":\"Request too large\"}");
            return;
        }

        String request = new String(body, StandardCharsets.UTF_8);

        Matcher matcher = Pattern.compile(
                "\"playerName\"\\s*:\\s*\"([A-Za-z0-9_]{1,16})\""
        ).matcher(request);

        if (!matcher.find()) {
            ApiResponse.send(exchange, 400,
                    "{\"error\":\"Invalid playerName\"}");
            return;
        }

        String playerName = matcher.group(1);

        VerificationSession session =
                plugin.getWebsiteAuth()
                        .getVerificationService()
                        .createVerification(playerName);

        String json = """
                {
                    "success": true,
                    "code": "%s",
                    "playerName": "%s",
                    "expiresAt": %d
                }
                """.formatted(
                session.code(),
                session.playerName(),
                session.expiresAt()
        );

        ApiResponse.send(exchange, 200, json);
    }
}
