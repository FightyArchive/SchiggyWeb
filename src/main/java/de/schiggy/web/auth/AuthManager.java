package de.schiggy.web.auth;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AuthManager {

    private final Map<String, AuthToken> sessions = new ConcurrentHashMap<>();

    public AuthToken createSession(UUID playerUuid) {
        String token = UUID.randomUUID().toString().replace("-", "");

        AuthToken authToken = new AuthToken(
                token,
                playerUuid,
                System.currentTimeMillis()
        );

        sessions.put(token, authToken);
        return authToken;
    }

    public AuthToken getSession(String token) {
        if (token == null || token.isBlank()) {
            return null;
        }

        return sessions.get(token);
    }

    public void removeSession(String token) {
        if (token != null) {
            sessions.remove(token);
        }
    }

    public void removeAllSessions(UUID playerUuid) {
        sessions.entrySet().removeIf(entry ->
                entry.getValue().playerUuid().equals(playerUuid)
        );
    }
}
