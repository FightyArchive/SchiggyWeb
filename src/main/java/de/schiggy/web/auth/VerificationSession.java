package de.schiggy.web.auth;

import java.util.UUID;

public record VerificationSession(
        String code,
        String playerName,
        UUID playerUuid,
        long createdAt,
        long expiresAt
) {
}
