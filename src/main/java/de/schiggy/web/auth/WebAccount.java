package de.schiggy.web.auth;

import java.util.UUID;

public record WebAccount(
        UUID playerUuid,
        String playerName,
        long linkedAt
) {
}
