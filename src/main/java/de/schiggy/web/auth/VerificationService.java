package de.schiggy.web.auth;

import java.security.SecureRandom;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class VerificationService {

    private static final long CODE_LIFETIME = 5 * 60 * 1000L;

    private final SecureRandom random = new SecureRandom();

    private final Map<String, VerificationSession> sessions =
            new ConcurrentHashMap<>();

    public VerificationSession createVerification(String playerName) {
        if (playerName == null || playerName.isBlank()) {
            throw new IllegalArgumentException("Spielername darf nicht leer sein.");
        }

        String code = generateCode();

        VerificationSession session = new VerificationSession(
                code,
                playerName,
                null,
                System.currentTimeMillis(),
                System.currentTimeMillis() + CODE_LIFETIME
        );

        sessions.put(code, session);
        return session;
    }

    public VerificationSession getVerification(String code) {
        if (code == null || code.isBlank()) {
            return null;
        }

        VerificationSession session = sessions.get(code);

        if (session == null) {
            return null;
        }

        if (System.currentTimeMillis() > session.expiresAt()) {
            sessions.remove(code);
            return null;
        }

        return session;
    }

    public boolean completeVerification(String code, UUID playerUuid, String playerName) {
        VerificationSession session = getVerification(code);

        if (session == null || playerUuid == null || playerName == null) {
            return false;
        }

        if (!session.playerName().equalsIgnoreCase(playerName)) {
            return false;
        }

        sessions.remove(code);
        return true;
    }

    private String generateCode() {
        int number = random.nextInt(1_000_000);
        return String.format("%06d", number);
    }
}
