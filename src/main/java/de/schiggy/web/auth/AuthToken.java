package de.schiggy.web.auth;

import java.util.UUID;

public record AuthToken(
        String token,
        UUID playerUuid,
        long createdAt
) {
}
